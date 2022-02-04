/**
 * Author: Matheus
 * Created on: 2022-01-02
 *
 * This class is used to
 */

package Controllers;

import Models.*;

import java.time.*;
import java.io.File;
import java.util.ArrayList;

import static Controllers.EmailUtils.eventEmail;
import static ExternalConnections.DBUtilities.*;


public class EventController {
    public static Event CreateEvent(Event selectedEvent){
        int eventId = insertNewEvent(selectedEvent);
        selectedEvent.setEventID(eventId);
        ArrayList<User> participants = selectedEvent.getParticipants();
        for (int i = 0; i < participants.size(); i++){
            createUser_EventBridge(participants.get(i).getId(), selectedEvent.getEventID());
        }
        System.out.println("Event " + selectedEvent.getEventName() + " created.");
        //TODO: Add Email function
        for(String email : selectedEvent.getEmails())
        {
            eventEmail(1, email, selectedEvent);
        }
        return selectedEvent;
    }
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
        selectedEvent.setLocation(location);
        selectedEvent.setReminder(reminder);
        //TODO: remove this
        selectedEvent.setEmails(emails);
        selectedEvent.setPriority(priority);

        //TODO: Add Email function
        for(String email : selectedEvent.getEmails())
        {
            eventEmail(2, email, selectedEvent);
        }
        editEvent(selectedEvent);
        return selectedEvent;
    }

    public static int DeleteEvent(Event selectedEvent){
        int id = selectedEvent.getEventID();

        //TODO: Add Email function
        for(String email : selectedEvent.getEmails())
        {
            eventEmail(3, email, selectedEvent);
        }

        ArrayList<User> participants = selectedEvent.getParticipants();
        for (int i = 0; i < participants.size(); i++){
            deleteUser_EventBridge(participants.get(i).getId(), selectedEvent.getEventID());
        }
        deleteEvent(id);
        selectedEvent = null;

        System.gc();
        System.out.println("Event number " + id + " successfully deleted.");

        return 0;
    }

}
