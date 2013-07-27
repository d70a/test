package ru.nsk.test.cabinet.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import ru.nsk.test.cabinet.core.Identifiable;

/**
 * Class implements primary key for child entity classes.
 * @author me
 */
@MappedSuperclass
public class AbstractEntity implements Identifiable<Long> {

    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private final Long id;

    protected AbstractEntity() {
        this.id = null;
    }
    
    @Override
    public Long getId() {
        return this.id;
    }
}
