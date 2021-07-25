package ru.yauroff.awsfileloader.rest.dto;

import lombok.Data;
import ru.yauroff.awsfileloader.model.Role;
import ru.yauroff.awsfileloader.model.Status;

@Data
public class UserRequestDTO {
    private Long id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;
    private Status status;
}
