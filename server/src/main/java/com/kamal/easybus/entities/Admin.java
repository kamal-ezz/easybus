package com.kamal.easybus.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class Admin {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String fullName;

	@Size(min = 6, max = 20)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@Size(max = 40)
	@Column(unique = true)
	private String email;
}
