package org.com.mplayer.users.domain.core.usecase.common.constants;

public class EmailConstants {
    public static class ForgotPassword {
        public static final String FORGOT_PASSWORD_EMAIL_TITLE = "New password requested - mPlayer";
        public static final String FORGOT_PASSWORD_EMAIL_TEMPLATE = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>Password Reset</title></head><body><div><p>Hello {0}!</p><p>You have requested a password reset for your account.</p><p>Your new password is: <strong>{1}</strong><p>For more information about your account, please visit your <a href=\"#\">account management page</a>.</p><p>Best regards, mPlayer.</p></div></body></html>";
    }
}
