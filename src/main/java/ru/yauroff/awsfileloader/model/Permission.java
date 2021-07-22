package ru.yauroff.awsfileloader.model;

public enum Permission {
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    FILE_READ("file:read"),
    FILE_WRITE("file:write"),
    EVENT_READ("event:read"),
    EVENT_WRITE("event:write");

    private String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
