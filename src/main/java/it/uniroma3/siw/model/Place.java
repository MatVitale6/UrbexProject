package it.uniroma3.siw.model;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table
public class Place {
	//nome, posizione, descrizione, accessibilità
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String name;
	
	private String description;
	
	private Double longitude;
	private Double latitude;
	
	@NotBlank
	private String region;
	@NotBlank
	private String address;

	@OneToOne
	private Photo thumbnail;
	
	@OneToMany
	private Set<Photo> photos;
	@OneToMany
	private List<Review> reviews;
	@ManyToMany
	private Set<Photographer> photographers;


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLatitude() {return latitude;}

	public void setLatitude(Double latitude) {this.latitude = latitude;}

	public Double getLongitude() {return longitude;}

	public void setLongitude(Double longitude) {this.longitude = longitude;}


	public Photo getThumbnail() {return thumbnail;}

	public void setThumbnail(Photo photo) {this.thumbnail=photo;}
	public Set<Photo> getPhotos() {
		return photos;
	}
	public void setPhotos(Set<Photo> photos) {
		this.photos = photos;
	}
	public List<Review> getReviews() {
		return reviews;
	}
	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	public Set<Photographer> getPhotographers() {
		return photographers;
	}
	public void setPhotographers(Set<Photographer> photographers) {
		this.photographers = photographers;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, name);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Place other = (Place) obj;
		return Objects.equals(address, other.address) && Objects.equals(name, other.name);
	}
}
