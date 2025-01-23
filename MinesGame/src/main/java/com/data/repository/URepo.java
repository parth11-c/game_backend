package com.data.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.data.entity.User;

public interface URepo extends MongoRepository<User, String> {
	
	public  Optional<User>   findByName(String name);

}
