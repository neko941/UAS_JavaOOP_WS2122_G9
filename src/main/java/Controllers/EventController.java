/**
 * @author Matheus
 * Created on: 2022-01-02
 *
 * This class is used to
 */

package Controllers;

import Models.*;

import java.time.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static Controllers.Debugging.printNotificationInConsole;
import static Controllers.EmailUtils.eventEmail;
import static ExternalConnections.DBUtilities.*;


public class EventController {
    /**
     * Takes an event Object and invokes the DB utilities for creating the necessary objects in the Database.
     *
     * @param selectedEvent: the event object to be created in the DB
     * @return Event: the event object appended with the ID which is the primary key in the Database.
     */
    public static Event CreateEvent(User creator, Event selectedEvent){
        int eventId = insertNewEvent(creator, selectedEvent);
        selectedEvent.setEventID(eventId);



        //TODO: Add Email function
        eventEmail(1, selectedEvent.getParticipants(), selectedEvent);
        printNotificationInConsole("Event " + selectedEvent.getEventName() + " created.");
        return selectedEvent;
    }

    /**
     * Takes an event Object and attributes and invokes the DB utilities functions for creating/updating the necessary objects in the Database.
     *
     * @param selectedEvent: the current event object in the Database
     * @param eventName: value to be updated in the Database
     * @param date: value to be updated in the Database
     * @param time: value to be updated in the Database
     * @param duration: value to be updated in the Database
     * @param location: value to be updated in the Database
     * @param participants: value to be updated in the Database
     * @param emails: value to be updated in the Database
     * @param attachments: value to be updated in the Database
     * @param reminder: value to be updated in the Database
     * @param priority: value to be updated in the Database
     * @return Event: the event object updated with the changes
     */
    public static Event EditEvent(Event selectedEvent,
                             String eventName,
                             LocalDate date,
                             LocalTime time,
                             int duration,
                             Location location,
                             ArrayList<User> participants,
                             String[] emails,
                             ArrayList<File> attachments,
                             Reminder reminder,
                             Priority priority) {

        selectedEvent.setEventName(eventName);
        selectedEvent.setDate(date);
        selectedEvent.setTime(time);
        selectedEvent.setDuration(duration);
        selectedEvent.setParticipants(participants);
        selectedEvent.setAttachments(attachments);
        int locationId = selectedEvent.getLocation().getLocationID();
        location.setLocationID(locationId);
        selectedEvent.setLocation(location);
        selectedEvent.setReminder(reminder);
        selectedEvent.setEmails(emails);
        selectedEvent.setPriority(priority);

        printNotificationInConsole("Event " + selectedEvent.getEventName() + " edited.");


        eventEmail(2, selectedEvent.getParticipants(), selectedEvent);

        editEvent(selectedEvent);
        return selectedEvent;
    }
    /**
     * Deletes an event object from the Database.
     *
     * @param selectedEvent: the event object to be deleted in the DB
     * @return int: 0 if the deletion was successful.
     */
    public static int DeleteEvent(User creator, Event selectedEvent){
        int id = selectedEvent.getEventID();

        //TODO: Add Email function
        eventEmail(3, selectedEvent.getParticipants(), selectedEvent);

        deleteEvent(creator, selectedEvent);
        selectedEvent = null;

        System.gc();
        printNotificationInConsole("Event number " + id + " successfully deleted.");
        return 0;
    }

}
