package com.kamal.easybus.service;

import com.kamal.easybus.exception.ResourceNotFoundException;
import com.kamal.easybus.model.User;
import com.kamal.easybus.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUserById(long id) {
       return userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User","id",id));
    }


    public User addUser(User user) {
        return userRepo.save(user);
    }

    public User updateUser(long id, User user) {
        Optional<User> userData = userRepo.findById(id);

        if (userData.isPresent()) {
            User _user = userData.get();
            _user.setFirstName(user.getFirstName());
            _user.setLastName(user.getLastName());
            _user.setEmail(user.getEmail());
            _user.setPassword(user.getPassword());
            _user.setPhone(user.getPhone());
            return _user;
        }else {
            return null;
        }
    }

    public void deleteUser(long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        userRepo.delete(user);
    }
}
