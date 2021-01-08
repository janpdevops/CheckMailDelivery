package de.petranek.checkMailDelivery;

import de.petranek.checkMailDelivery.mail.MailHandler;
import de.petranek.checkMailDelivery.monitoring.ExceptingEmail;
import de.petranek.checkMailDelivery.monitoring.ExpectationStore;

import java.io.File;
import java.io.FileReader;
import java.util.List;
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

        ExpectationStore expectationStore = new ExpectationStore();

        MailHandler mailHandler = new MailHandler(properties);
        ExceptingEmail sent = mailHandler.sendEMail();
        expectationStore.mailHasBeenSend(sent);
        Thread.sleep(30*1000);
        List<String> mails = mailHandler.getEmail();

        for (String header: mails) {
            ExceptingEmail match = expectationStore.mailReceived(header);
            if (match != null) {
                System.out.println ("Matching Email received " + match);

            }
        }

        if (expectationStore.openItemCount() > 0 ) {
            System.out.println("We still expect " + expectationStore.openItemCount() + " mails!");
        } else {
            System.out.println("All mails have been received.");
        }

    }





    private void init() {
    }
}
