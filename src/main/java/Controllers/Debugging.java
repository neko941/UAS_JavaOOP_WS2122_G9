package Controllers;

import Models.User;

import static Controllers.Security.sha512;

public class Debugging {
    public static void printUserInfo(User user)
    {
        System.out.format("First Name: %s\n", user.getFirstname());
        System.out.format("Last Name: %s\n", user.getLastname());
        System.out.format("Username: %s\n", user.getUsername());
        System.out.format("Password: %s\n", sha512(user.getFirstname()));
        System.out.format("Email: %s\n", user.getEmail());
    }
}
