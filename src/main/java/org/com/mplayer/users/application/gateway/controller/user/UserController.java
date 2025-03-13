package org.com.mplayer.users.application.gateway.controller.user;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.com.mplayer.users.application.dto.user.AuthUserDTO;
import org.com.mplayer.users.application.dto.user.ForgotPasswordDTO;
import org.com.mplayer.users.application.dto.user.RegisterUserDTO;
import org.com.mplayer.users.domain.ports.in.usecase.AuthUserUsecasePort;
import org.com.mplayer.users.domain.ports.in.usecase.ForgotPasswordUsecasePort;
import org.com.mplayer.users.domain.ports.in.usecase.RegisterUserUsecasePort;
import org.com.mplayer.users.domain.ports.out.usecase.AuthUserOutPort;
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
    private final RegisterUserUsecasePort registerUserInputPort;
    private final ForgotPasswordUsecasePort forgotPasswordInputPort;
    private final AuthUserUsecasePort authUserUsecasePort;

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterUserDTO dto) {
        registerUserInputPort.execute(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordDTO dto) {
        forgotPasswordInputPort.execute(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthUserOutPort> auth(@RequestBody @Valid AuthUserDTO dto) {
        AuthUserOutPort output = authUserUsecasePort.execute(dto);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}
