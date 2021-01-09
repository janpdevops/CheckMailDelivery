package de.petranek.checkMailDelivery.mail;

import de.petranek.checkMailDelivery.monitoring.ExceptingEmail;
import de.petranek.checkMailDelivery.utils.PropertyResolver;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class MailHandler {

    private final PropertyResolver propertyResolver;

    public MailHandler(PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
    }

    public ExceptingEmail sendTestEMail() throws Exception {
        String destmailid = propertyResolver.resolveProperty("mbxcheck.receiver");
        String subject = propertyResolver.resolveProperty("mbxcheck.subject.prefix");
        ZonedDateTime now =  ZonedDateTime.now();
        subject += now.toString();

        ExceptingEmail exceptingEmail = new ExceptingEmail(subject, now);
        String content = propertyResolver.resolveProperty("mbxcheck.message.content");
        sendEMail(destmailid, content , subject);
        return exceptingEmail;
    }

    public void sendAlertEMail(String content, String subject) throws Exception {
        String destmailid = propertyResolver.resolveProperty("mbxcheck.alert.receiver");
        ZonedDateTime now =  ZonedDateTime.now();
        content += "\nSent on " +  now.toString();
        sendEMail(destmailid, content , subject);

    }

    public void sendEMail(String destmailid, String content, String subject) throws Exception{
//Declare recipient's & sender's e-mail id.

        String sendrmailid = propertyResolver.resolveProperty("mbxcheck.mail.user");
        //Mention user name and password as per your configuration
        final String uname = propertyResolver.resolveProperty("mbxcheck.mail.user");
        final String pwd = propertyResolver.resolveProperty("mbxcheck.mail.pwd");


        //Create a Session object & authenticate uid and pwd
        Session sessionobj = Session.getInstance(propertyResolver.getProperties(),
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


            messageobj.setSubject(subject);
            messageobj.setText(content);
            //Now send the message
            Transport.send(messageobj);

            System.out.println("Your email sent successfully...." + subject);

        } catch (MessagingException exp) {
            throw new Exception(exp);
        }
    }


    public List<String> getEmail() throws Exception{

        List<String> received = new ArrayList<>();
        try {
            String hostval = propertyResolver.resolveProperty("mbxcheck.pop3.host");
            final String uname = propertyResolver.resolveProperty("mbxcheck.mail.user");
            final String pwd = propertyResolver.resolveProperty("mbxcheck.mail.pwd");
            //Set property values

            Session emailSessionObj = Session.getDefaultInstance(propertyResolver.getProperties());
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
            emailFolderObj.close(Boolean.valueOf(propertyResolver.resolveProperty("mbxcheck.mail.expunge")));
            storeObj.close();

            return received;
        } catch (Exception exp) {
            exp.printStackTrace();
            throw exp;
        }

    }



}
