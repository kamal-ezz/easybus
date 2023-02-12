package com.kamal.easybus.web;

import com.kamal.easybus.dtos.UserDTO;
import com.kamal.easybus.entities.User;
import com.kamal.easybus.exceptions.ResourceNotFoundException;
import com.kamal.easybus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getUsers(){
        List<UserDTO> users = userService.getAllUsers();
        if(users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") long id){
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUser(@PathVariable("id") long id, @RequestBody UserDTO user) {
        try{
            userService.updateUser(id,user);
            return new ResponseEntity<>("User successfully updated", HttpStatus.OK);
        }catch (ResourceNotFoundException e){
            return new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
        }catch (ResourceNotFoundException e){
            return new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
        }
    }
}
