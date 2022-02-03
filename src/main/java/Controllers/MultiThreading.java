/**
 * @author  neko941
 * Created on: 2021-12-27
 *
 * This class is used to send event reminder on background
 */


package Controllers;

import static Controllers.Debugging.printNotificationInConsole;

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

                // Send email

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