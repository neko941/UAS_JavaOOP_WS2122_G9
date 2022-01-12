/**
 * Author: neko941
 * Created on: 2021-12-13
 *
 * This class provides model for an email
 */

package Models;

public class EmailContent
{
    private String subject;
    private String content;
    private User user;
    private String antiPhishing;

    public EmailContent(String subject, String content, User user)
    {
        this.subject = subject;
        this.content = content;
        this.antiPhishing = user.getAntiPhishing();
    }

    // setters and getters
    public String getSubject(){return subject;}
    public void setSubject(String subject){this.subject = subject;}

    public String getContent(){return content;}
    public void setContent(String content){this.content = content;}

    public String getAntiPhishing() {return antiPhishing;}
    public void setAntiPhishing(String antiPhishing) {this.antiPhishing = antiPhishing;}
}