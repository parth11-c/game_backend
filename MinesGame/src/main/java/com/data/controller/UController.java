package com.data.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.data.entity.User;
import com.data.entity.UserDTO;
import com.data.service.UService;

@RestController
 public class UController {

	@Autowired
	private UService uservice;
	

	
	
    @CrossOrigin
	@GetMapping("/current-user")
	public String getCurrentuser(Principal principal) {
		
		return principal.getName(); 
	}
	
	
    @PreAuthorize("hasRole('ADMIN','OWNER')")
	@PutMapping("/admin/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*") // Enable CORS for all origins and headers

    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User updatedUser)
	{
        User user = uservice.updateUser(id, updatedUser);
        return ResponseEntity.ok(user);
		
}
    @CrossOrigin
    @PreAuthorize("hasRole('OWNER')")
	@PutMapping("/owner/{id}")
    public ResponseEntity<User> updateAdmin(@PathVariable String id, @RequestBody User updatedUser)
	{
        User user = uservice.updateUser(id, updatedUser);
        return ResponseEntity.ok(user);
		
}
    
    @CrossOrigin
    @GetMapping("/")
    public String home() {
        return "Hello World";
    }
    
    @CrossOrigin
    @PreAuthorize("hasRole('OWNER')") 
    @DeleteMapping("/owner/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        uservice.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully!");
    }
    
    
    @CrossOrigin
    @GetMapping("/getallusers")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = uservice.getUsers();
        return ResponseEntity.ok(users);
    }
    
    
    
    
    
}


