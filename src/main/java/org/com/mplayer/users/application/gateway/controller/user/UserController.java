package org.com.mplayer.users.application.gateway.controller.user;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.com.mplayer.users.application.dto.user.RegisterUserDTO;
import org.com.mplayer.users.domain.core.usecase.user.registerUser.RegisterUserUsecase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.com.mplayer.MplayerApplication.API_PREFIX;

@RestController
@AllArgsConstructor
@RequestMapping(API_PREFIX + "/user")
public class UserController {
    private final RegisterUserUsecase registerUserInputPort;

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterUserDTO dto) {
        registerUserInputPort.execute(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
