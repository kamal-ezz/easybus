package com.kamal.easybus.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kamal.easybus.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;


@NoArgsConstructor
@Entity
@Table(name = "users")
@Getter
@Setter
public class User{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;
	@NotBlank
	@Size(min = 6, max = 20)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@NotBlank
	@Size(max = 40)
	@Email
	@Column(unique = true)
	private String email;

	@NotBlank
	private String phone;
	@Transient
    private Set<Role> roles;

	
	public User(String firstName, String lastName, String email,String password, String phone) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phone = phone;
	}
}
