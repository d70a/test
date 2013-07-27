package ru.nsk.test.cabinet.repository;

import org.springframework.data.repository.CrudRepository;
import ru.nsk.test.cabinet.pojo.Place;

/**
 * Spring data driven CRUD repository for place.
 * 
 * @author me
 */
public interface PlaceRepository extends CrudRepository<Place, Long>{

}
