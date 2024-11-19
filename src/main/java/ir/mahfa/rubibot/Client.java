package ir.mahfa.rubibot;

import ir.mahfa.rubibot.dto.SessionData;
import org.apache.commons.lang3.StringUtils;

public class Client {
    private final Methods methods;
    private SessionData sessionData;
    private Sessions sessions;
    private final String session;
    final String platform;
    private final int apiVersion;
    final String proxy;
    final int timeout;


    public Client(String session,
                  String auth,
                  String _private,
                  String platform,
                  int apiVersion,
                  String proxy,
                  int timeout,
                  boolean show_progress_bar) {
        this.session = session;
        this.platform = platform;
        this.apiVersion = apiVersion;
        this.proxy = proxy;
        this.timeout = timeout;
        if (StringUtils.isNotEmpty(session)) {
            this.sessions = new Sessions(this);
            if (this.sessions.checkSessionExists()) {
                this.sessionData = sessions.loadSessionData();
            } else {
                this.sessionData = sessions.createSession();
            }
        } else {
            this.sessionData = SessionData.builder()
                    .auth(auth)
                    .private_key(Utils.privateParse(_private))
                    .build();
        }
        this.methods = new Methods(
                sessionData,
                platform,
                apiVersion,
                proxy,
                timeout,
                show_progress_bar,
                platform1, apiVersion1);
    }
}
