package com.data.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.data.entity.Game;
import com.data.entity.User;

public interface GameRepository extends MongoRepository<Game, String> {

 
 
}
