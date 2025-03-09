package org.com.mplayer.users.application.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDTO {
    @NotEmpty(message = "email can't be empty.")
    @NotNull(message = "email can't be null.")
    private String email;

    @NotEmpty(message = "firstName can't be empty.")
    @NotNull(message = "firstName can't be null.")
    private String firstName;

    @NotEmpty(message = "lastName can't be empty.")
    @NotNull(message = "lastName can't be null.")
    private String lastName;

    @NotEmpty(message = "password can't be empty.")
    @NotNull(message = "password can't be null.")
    private String password;
}
