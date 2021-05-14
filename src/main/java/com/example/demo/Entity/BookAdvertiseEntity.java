package com.example.demo.Entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.demo.dto.AdvertiseDetailsDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "book_advertise")
//@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@NamedNativeQuery(name = "AdvertiseAndUserDetailsQuery",
query = "SELECT b.id, b.name, b.description, b.image_name as image, b.genre, b.price, b.user_id, CONCAT(u.firstname, ' ', u.lastname) AS user_fullname, u.pincode, b.finalised, b.created_at, (SELECT count(*) FROM user_interest_advertise WHERE user_id = :user_id AND advertise_id = :advertise_id) AS user_interested FROM book_advertise b JOIN users u ON b.user_id = u.id WHERE b.id = :advertise_id",
resultSetMapping = "AdvertiseAndUserDetailsMapping",
resultClass = AdvertiseDetailsDto.class
)
@SqlResultSetMapping(
	name = "AdvertiseAndUserDetailsMapping",
    classes = @ConstructorResult(
        columns = {
            @ColumnResult(name = "id", type = Long.class),
            @ColumnResult(name = "name",  type = String.class),
            @ColumnResult(name = "description",  type = String.class),
            @ColumnResult(name = "image",  type = String.class),
            @ColumnResult(name = "genre",  type = String.class),
            @ColumnResult(name = "price", type = int.class),
            @ColumnResult(name = "user_id", type = Long.class),
            @ColumnResult(name = "user_fullname",  type = String.class),
            @ColumnResult(name = "pincode",  type = String.class),
            @ColumnResult(name = "finalised", type = boolean.class),
            @ColumnResult(name = "created_at",  type = String.class),
            @ColumnResult(name = "user_interested",  type = int.class)
        },
        targetClass = AdvertiseDetailsDto.class
    )
)
public class BookAdvertiseEntity extends AuditModel{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@NotNull
	@Size(max = 50)
	private String name;
	
	@NotNull
	@Size(max = 50)
	private String genre; 
	
	@NotNull
	@Size(max = 255)
	private String description;
	
	private int price;
	
	@NotNull
	@Size(max=50)
	@Column(name = "image_name", length = 50)
	private String imageName;
		
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private UserEntity user;

	@Column(columnDefinition = "boolean default 'f'")
	private boolean finalised = false;
	
	public boolean isFinalised() {
		return finalised;
	}

	public void setFinalised(boolean finalised) {
		this.finalised = finalised;
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

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "BookAdvertise [id=" + id + ", name=" + name + ", genre=" + genre + ", description=" + description
				+ ", price=" + price + ", user=" + user + "]";
	}
	
}
