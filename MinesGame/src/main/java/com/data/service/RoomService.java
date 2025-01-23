package com.data.service;

import com.data.entity.Game;
import com.data.entity.GameRequest;
import com.data.entity.Room;
import com.data.entity.User;
import com.data.repository.GameRepository;
import com.data.repository.RoomRepository;
import com.data.repository.URepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private URepo userRepository;

    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private GameService gameService;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    public Room createRoom(int timeoutMinutes) {
        Room room = new Room();
        room.setCode(generateRoomCode());
        room.setCreatedAt(LocalDateTime.now());
        room.setTimeout(timeoutMinutes);
        room.setClosed(false);

        Room savedRoom = roomRepository.save(room);

        // Schedule the room to be closed after the timeout
        scheduleRoomClosure(savedRoom.getId(), timeoutMinutes);

        return savedRoom;
    }

    public Room joinRoom(String code) {
        Room room = roomRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (room.isClosed()) {
            throw new IllegalStateException("Room is closed. No more users can join.");
        }

        return room;
    }

    private String generateRoomCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    private void scheduleRoomClosure(String roomId, int timeoutMinutes) {
        long delayMillis = Duration.ofMinutes(timeoutMinutes).toMillis();
        taskScheduler.schedule(() -> closeRoom(roomId), new Date(System.currentTimeMillis() + delayMillis));
    }

    private void closeRoom(String roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setClosed(true);
        roomRepository.save(room);
    }

    /**
     * Fetches the leaderboard for a specific room.
     * @param roomCode The code of the room.
     * @return A map of usernames to their total cashout amounts, sorted in descending order.
     */
    public Map<String, Double> getLeaderboard(String roomCode) {
        // Fetch the room by its code
        Room room = roomRepository.findByCode(roomCode)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Check if the room has games
        if (room.getGames() == null || room.getGames().isEmpty()) {
            throw new RuntimeException("No games found in the specified room.");
        }

        // Create a map to store userName -> totalCashout
        Map<String, Double> leaderboard = new HashMap<>();

        // Iterate over each game in the room
        for (Game game : room.getGames()) {
            User user = game.getUser(); // Fetch User directly from the Game
            if (user != null) {
                // Use the user's username or ID for the leaderboard
                String userName = user.getUsername(); // Assuming User has a getUsername() method
                double cashoutAmount = calculateCashout(game);
                leaderboard.put(userName, leaderboard.getOrDefault(userName, 0.0) + cashoutAmount);
            }
        }

        // Sort the map in descending order of totalCashout
        return leaderboard.entrySet()
                .stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())) // Sort by value (descending)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // Merge function (not needed here)
                        LinkedHashMap::new // Use LinkedHashMap to preserve order
                ));
    }

    /**
     * Calculates the cashout amount for a specific game.
     * @param game The game object.
     * @return The cashout amount.
     */
    private double calculateCashout(Game game) {
        if ("WON".equals(game.getGameState()) || "CASHED_OUT".equals(game.getGameState())) {
            return game.getBetAmount() * game.getMultiplier();
        } else {
            return 0.0; // No cashout for games that are lost or in progress
        }
    }
    
    public Game startGameInRoom(String roomCode, GameRequest request, User user) {
        // Fetch the room by its code
        Room room = roomRepository.findByCode(roomCode)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Create a new game
        Game game = new Game();
        game.setBetAmount(request.getBetAmount());
        game.setMines(generateMines(request.getNumMines()));
        game.setRevealed(new HashSet<>()); // Initialize revealed cells
        game.setGameState("IN_PROGRESS");
        game.setMultiplier(1.0); // Initial multiplier is 1.0
        game.setUser(user); // Set the user reference

        // Save the game and add it to the room
        gameRepository.save(game);
        room.getGames().add(game);
        roomRepository.save(room);

        return game;
    }
    
    private Set<Integer> generateMines(int numMines) {
        Set<Integer> mines = new HashSet<>();
        Random random = new Random();
        while (mines.size() < numMines) {
            mines.add(random.nextInt(25)); // 5x5 grid
        }
        return mines;
    }
}