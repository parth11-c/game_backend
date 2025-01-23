package com.data.entity;

import lombok.Data;

@Data
public class MoveRequest {

    private String gameId;
    private int move; // Position clicked (0 to 24)

    // Getters and setters
}
