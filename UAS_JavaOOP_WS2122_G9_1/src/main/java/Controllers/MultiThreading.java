/**
 * Author: neko941
 * Created on: 2021-12-27
 *
 * This class is used to send event reminder on background
 */


package Controllers;

public class MultiThreading implements Runnable{
    private Thread t;
    private String threadName;

    public MultiThreading(String name) {
        threadName = name;
        System.out.println("Creating " +  threadName );
    }

    public void run() {
        System.out.println("Running " +  threadName );

        try {
            while(true) {
                // sort the database

                // Send email

                // Let the thread sleep for 30 seconds
                Thread.sleep(1000 * 30);

            }
        } catch (InterruptedException e) {
            System.out.println("Thread " +  threadName + " interrupted.");
        }
    }

    public void start () {
        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }
}