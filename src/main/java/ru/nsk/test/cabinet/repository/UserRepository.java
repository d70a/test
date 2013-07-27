package ru.nsk.test.cabinet.repository;

import ru.nsk.test.cabinet.utils.SecurityUserDetailsService;
import org.springframework.data.repository.CrudRepository;
import ru.nsk.test.cabinet.pojo.User;

/**
 *
 * @author me
 */
public interface UserRepository extends CrudRepository<User, Long>,
        SecurityUserDetailsService{

    User findByEmail(String name);
    
    User findByCodeAndPhone(String code, String phone);
}
