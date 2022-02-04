package Controllers;

import Models.Event;
import Models.User;

import static Controllers.Security.sha512;

public class Debugging {
    public static void printUserInfo(User user)
    {
        System.out.format("First Name: %s\n", user.getFirstname());
        System.out.format("Last Name: %s\n", user.getLastname());
        System.out.format("Username: %s\n", user.getUsername());
        System.out.format("Password: %s\n", sha512(user.getFirstname()));
        System.out.format("Email: %s\n\n", user.getEmail());
    }

    public static void printEventInfo(Event event)
    {
        System.out.format("Event Name: %s\n", event.getEventName());
        System.out.format("Date: %s\n", event.getDate());
        System.out.format("Time: %s\n", event.getTime());
        System.out.format("Duration: %d\n", event.getDuration());
        System.out.format("Reminder: %s\n\n", event.getReminder());
    }
}

