package com.kamal.easybus.service;

import com.kamal.easybus.model.User;
import com.kamal.easybus.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepo);
    }

    @Test
    void canGetAllUsers(){
        underTest.getAllUsers();
        verify(userRepo).findAll();
    }

    @Test
    void canAddUser(){
        User user = new User("Kamal", "Ezzarmou", "kamal@admin.com", "kamal","066");
        underTest.addUser(user);

        ArgumentCaptor<User> userArgumentCaptor =
                ArgumentCaptor.forClass(User.class);
        verify(userRepo)
                .save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);
    }


    @Test
    void canDeleteUser(){
        long id = 1;
        given(userRepo.existsById(id))
                .willReturn(true);
        // when
        underTest.deleteUser(id);

        // then
        verify(userRepo).deleteById(id);
    }


}
