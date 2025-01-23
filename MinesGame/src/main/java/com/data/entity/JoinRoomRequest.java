package com.data.entity;

 
import lombok.Data;

@Data
public class JoinRoomRequest {
    private String code; // Room code provided by the user
    private String name; // User's name provided by the user
}
