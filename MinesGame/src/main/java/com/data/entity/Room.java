package com.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
@Data
@Document("rooms")
public class Room {
    @Id
    private String id;
    private String code; // 6-digit room code

    @DBRef
    private List<User> users = new ArrayList<>();
     
    @DBRef
    private List<Game> games = new ArrayList<>();
    
    private LocalDateTime createdAt; // Room creation time
    private int timeout; // Timeout in minutes (set by admin)
    private boolean isClosed = false;
 }
