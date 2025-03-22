package org.com.mplayer;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.com.mplayer.users.domain.core.entity.User;
import org.com.mplayer.users.domain.core.entity.UserRole;
import org.com.mplayer.users.domain.ports.out.data.UserDataProviderPort;
import org.com.mplayer.users.domain.ports.out.data.UserRoleDataProviderPort;
import org.com.mplayer.users.domain.ports.out.utils.AuthUtilsPort;
import org.com.mplayer.users.infra.adapters.config.UserDetailsAdapter;
import org.com.mplayer.users.infra.exception.InvalidAuthorizationTokenException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class MPlayerWebSecurityRequestFilterConfiguration extends OncePerRequestFilter {
    private final UserDataProviderPort userDataProviderPort;
    private final UserRoleDataProviderPort userRoleDataProviderPort;
    private final AuthUtilsPort authUtilsPort;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(request);

            String tokenSubject = authUtilsPort.verify(token, "sub");

            if (tokenSubject == null) {
                response.sendError(401, "Error while getting token subject!");
                return;
            }

            Optional<User> user = userDataProviderPort.findById(Long.valueOf(tokenSubject));

            if (user.isEmpty()) {
                response.sendError(401, "User not found!");
                return;
            }

            List<UserRole> userRoles = userRoleDataProviderPort.findByUser(user.get().getId());

            if (userRoles.isEmpty()) {
                response.sendError(401, "User roles not found!");
                return;
            }

            user.get().setUserRoles(userRoles);

            UserDetails userDetails = new UserDetailsAdapter(user.get());

            SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities())
            );

            filterChain.doFilter(request, response);
        } catch (InvalidAuthorizationTokenException e) {
            response.sendError(401, "Invalid token or token expired!");
        } catch (Exception e) {
            response.sendError(500, e.getMessage());
        }
    }

    private String getToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null) {
            throw new InvalidAuthorizationTokenException("Authorization token missing!");
        }

        return bearerToken.replace("Bearer ", "");
    }

    @Override
    public boolean shouldNotFilter(HttpServletRequest request) {
        List<String> publicRoutesDeclarations = new ArrayList<>();

        publicRoutesDeclarations = Stream.of(RoutesConfig.Public.class.getDeclaredFields()).map(field -> {
            try {
                return (String) field.get(field.getName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toList();

        if (publicRoutesDeclarations.isEmpty()) {
            return false;
        }

        return publicRoutesDeclarations.contains(request.getRequestURI());
    }
}
