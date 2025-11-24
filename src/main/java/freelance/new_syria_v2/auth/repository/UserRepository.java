package freelance.new_syria_v2.auth.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import freelance.new_syria_v2.auth.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

	@Query("select u from User u where u.email=:email")
	Optional<User> findByEmail(String email);

}
