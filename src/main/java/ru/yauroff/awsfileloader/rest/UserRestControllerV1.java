package ru.yauroff.awsfileloader.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yauroff.awsfileloader.model.User;
import ru.yauroff.awsfileloader.rest.dto.UserRequestDTO;
import ru.yauroff.awsfileloader.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestControllerV1 {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userService.getAll();
        if (userList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<User> createUser(@RequestBody @Validated UserRequestDTO userDTO) {
        HttpHeaders headers = new HttpHeaders();
        if (userDTO == null || userDTO.getLogin() == null || userDTO.getPassword() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = convertFromRequestDTO(userDTO);
        this.userService.create(user);
        return new ResponseEntity<>(user, headers, HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<User> updateUser(@RequestBody @Validated UserRequestDTO userDTO) {
        HttpHeaders headers = new HttpHeaders();
        if (userDTO == null || userDTO.getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.getById(userDTO.getId());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        updateFromRequestDTO(user, userDTO);
        user = this.userService.update(user);
        return new ResponseEntity<>(user, headers, HttpStatus.OK);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<User> getUser(@PathVariable("id") Long userId) {
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.getById(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long userId) {
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.getById(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.deleteById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @RequestMapping(value = "/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<Long> getCountUsers() {
        Long count = this.userService.getCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    private User convertFromRequestDTO(UserRequestDTO userRequestDTO) {
        User user = new User(userRequestDTO.getId(), userRequestDTO.getLogin(),
                passwordEncoder.encode(userRequestDTO.getPassword()),
                userRequestDTO.getFirstName(), userRequestDTO.getLastName(), userRequestDTO.getRole(),
                userRequestDTO.getStatus());
        return user;
    }

    private void updateFromRequestDTO(User user, UserRequestDTO userRequestDTO) {
        if (userRequestDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }
        if (userRequestDTO.getFirstName() != null) {
            user.setFirstName(userRequestDTO.getFirstName());
        }
        if (userRequestDTO.getLastName() != null) {
            user.setLastName(userRequestDTO.getLastName());
        }
        if (userRequestDTO.getRole() != null) {
            user.setRole(userRequestDTO.getRole());
        }
        if (userRequestDTO.getStatus() != null) {
            user.setStatus(userRequestDTO.getStatus());
        }
    }
}
