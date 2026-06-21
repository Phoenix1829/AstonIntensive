package ru.phoenix.task2.service;

import ru.phoenix.task2.dao.UserDao;
import ru.phoenix.task2.dao.UserDaoImpl;
import ru.phoenix.task2.entity.User;

import java.util.List;

public class UserService {

    private final UserDao userDao;

    public UserService() {
        this(new UserDaoImpl());
    }

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createUser(String name,
                           String email,
                           Integer age) {

        User user = new User(
                name,
                email,
                age
        );

        userDao.save(user);
    }

    public User getUserById(Long id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public void updateUser(Long id,
                           String name,
                           String email,
                           Integer age) {

        User user = userDao.findById(id);

        if (user == null) {
            throw new IllegalArgumentException(
                    "User not found. ID = " + id
            );
        }

        user.setName(name);
        user.setEmail(email);
        user.setAge(age);

        userDao.update(user);
    }

    public void deleteUser(Long id) {
        userDao.delete(id);
    }
}