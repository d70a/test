package ru.nsk.test.cabinet.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import ru.nsk.test.cabinet.utils.JsonDateSerializer;

/**
 * Id card entity class.
 * 
 * @author me
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"user"})
@ToString(exclude = {"user"})
@Table(name = "idcard",
        uniqueConstraints = {
    @UniqueConstraint(name = "idcard_u", columnNames = {"serial", "number"})})
public class IdCard implements Serializable {

    @GenericGenerator(name = "generator", strategy = "foreign",
            parameters =
            @Parameter(name = "property", value = "user"))
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    @JsonIgnore
    Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    @JsonIgnore
    public User user;
    
    @NotNull(message = "Please provide id card serial")
    Long serial;
    
    @NotNull(message = "Please provide id card number")
    Long number;
    
    @NotNull(message = "Please provide birth date")
    @Temporal(javax.persistence.TemporalType.DATE)
    @JsonSerialize(using = JsonDateSerializer.class)
    Date birthDate;
}
