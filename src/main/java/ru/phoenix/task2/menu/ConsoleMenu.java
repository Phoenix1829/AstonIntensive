package ru.phoenix.task2.menu;

import ru.phoenix.task2.entity.User;
import ru.phoenix.task2.service.UserService;

import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private final UserService userService;
    private final Scanner scanner;

    public ConsoleMenu() {
        this.userService = new UserService();
        this.scanner = new Scanner(System.in);
    }

    public void start() {

        boolean running = true;

        while (running) {

            printMenu();

            System.out.print("Choose option: ");

            String input = scanner.nextLine();

            try {

                switch (input) {

                    case "1" -> createUser();

                    case "2" -> findUserById();

                    case "3" -> showAllUsers();

                    case "4" -> updateUser();

                    case "5" -> deleteUser();

                    case "0" -> {
                        running = false;
                        System.out.println("Application closed.");
                    }

                    default ->
                            System.out.println("Invalid option.");

                }

            } catch (Exception e) {

                System.out.println(
                        "Error: " + e.getMessage()
                );

            }
        }
    }

    private void printMenu() {

        System.out.println();
        System.out.println("===== USER SERVICE =====");
        System.out.println("1. Create user");
        System.out.println("2. Find user by id");
        System.out.println("3. Show all users");
        System.out.println("4. Update user");
        System.out.println("5. Delete user");
        System.out.println("0. Exit");
        System.out.println();
    }

    private void createUser() {

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Age: ");
        Integer age =
                Integer.parseInt(scanner.nextLine());

        userService.createUser(
                name,
                email,
                age
        );

        System.out.println("User created.");
    }

    private void findUserById() {

        System.out.print("ID: ");

        Long id =
                Long.parseLong(scanner.nextLine());

        User user =
                userService.getUserById(id);

        if (user == null) {

            System.out.println(
                    "User not found."
            );

            return;
        }

        System.out.println(user);
    }

    private void showAllUsers() {

        List<User> users =
                userService.getAllUsers();

        if (users.isEmpty()) {

            System.out.println(
                    "No users found."
            );

            return;
        }

        users.forEach(System.out::println);
    }

    private void updateUser() {

        System.out.print("ID: ");
        Long id =
                Long.parseLong(scanner.nextLine());

        System.out.print("New name: ");
        String name = scanner.nextLine();

        System.out.print("New email: ");
        String email = scanner.nextLine();

        System.out.print("New age: ");
        Integer age =
                Integer.parseInt(scanner.nextLine());

        userService.updateUser(
                id,
                name,
                email,
                age
        );

        System.out.println(
                "User updated."
        );
    }

    private void deleteUser() {

        System.out.print("ID: ");

        Long id =
                Long.parseLong(scanner.nextLine());

        userService.deleteUser(id);

        System.out.println(
                "User deleted."
        );
    }
}