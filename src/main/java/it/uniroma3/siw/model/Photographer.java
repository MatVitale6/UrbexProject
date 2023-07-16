package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Photographer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    private String email;

    private String phoneNumber;

    private String bio;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth;

    @ManyToMany(mappedBy="photographers")
    private Set<Place> starredPlaces;

    @OneToOne
    private Photo photographerPhoto;

    public Photographer() {
        this.starredPlaces = new HashSet<>();
    }


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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Set<Place> getstarredPlaces() {
        return starredPlaces;
    }

    public void setstarredPlaces(Set<Place> starredPlaces) {
        this.starredPlaces = starredPlaces;
    }

    public Photo getPhotographerPhoto() {
        return photographerPhoto;
    }

    public void setPhotographerPhoto(Photo photographerPhoto) {
        this.photographerPhoto = photographerPhoto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    @Override
	public int hashCode() {
		return Objects.hash(name, surname);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Photographer other = (Photographer) obj;
		return Objects.equals(name, other.name) && Objects.equals(surname, other.surname);
	}
}
