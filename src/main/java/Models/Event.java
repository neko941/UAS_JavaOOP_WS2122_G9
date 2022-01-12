/**
 * Author: neko941
 * Created on: 2021-12-13
 *
 * This class provides model for an event
 */

package Models;

import java.util.ArrayList;
import java.util.Date;

public class Event
{
    /**
     * @param eventID: unique
     * @param eventName
     * @param eventStartTime
     * @param eventEndTime
     * @param reminderTime: exact time to send reminder
     * @param participants
     * @param priority
     *
     * in java.util.Date, year = year - 1900, month = month - 1
     */

    private String eventID;
    private String eventName;
    private Date eventStartTime;
    private Location location;
    private Date reminderTime;
    private ArrayList<User> participants;
    private String priority;



    // constructor
    public Event(String eventName, Date eventStartTime, Date reminderTime, Location location, ArrayList<User> participants, String priority)
    {
        this.eventName = eventName;
        this.eventStartTime = eventStartTime;
        this.reminderTime = reminderTime;
        this.location = location;
        // this.participants = new ArrayList<User>();
        this.participants = participants;
        this.priority = priority;
    }

    // getters and setters
    public String getEventID() {return this.eventID;}
    public void setEventID(String eventID) {this.eventID = eventID;}

    public String getName() {return this.eventName;}
    public void setName(String eventName) {this.eventName = eventName;}

    public Date getEventStartTime() {return this.eventStartTime;}
    public void setDate(Date eventStartTime) {this.eventStartTime = eventStartTime;}

    public Location getLocation() {return this.location;}
    public void setLocation(Location location) {this.location = location;}

    public Date getReminderTime() {return this.reminderTime;}
    public void setReminderTime(Date reminderTime) {this.reminderTime = reminderTime;}

    public ArrayList<User> getParticipants() {return this.participants;}
    public void setParticipants(ArrayList<User> participants) {this.participants = participants;}

    public String getPriority() {return this.priority;}
    public void setPriority(String priority) {this.priority = priority;}
}