package com.data.entity;

import lombok.Data;

 

@Data
public class CashoutResponse {
    private String gameId;
    private double betAmount;
    private double cashoutAmount;
    private String gameState;
}

