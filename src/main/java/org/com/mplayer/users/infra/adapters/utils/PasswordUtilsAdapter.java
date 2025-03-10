package org.com.mplayer.users.infra.adapters.utils;

import lombok.AllArgsConstructor;
import org.com.mplayer.users.domain.ports.out.utils.PasswordUtilsPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Random;

@Component
@AllArgsConstructor
public class PasswordUtilsAdapter implements PasswordUtilsPort {
    private final static int RANDOM_PASSWORD_LENGTH = 12;

    private final PasswordEncoder passwordEncoder;

    @Override
    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean comparePassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    @Override
    public String genRandomPassword() {
        String symbol = "&*!@%=";
        String capLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String smallLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String possibleCombinations = capLetters + smallLetters + numbers + symbol;

        char[] newRandomPassword = new char[RANDOM_PASSWORD_LENGTH];

        Random random = new Random();

        for (int i = 0; i < RANDOM_PASSWORD_LENGTH; i++) {
            newRandomPassword[i] = possibleCombinations.charAt(random.nextInt(possibleCombinations.length()));
        }

        return Arrays.toString(newRandomPassword).replace("[", "").replace("]", "").replaceAll(",", "").replaceAll(" ", "").toLowerCase();
    }
}
