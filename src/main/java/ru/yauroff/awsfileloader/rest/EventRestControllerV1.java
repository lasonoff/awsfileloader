package ru.yauroff.awsfileloader.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.yauroff.awsfileloader.dto.EventResponseDTO;
import ru.yauroff.awsfileloader.model.Event;
import ru.yauroff.awsfileloader.service.EventService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/events")
public class EventRestControllerV1 {

    @Autowired
    private EventService eventService;

    @GetMapping
    @PreAuthorize("hasAuthority('events:read')")
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        List<Event> eventList = eventService.getAll();
        if (eventList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<EventResponseDTO> eventResponseDTOList = eventList.stream()
                                                               .map(event -> EventResponseDTO.formEvent(event))
                                                               .collect(Collectors.toList());
        return new ResponseEntity<>(eventResponseDTOList, HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('events:write')")
    public ResponseEntity deleteAllEvents() {
        eventService.deleteAll();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('events:read')")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Event event = eventService.getById(id);
        if (event == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        EventResponseDTO eventResponseDTO = EventResponseDTO.formEvent(event);
        return new ResponseEntity<>(eventResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('events:write')")
    public ResponseEntity<EventResponseDTO> deleteEventById(@PathVariable Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Event event = eventService.getById(id);
        if (event == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        eventService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/count")
    @PreAuthorize("hasAuthority('events:read')")
    public ResponseEntity<Long> getCounts() {
        Long count = eventService.getCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
