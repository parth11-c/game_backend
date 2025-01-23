package com.data.controller;

import com.data.entity.Room;
import com.data.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*") // Enable CORS for all origins and headers
@RestController
@RequestMapping("/api")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @PostMapping("/admin/create-room")
    public ResponseEntity<?> createRoom(@RequestParam int timeoutMinutes) {
        try {
            Room room = roomService.createRoom(timeoutMinutes);
            return ResponseEntity.status(HttpStatus.CREATED).body(room);
        } catch (RuntimeException ex) {
            return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/join-room")
    public ResponseEntity<?> joinRoom(@RequestParam String code) {
        try {
            Room room = roomService.joinRoom(code);
            return ResponseEntity.ok(room);
        } catch (RuntimeException ex) {
            return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

 

    @CrossOrigin
    @GetMapping("/{roomCode}/leaderboard")
    public ResponseEntity<?> getLeaderboard(@PathVariable String roomCode) {
        try {
            Map<String, Double> leaderboard = roomService.getLeaderboard(roomCode);
            return ResponseEntity.ok(leaderboard);
        } catch (RuntimeException ex) {
            return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<?> buildErrorResponse(String errorMessage, HttpStatus status) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", errorMessage);
        return ResponseEntity.status(status).body(errorResponse);
    }
}