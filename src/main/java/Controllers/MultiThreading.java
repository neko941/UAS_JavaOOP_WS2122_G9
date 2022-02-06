/**
 * @author  neko941
 * Created on: 2021-12-27
 *
 * This class is used to send event reminder on background
 */


package Controllers;

import Models.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static Controllers.Debugging.printNotificationInConsole;
import static Controllers.EmailUtils.eventEmail;
import static ExternalConnections.DBUtilities.editEvent;
import static ExternalConnections.DBUtilities.fetchAllEventsWithReminderFromDatabase;

@SuppressWarnings("ALL")
public class MultiThreading implements Runnable{
    private Thread t;
    private final String threadName;

    public MultiThreading(String name) {
        threadName = name;
        printNotificationInConsole(String.format("Creating new thred \"%s\"", threadName));
    }

    public void run() {
        printNotificationInConsole(String.format("Running thread \"%s\"", threadName));

        try {
            while(true) {
                // sort the database
                ArrayList<Event> events = fetchAllEventsWithReminderFromDatabase();
                // Send email
                for(Event event : events)
                {
                    if(event.getReminderTime().isBefore(LocalDateTime.now()))
                    {
                        eventEmail(0, event.getEmails(), event);
                        event.setReminder(null);
                        editEvent(event);
                    }
                    else
                    {
                        break;
                    }
                }
                // Let the thread sleep for 30 seconds
                Thread.sleep(1000 * 30);
            }
        } catch (InterruptedException e) {
            printNotificationInConsole(String.format("Thread \"%s\" interrupted", threadName));
        }
    }

    public void start () {
        printNotificationInConsole(String.format("Starting thread \"%s\"", threadName));
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }
}