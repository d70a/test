package ru.nsk.test.cabinet.repository;

import org.springframework.data.repository.CrudRepository;
import ru.nsk.test.cabinet.pojo.IdCard;

/**
 * Spring data driven CRUD repository for id card.
 * 
 * @author me
 */
public interface IdCardRepository extends CrudRepository<IdCard, Long> {
    public IdCard findBySerialAndNumber(Long serial, Long number);
}
