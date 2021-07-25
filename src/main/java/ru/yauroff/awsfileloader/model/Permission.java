package ru.yauroff.awsfileloader.model;

public enum Permission {
    USER_READ("users:read"),
    USER_WRITE("users:write"),
    FILE_READ("files:read"),
    FILE_WRITE("files:write"),
    EVENT_READ("events:read"),
    EVENT_WRITE("events:write");

    private String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
