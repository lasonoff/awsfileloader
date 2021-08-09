package ru.yauroff.awsfileloader.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.yauroff.awsfileloader.model.Role;
import ru.yauroff.awsfileloader.model.Status;
import ru.yauroff.awsfileloader.model.User;

import java.util.Date;

@AllArgsConstructor
@Setter
@Getter
public class UserResponseDTO {
    private String login;
    private String firstName;
    private String lastName;
    private Role role;
    private Status status;
    private Date created;

    public static UserResponseDTO fromUser(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO(user.getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getStatus(),
                user.getCreated());
        return userResponseDTO;
    }
}
