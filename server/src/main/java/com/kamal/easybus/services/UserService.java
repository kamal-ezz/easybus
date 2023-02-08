package com.kamal.easybus.services;

import com.kamal.easybus.dtos.UserDTO;
import com.kamal.easybus.exceptions.ResourceNotFoundException;
import com.kamal.easybus.entities.User;
import com.kamal.easybus.repos.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    UserRepo userRepo;
    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    public UserDTO mapUserToUserDTO(User user){
        return UserDTO.builder().firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .email(user.getEmail())
                                .phone(user.getPhone()).build();
    }


    public User mapUserDTOToUser(UserDTO userDTO, String password){
        return User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(password)
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .build();
    }

    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream().map(this::mapUserToUserDTO).collect(Collectors.toList());
    }

    public UserDTO getUserById(long id) {
       return mapUserToUserDTO(userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User","id",id)));
    }


    public void addUser(User user){
        userRepo.save(user);
    }


    public void updateUser(long id, UserDTO user) {
        User _user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        _user.setFirstName(user.getFirstName());
        _user.setLastName(user.getLastName());
        _user.setEmail(user.getEmail());
        _user.setPhone(user.getPhone());
        userRepo.save(_user);
    }

    public void deleteUser(long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepo.delete(user);
    }
}
