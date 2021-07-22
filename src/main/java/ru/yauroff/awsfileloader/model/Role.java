package ru.yauroff.awsfileloader.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    ADMIN(Set.of(Permission.USER_READ, Permission.USER_WRITE, Permission.FILE_READ, Permission.FILE_WRITE,
            Permission.EVENT_READ, Permission.EVENT_WRITE)),
    MODERATOR(Set.of(Permission.USER_READ, Permission.FILE_READ, Permission.FILE_WRITE, Permission.EVENT_READ)),
    USER(Set.of(Permission.FILE_READ, Permission.EVENT_READ));

    Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthority() {
        return getPermissions().stream()
                               .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                               .collect(Collectors.toSet());
    }
}
