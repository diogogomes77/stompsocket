package dev.dgomes.backend.ws.model;

import java.security.Principal;
import java.util.Date;

public class WsUser implements Principal {

    private String name;
    private Date lastConnection;

    public WsUser(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(Date lastConnection) {
        this.lastConnection = lastConnection;
    }
}
