package ru.nsk.test.cabinet.repository;

import ru.nsk.test.cabinet.utils.SecurityUserDetails;
import ru.nsk.test.cabinet.utils.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import ru.nsk.test.cabinet.pojo.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.nsk.test.cabinet.core.response.CommonResponseCodes;
import ru.nsk.test.cabinet.core.response.CommonResponseCodesImpl;
import ru.nsk.test.cabinet.ex.AlreadyRegisteredException;
import ru.nsk.test.cabinet.ex.EmptyPasswordProvidedException;
import ru.nsk.test.cabinet.ex.SessionExpiredException;

/**
 * User security services implementation for login and registration methods.
 * Also implement user loading from session.
 *
 * @author me
 */
public class UserRepositoryImpl implements SecurityUserDetailsService {

    @Autowired
    UserRepository repository;
    @Autowired
    IdCardRepository idCardRepository;

    @Override
    public SecurityUserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        final User user = repository.findByEmail(name);
        if (user == null) {
            throw new UsernameNotFoundException(
                    "Login(e-mail) not registered: " + name);
        }
        return new SecurityUserDetails(user);

    }

    /**
     * Method load database user object by session identification.
     *
     * @return Always return authorized (@see User) object. If user not
     * authorized or session expired, method throw 
     * (@see SessionExpiredException).
     */
    @Override
    public User getUserFromSession() {
        User result = null;
        try {
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();
            Object principal = authentication.getPrincipal();
            if (principal instanceof SecurityUserDetails) {
                SecurityUserDetails userDetails = (SecurityUserDetails) principal;
                result = userDetails.getUser();
            }
            return result;
        } finally {
            if (result == null) {
                throw new SessionExpiredException();
            }
        }
    }

    /**
     * Method register new user in server database. Method check provided
     * user login (aka e-mail address) for unique value, if given e-mail already
     * registered in database then (@see AlreadyRegisteredException) will be
     * thrown by code. Newly registered user added to current session.
     * 
     * @param name User e-mail address.
     * @param password User password.
     * @return Valid registered user object or (@see AlreadyRegisteredException)
     * if this e-mail already found in database.
     */
    @Override
    public User register(String name, String password) {
        User found = repository.findByEmail(name);
        if (found != null) {
            throw new AlreadyRegisteredException(
                    new CommonResponseCodes[]{
                CommonResponseCodesImpl.ALREADY_REGISTERED_EMAIL});
        }
        if (password == null || password.isEmpty()) {
            throw new EmptyPasswordProvidedException();
        }
        User user = new User(name, password);

        user = repository.save(user);

        setUserInSession(user);
        
        return user;
    }

    private void setUserInSession(User user) {
        SecurityContext context = SecurityContextHolder.getContext();
        SecurityUserDetails userDetails = new SecurityUserDetails(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), userDetails.getAuthorities());
        context.setAuthentication(authentication);
    }
}
