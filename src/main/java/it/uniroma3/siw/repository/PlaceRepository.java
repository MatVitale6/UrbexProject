package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Place;

public interface PlaceRepository extends CrudRepository<Place, Long> {
	
	public List<Place> findByRegion (String region);

	public boolean existsByAddressAndName(String address, String name);

    public Object findByAddress(String address);
}
