package ir.mahfa.rubibot;

import ir.mahfa.rubibot.dto.SendCodeData;
import ir.mahfa.rubibot.dto.SessionData;

public class Sessions {
    private final Client client;

    public Sessions(Client client) {
        this.client = client;
    }

    public boolean checkSessionExists() {
        return false;
    }

    public SessionData loadSessionData() {
        return SessionData.builder().build();
    }

    public SessionData createSession() {
        Methods methods = new Methods(SessionData.builder().build(),
                client.platform,
                6,
                client.proxy,
                client.timeout,
                true,
                platform1, apiVersion1);
        while (true) {
            System.out.println("Enter the phone number » ");
            String phoneNumber = System.console().readLine();
            try {
                SendCodeData sendCodeData = methods.sendCode(phoneNumber, null, false);
            } catch (Exception e) {
                System.out.println("The phone number is invalid! Please try again.");
                continue;
            }
            break;
        }
        return SessionData.builder().build();
    }
}
