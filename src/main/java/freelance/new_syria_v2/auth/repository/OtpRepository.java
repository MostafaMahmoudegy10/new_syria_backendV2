package freelance.new_syria_v2.auth.repository;

import freelance.new_syria_v2.auth.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OtpRepository extends JpaRepository<OTP, UUID> {

    @Query(value = """
            select * 
            from otp o
            where o.email = :email 
              and o.code = :codeOtp
            order by created_at desc
            limit 1
        """, nativeQuery = true)
    Optional<OTP> findByEmailAndCodeOtp(@Param("email") String email, @Param("codeOtp") String codeOtp);
}
