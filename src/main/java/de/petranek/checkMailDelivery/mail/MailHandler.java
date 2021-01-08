package de.petranek.checkMailDelivery.mail;

import de.petranek.checkMailDelivery.monitoring.ExceptingEmail;
import org.apache.commons.lang3.StringUtils;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class MailHandler {

    private final Properties properties;

    public MailHandler(Properties properties) {
        this.properties = properties;
    }


    public ExceptingEmail sendEMail() throws Exception{
//Declare recipient's & sender's e-mail id.
        String destmailid = resolveProperty("mbxcheck.receiver");
        String sendrmailid = resolveProperty("mbxcheck.mail.user");
        //Mention user name and password as per your configuration
        final String uname = resolveProperty("mbxcheck.mail.user");
        final String pwd = resolveProperty("mbxcheck.mail.pwd");


        //Create a Session object & authenticate uid and pwd
        Session sessionobj = Session.getInstance(this.properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(uname, pwd);
                    }
                });

        try {
            //Create MimeMessage object & set values
            Message messageobj = new MimeMessage(sessionobj);
            messageobj.setFrom(new InternetAddress(sendrmailid));
            messageobj.setRecipients(Message.RecipientType.TO,InternetAddress.parse(destmailid));
            String subject = resolveProperty("mbxcheck.subject.prefix");
            ZonedDateTime now =  ZonedDateTime.now();
            subject += now.toString();

            messageobj.setSubject(subject);
            messageobj.setText(resolveProperty("mbxcheck.message.content"));
            //Now send the message
            Transport.send(messageobj);
            ExceptingEmail exceptingEmail = new ExceptingEmail(subject, now);
            System.out.println("Your email sent successfully...." + subject);
            return exceptingEmail;
        } catch (MessagingException exp) {
            throw new Exception(exp);
        }
    }


    public List<String> getEmail() throws Exception{

        List<String> received = new ArrayList<>();
        try {
            String hostval = resolveProperty("mbxcheck.pop3.host");
            final String uname = resolveProperty("mbxcheck.mail.user");
            final String pwd = resolveProperty("mbxcheck.mail.pwd");
            //Set property values

            Session emailSessionObj = Session.getDefaultInstance(this.properties);
            //Create POP3 store object and connect with the server
            Store storeObj = emailSessionObj.getStore("pop3s");

            storeObj.connect(hostval, uname, pwd);
            //Create folder object and open it in read-only mode
            Folder emailFolderObj = storeObj.getFolder("INBOX");
            emailFolderObj.open(Folder.READ_WRITE);
            //Fetch messages from the folder and print in a loop
            Message[] messageobjs = emailFolderObj.getMessages();

            for (int i = 0, n = messageobjs.length; i < n; i++) {
                Message indvidualmsg = messageobjs[i];
                System.out.println("Printing individual messages");
                System.out.println("No# " + (i + 1));
                System.out.println("Email Subject: " + indvidualmsg.getSubject());
                System.out.println("Sender: " + indvidualmsg.getFrom()[0]);
                System.out.println("Content: " + indvidualmsg.getContent().toString());
                indvidualmsg.setFlag(Flags.Flag.DELETED, true);

                received.add(indvidualmsg.getSubject());

            }

            //Now close all the objects
            emailFolderObj.close(Boolean.valueOf(resolveProperty("mbxcheck.mail.expunge")));
            storeObj.close();

            return received;
        } catch (Exception exp) {
            exp.printStackTrace();
            throw exp;
        }

    }

    private String resolveProperty(String key) throws Exception {
        if (properties == null ) {
            throw new Exception("No properties");

        }
        String value = (String) this.properties.get(key);
        if (StringUtils.isEmpty(value))  {
            throw new Exception("No value found for " + key);
        }
        return value;
    }

}
