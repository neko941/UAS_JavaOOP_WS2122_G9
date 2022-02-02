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
    public static Event CreateEvent(Event myEvent){
        int eventId = insertNewEvent(myEvent);
        myEvent.setEventID(eventId);
        for (int i = 0; i < myEvent.getParticipants().size(); i++){
            createUser_EventBridge(myEvent.getParticipants().get(i).getId(), myEvent.getEventID());
        }
        System.out.println("Event " + myEvent.getEventName() + " created.");
        return myEvent;
    }
    public static Event ChangeEvent(Event selectedEvent,
                             String eventName,
                             LocalDate date,
                             LocalTime time,
                             int duration,
                             ArrayList<User> participants,
                             ArrayList<File> attachments,
                             Location location,
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

    public static boolean CheckForChange(String oldString, String newString){
        if (oldString != newString){
            return true;
        }
        return false;
    }

    public static boolean CheckForChange(int oldInt, int newInt){
        if (oldInt != newInt){
            return true;
        }
        return false;
    }

    public static boolean CheckForChange(LocalDate oldDate, LocalDate newDate){
        if (oldDate != newDate){
            return true;
        }
        return false;
    }

    public static boolean CheckForChange(LocalTime oldTime, LocalTime newTime){
        if (oldTime != newTime){
            return true;
        }
        return false;
    }

    public static boolean CheckForChange(ArrayList<?> oldArray, ArrayList<?> newArray){
        if (oldArray != newArray){
            return true;
        }
        return false;
    }

    public static boolean CheckForChange(Location oldLocation, Location newLocation){
        if (oldLocation != newLocation){
            return true;
        }
        return false;
    }

    public static boolean CheckForChange(Reminder oldReminder, Reminder newReminder){
        if (oldReminder != newReminder){
            return true;
        }
        return false;
    }

    public static boolean CheckForChange(Priority oldPriority, Priority newPriority){
        if (oldPriority != newPriority){
            return true;
        }
        return false;
    }
}
