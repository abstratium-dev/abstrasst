package dev.abstratium.abstrasst.service;

import java.util.UUID;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class SessionId {

    private String id = UUID.randomUUID().toString();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}