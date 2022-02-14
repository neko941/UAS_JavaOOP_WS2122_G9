/**
 * @author neko941, jatenderjossan
 * Created on: Dec. 28, 2021
 *
 * This class provides model for an event
 */

package Models;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event implements Comparable<Event>{

    // ID of the event
    private int eventID;
    // name of the event
    private String eventName;
    // date of the event
    private LocalDate date;
    // starting time of the event
    private LocalTime time;
    // duration of the event
    private int duration;
    // participants participating
    private ArrayList<User> participants;
    // list of emails
    private String[] emails;
    // attachments needed for the event
    private ArrayList<File> attachments;
    // location of the event
    private Location location;
    // reminder for the event
    private Reminder e_reminder;
    // the priority of the event
    private Priority e_priority;

    private LocalDateTime reminderTime;

    /**
     * Constructor for creating an event
     *
     * @param eventName - name of the event
     * @param date - date of the event
     * @param time - starting time of the event
     * @param duration - duration of the event in minutes
     * @param location - location of the event
     * @param participants - list of participants
     * @param attachments - list of attachments
     * @param reminder - selected reminder for the event
     * @param priority - selected priority for the event
     */
    public Event(String eventName, LocalDate date, LocalTime time, int duration, Location location,
                 ArrayList<User> participants, String[] emails, ArrayList<File> attachments, Reminder reminder, Priority priority) {
        this.eventName = eventName;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.participants = participants;
        this.emails = emails;
        this.attachments = attachments;
        this.location = location;
        this.e_reminder = reminder;
        this.e_priority = priority;
    }

    /**
     * Constructor for fetching an event
     *
     * @param eventID - ID of the event
     * @param eventName - name of the event
     * @param date - date of the event
     * @param time - starting time of the event
     * @param duration - duration of the event in minutes
     * @param participants - list of participants
     * @param attachments - list of attachments
     * @param location - location of the event
     * @param reminder - selected reminder for the event
     * @param priority - selected priority for the event
     */
    public Event(int eventID, String eventName, LocalDate date, LocalTime time, int duration, Location location,
                 ArrayList<User> participants, String[] emails, ArrayList<File> attachments, Reminder reminder, Priority priority) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.participants = participants;
        this.emails = emails;
        this.attachments = attachments;
        this.location = location;
        this.e_reminder = reminder;
        this.e_priority = priority;
    }

    @Override
    public int compareTo(Event e) {
        if(e.getReminder() != null)
        {
            return this.getReminder().getReminderTime(LocalDateTime.of(this.getDate(), this.getTime()))
                    .compareTo(e.getReminder().getReminderTime(LocalDateTime.of(e.getDate(), e.getTime())));
        }
        else
        {
            return 0;
        }
    }

    @Override
    public String toString()
    {
        ArrayList<String> event = new ArrayList<>();
        event.add(String.valueOf(this.getEventID()));
        event.add(String.valueOf(this.getEventName()));
        event.add(String.valueOf(this.getDate()));
        event.add(String.valueOf(this.getTime()));
        event.add(String.valueOf(this.getDuration()));
        event.add(String.valueOf(this.getLocation().toString()));

        return String.join("; ", event);
    }

    /**
     * Constructor for fetching an event for reminder
     *
     * @param eventID - ID of the event
     * @param eventName - name of the event
     * @param date - date of the event
     * @param time - starting time of the event
     * @param duration - duration of the event in minutes
     * @param participants - list of participants
     * @param attachments - list of attachments
     * @param location - location of the event
     * @param reminder - selected reminder for the event
     * @param priority - selected priority for the event
     * @param reminderTime - the actual time to send reminder
     */
    public Event(int eventID,
                 String eventName,
                 LocalDate date,
                 LocalTime time,
                 int duration,
                 Location location,
                 ArrayList<User> participants,
                 String[] emails,
                 ArrayList<File> attachments,
                 Reminder reminder,
                 Priority priority,
                 LocalDateTime reminderTime) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.participants = participants;
        this.emails = emails;
        this.attachments = attachments;
        this.location = location;
        this.e_reminder = reminder;
        this.e_priority = priority;
        this.reminderTime = reminderTime;
    }

    public void printInfo()
    {
        System.out.format("Event Name: %s\n", this.getEventName());
        System.out.format("Event ID: %d\n", this.getEventID());
        System.out.format("Event Reminder: %s\n", this.getReminder());
        System.out.format("Event Reminder Time: %s\n\n", this.getReminderTime());
    }
    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * @param time - starting time of the event
     */
    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setParticipants(ArrayList<User> participants) {
        this.participants = participants;
    }

    public void setEmails(String[] emails) {
        this.emails = emails;
    }

    public void setAttachments(ArrayList<File> attachments) {
        this.attachments = attachments;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setReminder(Reminder reminder) {
        this.e_reminder = reminder;
    }

    public void setPriority(Priority priority) {
        this.e_priority = priority;
    }

    public int getEventID() {
        return eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public LocalDate getDate() {
        return date;
    }

    /**
     * @return starting time of an event
     */
    public LocalTime getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }

    public ArrayList<User> getParticipants() {
        return participants;
    }

    public String[] getEmails() {
        return emails;
    }

    public ArrayList<File> getAttachments() {
        return attachments;
    }

    public Location getLocation() {
        return location;
    }

    public Reminder getReminder() {
        return e_reminder;
    }

    public Priority getPriority() {
        return e_priority;
    }

    public LocalDateTime getReminderTime() {
        return reminderTime;
    }
}
