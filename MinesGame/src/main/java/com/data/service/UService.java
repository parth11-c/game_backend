package com.data.service;

 

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.data.entity.User;
import com.data.entity.UserDTO;
import com.data.repository.URepo;

@Service
public class UService {

    @Autowired
	private URepo urepo;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
	
    public List<UserDTO> getUsers() {
        return urepo.findAll().stream()
            .map(user -> new UserDTO(
                user.getId(),
                user.getName(),
                user.getRole() // Convert role directly to string
            ))
            .collect(Collectors.toList());
    }
	
	public  User  CreateUser(User user) {
		
		user.setId(UUID.randomUUID().toString());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return urepo.save(user);
		 
	}
	
    public Optional<User> findByName(String name) {
        return urepo.findByName(name);
    }
 
 
    public User updateUser(User user) {
        return urepo.save(user);
    }

    public User updateUser(String id, User updatedUser) {
        Optional<User> optionalUser = urepo.findById(id);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found with id: " + id);
        }

        User existingUser = optionalUser.get();

        // Update fields
        if (updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }

  

        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(updatedUser.getPassword()); // Optionally encode password
        }

        if (updatedUser.getRole() != null) {
            existingUser.setRole(updatedUser.getRole());
        }

   

        // Save updated user
        return urepo.save(existingUser);
    }
    
    
    public void deleteUser(String id) {
        if (!urepo.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        urepo.deleteById(id);
    }
	
}
