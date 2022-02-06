/**
 * @author neko941
 * Created on: 2021-12-13
 *
 * This class provides function to send an email
 */

package Controllers;

import Models.Event;
import Models.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import static Controllers.ConfigController.getDataFromConfig;
import static Controllers.Debugging.printNotificationInConsole;

public class EmailUtils {
    static String systemEmail = getDataFromConfig("systemEmail", "email");
    static String systemEmailPassword = getDataFromConfig("systemEmail", "password");
    static Session session = getSession();

    /**
     * Get the working session of the mail API
     *
     * @return session
     */
    public static Session getSession() {
        // your host email smtp server details
        Properties pr = new Properties();
        pr.setProperty("mail.smtp.host", "smtp.gmail.com");
        pr.setProperty("mail.smtp.port", "587");
        pr.setProperty("mail.smtp.auth", "true");
        pr.setProperty("mail.smtp.starttls.enable", "true");
        pr.put("mail.smtp.socketFactory.port", "587");
        pr.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        //get session to authenticate the host email address and password
        session = Session.getInstance(pr, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(systemEmail, systemEmailPassword);
                    }
                }
        );
        return session;
    }

    /**
     * @param email program will send verification code to this email
     * @param code  the verification code
     */
    public static void verificationEmail(String email, String code) {
        try {
            //set email message details
            Message mess = new MimeMessage(session);

            //set from email address
            mess.setFrom(new InternetAddress(systemEmail));

            //set to email address or destination email address
            mess.setRecipient(Message.RecipientType.TO, new InternetAddress(email));

            //set email subject
            mess.setSubject("VERIFICATION CODE");

            //set email content
            String mailText = "<h3><strong> Your Code: </strong></h3>";
            String codeBold = "<h1><strong>" + code + "</strong></h1>";
            mess.setContent(mailText + codeBold, "text/html; charset=utf-8");

            // set send day
            mess.setSentDate(new Date());

            //set message text
            mess.saveChanges();

            //send the message
            Transport.send(mess);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        printNotificationInConsole(String.format("Verification code has been sent to email \"%s\"", email));
    }

    public static void eventEmail(int option, String email, Event event) {
        try {
            //set email message details
            Message mess = new MimeMessage(session);

            //set from email address
            mess.setFrom(new InternetAddress(systemEmail));

            //set to email address or destination email address
            mess.setRecipient(Message.RecipientType.TO, new InternetAddress(email));

            //set email subject
            switch (option) {
                case 0 -> mess.setSubject("EVENT REMINDER");
                case 1 -> mess.setSubject("EVENT CREATED");
                case 2 -> mess.setSubject("EVENT UPDATED");
                case 3 -> mess.setSubject("EVENT DELETED");
            }

            //set email content
            String eventName =      "<br><strong> Event Name     : </strong>" + event.getEventName() + "</br>";
            String eventStartTime = "<br><strong> Event Starts   : </strong>%s</br>".formatted(LocalDateTime.of(event.getDate(), event.getTime()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            String eventEndTime =   "<br><strong> Event Ends     : </strong>%s</br>".formatted(event.getReminder().getReminderTime(LocalDateTime.of(event.getDate(), event.getTime())).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            String eventLocation =  "<br><strong> Event Location : </strong>%s</br>".formatted(event.getLocation().toString());
            mess.setContent(eventName + eventStartTime + eventEndTime + eventLocation, "text/html; charset=utf-8");

            // set send day
            mess.setSentDate(new Date());

            //set message text
            mess.saveChanges();

            //send the message
            Transport.send(mess);
        } catch (Exception e) {
            e.printStackTrace();
        }
        printNotificationInConsole(String.format("Reminder of event \"%s\" \"%s\"",event.getEventName(), email));
    }

    public static void eventEmail(int option, ArrayList<User> users, Event event)
    {
        for (User user : users)
        {
            eventEmail(option, user.getEmail(), event);
        }
    }

    public static void eventEmail(int option, String[] emails, Event event)
    {
        for (String email : emails)
        {
            eventEmail(option, email, event);
        }
    }
}