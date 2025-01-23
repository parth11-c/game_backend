package com.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "games")
public class Game {

    @Id
    private String id;
    private double betAmount;

    @DBRef // Ensure this annotation is present
    private User user; // Reference to User object

    private Set<Integer> mines = new HashSet<>();
    private Set<Integer> revealed = new HashSet<>();
    private String gameState; // "IN_PROGRESS", "WON", "LOST"
    private double multiplier; // New field for the multiplier
}