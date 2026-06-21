package ru.phoenix.dao;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.phoenix.task2.dao.UserDaoImpl;
import ru.phoenix.task2.entity.User;
import ru.phoenix.task2.util.HibernateUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoImplIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("test_db")
                    .withUsername("postgres")
                    .withPassword("postgres");

    private static SessionFactory sessionFactory;

    private UserDaoImpl userDao;

    @BeforeAll
    static void beforeAll() {

        sessionFactory =
                HibernateUtil.buildSessionFactory(
                        postgres.getJdbcUrl(),
                        postgres.getUsername(),
                        postgres.getPassword()
                );
    }

    @AfterAll
    static void afterAll() {

        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @BeforeEach
    void setUp() {

        userDao =
                new UserDaoImpl(sessionFactory);

        clearDatabase();
    }

    private void clearDatabase() {

        try (var session =
                     sessionFactory.openSession()) {

            var transaction =
                    session.beginTransaction();

            session.createMutationQuery(
                    "DELETE FROM User"
            ).executeUpdate();

            transaction.commit();
        }
    }

    @Test
    void save_ShouldPersistUser() {

        User user =
                new User(
                        "Mikhail",
                        "mikhail@test.com",
                        25
                );

        userDao.save(user);

        assertNotNull(user.getId());

        User saved =
                userDao.findById(user.getId());

        assertNotNull(saved);

        assertEquals(
                "Mikhail",
                saved.getName()
        );
    }

    @Test
    void findById_ShouldReturnUser() {

        User user =
                new User(
                        "Roman",
                        "roman@test.com",
                        30
                );

        userDao.save(user);

        User found =
                userDao.findById(user.getId());

        assertNotNull(found);

        assertEquals(
                "Roman",
                found.getName()
        );
    }

    @Test
    void findAll_ShouldReturnAllUsers() {

        userDao.save(
                new User(
                        "User1",
                        "u1@test.com",
                        20
                )
        );

        userDao.save(
                new User(
                        "User2",
                        "u2@test.com",
                        25
                )
        );

        List<User> users =
                userDao.findAll();

        assertEquals(
                2,
                users.size()
        );
    }

    @Test
    void update_ShouldUpdateUser() {

        User user =
                new User(
                        "Old Name",
                        "old@test.com",
                        20
                );

        userDao.save(user);

        user.setName("New Name");

        userDao.update(user);

        User updated =
                userDao.findById(user.getId());

        assertEquals(
                "New Name",
                updated.getName()
        );
    }

    @Test
    void delete_ShouldRemoveUser() {

        User user =
                new User(
                        "Delete Me",
                        "delete@test.com",
                        22
                );

        userDao.save(user);

        Long id = user.getId();

        userDao.delete(id);

        User deleted =
                userDao.findById(id);

        assertNull(deleted);
    }
}