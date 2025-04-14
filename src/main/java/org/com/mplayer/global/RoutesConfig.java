package org.com.mplayer.global;

public class RoutesConfig {
    public static class Public {
        public final static String LOGIN = "/api/v1/user/login";
        public final static String REGISTER = "/api/v1/user/register";
        public final static String FORGOT_PASSWORD = "/api/v1/user/forgot-password";
        public final static String HEALTH_CHECK = "/api/v1/user/health";
    }
}
