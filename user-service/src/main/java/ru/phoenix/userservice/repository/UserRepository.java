package ru.phoenix.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.phoenix.userservice.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}