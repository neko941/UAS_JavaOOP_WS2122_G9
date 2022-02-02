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

import static ExternalConnections.DBUtilities.*;


public class EventController {
    public static Event CreateEvent(Event selectedEvent){
        int eventId = insertNewEvent(selectedEvent);
        selectedEvent.setEventID(eventId);
        ArrayList<User> participants = selectedEvent.getParticipants();
        System.out.println(participants.toString());
        System.out.println(participants.get(0).getUsername());
        System.out.println(participants.get(1).getUsername());
        for (int i = 0; i < participants.size(); i++){
            createUser_EventBridge(participants.get(i).getId(), selectedEvent.getEventID());
        }
        System.out.println("Event " + selectedEvent.getEventName() + " created.");
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


        editEvent(selectedEvent);
        return selectedEvent;
    }

    public static int DeleteEvent(Event selectedEvent){
        int id = selectedEvent.getEventID();
        deleteEvent(id);
        selectedEvent = null;
        System.gc();
        System.out.println("Event number " + id + " successfully deleted.");

        return 0;
    }

}
