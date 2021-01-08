package de.petranek.checkMailDelivery.monitoring;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class ExpectationStore {

    private Map<String, ExceptingEmail> expectedItems = new HashMap<>();

    public void mailHasBeenSend(ExceptingEmail sent) {
        expectedItems.put(sent.getMailHeader(), sent);
    }

    public ExceptingEmail mailReceived (String mailHeader) {
        ExceptingEmail item =  expectedItems.remove(mailHeader);
        if (item != null) {
           item.setReceiveTime(ZonedDateTime.now());
        }
        return item;
    }

    public int openItemCount(){
        return expectedItems.size();
    }
}
