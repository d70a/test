package ru.nsk.test.cabinet.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * User region (location) entity class.
 * @author me
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"user"})
@ToString(exclude = {"user"})
@Table(name = "place")
public class Place implements Serializable {

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
    
    @NotNull(message = "Please provide user region")
    String region;
    
    @NotNull(message = "Please provide user city")
    String city;
    
    @NotNull(message = "Please provide user street address")
    String address;
}
