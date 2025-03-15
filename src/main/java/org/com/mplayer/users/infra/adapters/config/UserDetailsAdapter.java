package org.com.mplayer.users.infra.adapters.config;

import org.com.mplayer.users.domain.core.entity.User;
import org.com.mplayer.users.domain.core.entity.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsAdapter implements UserDetails {
    public final static String ROLE_PREFIX = "ROLE_";
    private final User user;

    public UserDetailsAdapter(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (UserRole userRole : this.user.getUserRoles()) {
            String fullRole = ROLE_PREFIX.concat(userRole.getRole().getName().name());

            authorities.add(new SimpleGrantedAuthority(fullRole));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getId().toString();
    }

    public Long getId() {
        return user.getId();
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getEnabled();
    }
}
