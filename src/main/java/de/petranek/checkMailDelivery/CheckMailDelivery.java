package de.petranek.checkMailDelivery;

import de.petranek.checkMailDelivery.mail.MailHandler;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

public class CheckMailDelivery {

    public static void main (String [] args)  {

        try {
            CheckMailDelivery checkMailDelivery = new CheckMailDelivery();
            checkMailDelivery.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void start() throws Exception {
        init();
        File f = new File ("etc/mail.properties");

        FileReader fileReader = new FileReader(f);
        Properties properties = new Properties();
        properties.load(fileReader);

        MailHandler mailHandler = new MailHandler(properties);
        mailHandler.sendEMail();

        Thread.sleep(30*1000);
        mailHandler.getEmail();
    }





    private void init() {
    }
}
