package com.ivar.jrsa.agent;

import com.ivar.jrsa.event.EventSeverity;
import com.ivar.jrsa.event.SecurityEvent;
import com.ivar.jrsa.event.SecurityEventType;
import com.ivar.jrsa.sender.SmtapClient;

public class TestSender {

    public static void main(String[] args) {

        SecurityEvent event =
                new SecurityEvent(
                        SecurityEventType.PROCESS_EXECUTION,
                        EventSeverity.HIGH,
                        "JRSA",
                        "cmd.exe execution detected");

        SmtapClient.send(event);
    }
}