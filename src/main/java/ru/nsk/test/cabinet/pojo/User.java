package ru.nsk.test.cabinet.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * User entity class.
 * 
 * @author me
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false, of = {"email"})
@ToString(exclude = {"password"})
@Table(name = "user",
        uniqueConstraints = {
    @UniqueConstraint(name = "codephone_u", columnNames = {"code", "phone"})})
public class User extends AbstractEntity implements Serializable {

    @Transient
    private static final String SALT = "cewuiqwzie";
    
    @NotNull(message = "E-mail is mandatory field because used as user login")
    @Column(name = "email", unique = true)
    String email;
    
    @JsonIgnore
    @NotNull(message = "Password is mandatory field")
    String password;
    
    @NotNull(message = "Please provide first name")
    String nameFirst;
    
    String nameMiddle;
    @NotNull(message = "Please provide last name")
            
    String nameLast;
    @NotNull(message = "Please provide phone code")
            
    String code;
    @NotNull(message = "Please provide phone number")            
    String phone;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)            
    IdCard idCard;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Place place;

    public User() {
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public User(String email, String password) {
        this.email = email;
        this.password = encode(password);

        IdCard localIdCard = new IdCard();
        this.setIdCard(localIdCard);
        idCard.setUser(this);

        Place localPlace = new Place();
        this.setPlace(localPlace);
        place.setUser(this);
    }

    public void updatePassword(String old, String newPass1, String newPass2) {
        if (!password.equals(encode(old))) {
            throw new IllegalArgumentException("Existing Password invalid");
        }
        if (!newPass1.equals(newPass2)) {
            throw new IllegalArgumentException("New Passwords don't match");
        }
        this.password = encode(newPass1);
    }

    // TODO implement crypt functions by hardware token
    private String encode(String password) {
        return new Md5PasswordEncoder().encodePassword(password, SALT);
    }
}
