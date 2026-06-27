package ru.phoenix.task2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.phoenix.task2.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}