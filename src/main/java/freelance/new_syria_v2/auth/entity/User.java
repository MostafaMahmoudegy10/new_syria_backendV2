package freelance.new_syria_v2.auth.entity;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import freelance.new_syria_v2.article.entity.Image;
import freelance.new_syria_v2.country.entity.Country;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@NotBlank(message = "Username cannot be blank")
	@Column(name = "user_name", nullable = false, length = 50)
	private String userName;

	@NotBlank(message = "Password cannot be blank")
	@Column(nullable = false)
	@JsonIgnore
	private String password;

	@Email(message = "You should write a valid email")
	@Column(unique = true, nullable = false)
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role = Role.USER;

	private boolean enabled;

	private boolean isCompletedProfile = false;

	@Column(name = "imageUrl", nullable = true)
	private String imageUrl;

    @Column(nullable = true)
	private String countryName;

	@Column(name = "phone_number", nullable = true, length = 15)
	@Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{9,15}$", message = "Invalid phone number")
	private String phoneNumber;

	@Column(name = "bio",nullable = true)
	private String bio;

}
