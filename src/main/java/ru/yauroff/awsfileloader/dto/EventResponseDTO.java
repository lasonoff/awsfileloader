package ru.yauroff.awsfileloader.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.yauroff.awsfileloader.model.ActionType;
import ru.yauroff.awsfileloader.model.Event;


@AllArgsConstructor
@Getter
@Setter
public class EventResponseDTO {
    private Long id;
    private FileResponseDTO file;
    private UserResponseDTO user;
    private ActionType actionType;

    public static EventResponseDTO formEvent(Event event) {
        EventResponseDTO eventResponseDTO = new EventResponseDTO(event.getId(),
                FileResponseDTO.fromFile(event.getFile()), UserResponseDTO.fromUser(event.getUser()),
                event.getActionType());
        return eventResponseDTO;
    }
}
