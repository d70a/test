package ru.nsk.test.cabinet.utils;

import org.springframework.transaction.annotation.Transactional;
import ru.nsk.test.cabinet.pojo.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Interface for user registration/authorization, extends spring security.
 * 
 * @author me
 */
public interface SecurityUserDetailsService extends UserDetailsService {

    @Override
    SecurityUserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    User getUserFromSession();

    @Transactional
    User register(String name, String password);
}
