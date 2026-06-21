package ru.phoenix.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.phoenix.task2.dao.UserDao;
import ru.phoenix.task2.entity.User;
import ru.phoenix.task2.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDao = mock(UserDao.class);
        userService = new UserService(userDao);
    }

    @Test
    void createUser_ShouldSaveUser() {

        userService.createUser(
                "Mikhail",
                "mikhail@test.com",
                25
        );

        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        verify(userDao).save(captor.capture());

        User savedUser = captor.getValue();

        assertEquals("Mikhail", savedUser.getName());
        assertEquals("mikhail@test.com", savedUser.getEmail());
        assertEquals(25, savedUser.getAge());
    }

    @Test
    void getUserById_ShouldReturnUser() {

        User user =
                new User("Mikhail",
                        "mikhail@test.com",
                        25);

        when(userDao.findById(1L))
                .thenReturn(user);

        User result =
                userService.getUserById(1L);

        assertEquals(user, result);

        verify(userDao)
                .findById(1L);
    }

    @Test
    void getAllUsers_ShouldReturnUsers() {

        List<User> users =
                List.of(
                        new User(
                                "Mikhail",
                                "a@mail.com",
                                25
                        ),
                        new User(
                                "Roman",
                                "b@mail.com",
                                30
                        )
                );

        when(userDao.findAll())
                .thenReturn(users);

        List<User> result =
                userService.getAllUsers();

        assertEquals(2, result.size());

        verify(userDao).findAll();
    }

    @Test
    void updateUser_ShouldUpdateExistingUser() {

        User user =
                new User(
                        "Old",
                        "old@mail.com",
                        20
                );

        when(userDao.findById(1L))
                .thenReturn(user);

        userService.updateUser(
                1L,
                "New",
                "new@mail.com",
                30
        );

        assertEquals("New",
                user.getName());

        assertEquals("new@mail.com",
                user.getEmail());

        assertEquals(30,
                user.getAge());

        verify(userDao).update(user);
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {

        when(userDao.findById(1L))
                .thenReturn(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUser(
                        1L,
                        "New",
                        "new@mail.com",
                        30
                )
        );

        verify(userDao, never())
                .update(any());
    }

    @Test
    void deleteUser_ShouldCallDao() {

        userService.deleteUser(1L);

        verify(userDao)
                .delete(1L);
    }
}