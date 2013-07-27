package ru.nsk.test.cabinet.utils;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import ru.nsk.test.cabinet.pojo.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Simply implementation of spring security (@see UserDetails).
 * 
 * @author me
 */
public class SecurityUserDetails implements UserDetails {

    private final User user;

    public SecurityUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }
}
