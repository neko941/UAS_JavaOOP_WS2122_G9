/**
 * Author: neko941
 * Created on: 2021-12-13
 *
 * This class provides function to send an email
 */

package Controllers;

import Models.Event;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

import static Controllers.ConfigController.getDataFromConfig;

public class EmailUtils
{
    static String systemEmail = getDataFromConfig("systemEmail", "email");
    static String systemEmailPassword = getDataFromConfig("systemEmail", "systemEmailPassword");
    static Session session;

    public static void getSession()
    {
        // your host email smtp server details
        Properties pr = new Properties();
        pr.setProperty("mail.smtp.host", "smtp.gmail.com");
        pr.setProperty("mail.smtp.port", "587");
        pr.setProperty("mail.smtp.auth", "true");
        pr.setProperty("mail.smtp.starttls.enable", "true");
        pr.put("mail.smtp.socketFactory.port", "587");
        pr.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        //get session to authenticate the host email address and password
        session = Session.getInstance(pr, new Authenticator()
                {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(systemEmail, systemEmailPassword);
                    }
                }
        );
    }
    //send email to the user email
    public static void verificationEmail(String email, String code)
    {
        try
        {
            // get session
            getSession();

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
            String codeBold = "<h1><strong>" + code +  "</strong></h1>";
            mess.setContent(mailText + codeBold, "text/html; charset=utf-8");

            // set send day
            mess.setSentDate(new Date());

            //set message text
            mess.saveChanges();

            //send the message
            Transport.send(mess);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

   public static void reminderEmail(Event event)
   {
       try
       {
           // get session
           getSession();

           //set email message details
           Message mess = new MimeMessage(session);

           //set from email address
           mess.setFrom(new InternetAddress(systemEmail));

           //set to email address or destination email address
//           mess.setRecipient(Message.RecipientType.TO, new InternetAddress(email));

           //set email subject
           mess.setSubject("SCHEDULER REMINDER");

           //set email content
//           String mailText = "<h3><strong> Your Code: </strong></h3>";
//           String codeBold = "<h1><strong>" + code +  "</strong></h1>";
//           mess.setContent(mailText + codeBold, "text/html; charset=utf-8");

           // set send day
           mess.setSentDate(new Date());

           //set message text
           mess.saveChanges();

           //send the message
           Transport.send(mess);
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }
   }

//    public static void sendEmail(User user, Event event)
//    {
//        JSONParser parser = new JSONParser();
//
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = (JSONObject) parser.parse(new FileReader("src/Backend/config.json"));
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println();
//
//        String systemEmail = (String) jsonObject.get("systemEmail");
//        String systemEmailPassword = (String) jsonObject.get("systemEmailPassword");
//
//        try
//        {
//            // your host email smtp server details
//            Properties pr = new Properties();
//            pr.setProperty("mail.smtp.host", "smtp.gmail.com");
//            pr.setProperty("mail.smtp.port", "587");
//            pr.setProperty("mail.smtp.auth", "true");
//            pr.setProperty("mail.smtp.starttls.enable", "true");
//            pr.put("mail.smtp.socketFactory.port", "587");
//            pr.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//
//            //get session to authenticate the host email address and password
//            Session session = Session.getInstance(pr, new Authenticator()
//                    {
//                        @Override
//                        protected PasswordAuthentication getPasswordAuthentication()
//                        {
//                            return new PasswordAuthentication(systemEmail, systemEmailPassword);
//                        }
//                    }
//            );
//
//            //set email message details
//            Message mess = new MimeMessage(session);
//
//            //set from email address
//            mess.setFrom(new InternetAddress(systemEmail));
//            //set to email address or destination email address
//            mess.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
//
//            //set email subject
//            String mailSubject = "THE UPCOMING EVENT";
//            String mailAntiPhishing = "<br><strong> Anti-Phishing: </font>" + "<strong>" + user.getAntiPhishing() + "</strong></br>";
//            String eventName = "<br><strong> Event: </strong>" + event.getName() + "</br>";
//            String eventStartTime = "<br><strong> Event starts: </strong>" + event.getEventStartTime() + "</br>";
//            String location = "<br><strong> Location: </strong>>" + event.getLocation() + "</br>";
//
//            mess.setSubject(mailSubject);
//            mess.setContent(mailAntiPhishing + eventName + eventStartTime + location, "text/html; charset=utf-8");
//
//            // set send day
//            mess.setSentDate(new Date());
//
//            //set message text
//            mess.saveChanges();
//
//            //send the message
//            Transport.send(mess);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//        System.out.println("EMAIL VERIFICATION IS SENT TO " + user.getUsername());
//    }
}
