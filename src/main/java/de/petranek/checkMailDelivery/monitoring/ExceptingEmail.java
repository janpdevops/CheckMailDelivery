package de.petranek.checkMailDelivery.monitoring;

import java.time.Duration;
import java.time.ZonedDateTime;

public class ExceptingEmail {



    private String mailHeader;



    private ZonedDateTime sendTime;



    private ZonedDateTime receiveTime;


    public ExceptingEmail(String mailHeader, ZonedDateTime sendTime) {
        this.mailHeader = mailHeader;
        this.sendTime = sendTime;
    }

    public String getMailHeader() {
        return mailHeader;
    }

    public ZonedDateTime getSendTime() {
        return sendTime;
    }

    public ZonedDateTime getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(ZonedDateTime receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getDuration () {
        if (getReceiveTime() != null) {
            Duration duration =  Duration.between( getSendTime() , getReceiveTime() );
            return duration.toString();
        }
        return "-";
    }

    @Override
    public String toString() {
        return "ExceptingEmail{" +
                "mailHeader='" + mailHeader + '\'' +
                ", sendTime=" + sendTime +
                ", receiveTime=" + receiveTime +
                ", duration=" + getDuration() +
                '}';
    }
}
