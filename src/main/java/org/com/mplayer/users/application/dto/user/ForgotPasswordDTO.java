package org.com.mplayer.users.application.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordDTO {
    @NotNull(message = "email can't be null")
    @NotBlank(message = "email can't be blank")
    private String email;
}
