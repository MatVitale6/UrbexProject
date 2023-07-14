package it.uniroma3.siw.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Photographer;

public interface PhotographerRepository extends CrudRepository<Photographer, Long> {
    
    public boolean existsByNameAndSurname(String name, String surname);

	@Query(value="select * "
			+ "from photographer p "
			+ "where p.id not in "
			+ "(select photographers_id "
			+ "from place_photographers "
			+ "where place_photographers.starred_places_id = :placeId)", nativeQuery=true)
	public Iterable<Photographer> findPhotographersNotInPlace(@Param("placeId") Long placeId);
}
