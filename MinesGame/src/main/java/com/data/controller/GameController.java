package com.data.controller;

 
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.data.entity.CashoutResponse;
import com.data.entity.Game;
import com.data.entity.GameRequest;
import com.data.entity.MoveRequest;
import com.data.entity.User;
import com.data.repository.URepo;
import com.data.service.GameService;
import com.data.service.RoomService;

@RestController
@RequestMapping("/rooms/{roomCode}/games")
@CrossOrigin(origins = "http://localhost:8081", allowedHeaders = "*", allowCredentials = "true")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired URepo urepo;
    
    @Autowired RoomService roomService;
    /**
     * Starts a game in the specified room.
     * @param roomCode the code of the room
     * @param request the game request containing bet amount and number of mines
     * @return the started game
     */
    @CrossOrigin
    @PostMapping("/start")
    public Game startGame(
        @PathVariable String roomCode, 
        @RequestBody GameRequest request,
        Principal principal) { // Principal represents the currently logged-in user
        // Fetch the current user from the database
        User user = urepo.findByName(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Start the game with the current user
        return roomService.startGameInRoom(roomCode, request, user);
    }

    /** 
     * Makes a move in a game.
     * @param request the move request containing gameId and move details
     * @return the updated game state
     */
    @CrossOrigin
    @PostMapping("/move")
    public Game makeMove(@RequestBody MoveRequest request) {
        return gameService.makeMove(request);
    }

    /**
     * Cashes out from a game.
     * @param gameId the ID of the game
     * @return the cashout response
     */
    @CrossOrigin
    @PostMapping("/{gameId}/cashout")
    public CashoutResponse cashout(@PathVariable String gameId) {
        return gameService.cashout(gameId);
    }
}
