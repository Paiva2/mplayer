package org.com.mplayer.users.application.gateway.controller.user;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.com.mplayer.users.application.dto.user.AuthUserDTO;
import org.com.mplayer.users.application.dto.user.ForgotPasswordDTO;
import org.com.mplayer.users.application.dto.user.RegisterUserDTO;
import org.com.mplayer.users.domain.ports.in.usecase.AuthUserUsecasePort;
import org.com.mplayer.users.domain.ports.in.usecase.ForgotPasswordUsecasePort;
import org.com.mplayer.users.domain.ports.in.usecase.GetProfileUsecasePort;
import org.com.mplayer.users.domain.ports.in.usecase.RegisterUserUsecasePort;
import org.com.mplayer.users.domain.ports.out.usecase.AuthUserOutPort;
import org.com.mplayer.users.domain.ports.out.usecase.GetProfileOutputPort;
import org.com.mplayer.users.infra.adapters.config.UserDetailsAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.com.mplayer.MplayerApplication.API_PREFIX;

@RestController
@AllArgsConstructor
@RequestMapping(API_PREFIX + "/user")
public class UserController {
    private final RegisterUserUsecasePort registerUserInputPort;
    private final ForgotPasswordUsecasePort forgotPasswordInputPort;
    private final AuthUserUsecasePort authUserUsecasePort;
    private final GetProfileUsecasePort getProfileUsecasePort;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

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

    @GetMapping("/profile")
    public ResponseEntity<GetProfileOutputPort> auth(@AuthenticationPrincipal UserDetailsAdapter authPrincipal) {
        GetProfileOutputPort output = getProfileUsecasePort.execute(authPrincipal.getId());
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
}
