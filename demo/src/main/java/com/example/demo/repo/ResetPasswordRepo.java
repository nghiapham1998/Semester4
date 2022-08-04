package com.example.demo.repo;

import com.example.demo.domain.ConfirmationToken;
import com.example.demo.domain.ResetPassword;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ResetPasswordRepo extends CrudRepository<ResetPassword,Long> {
    Optional<ResetPassword> findByToken(String Token);

    @Transactional
    @Modifying
    @Query("UPDATE ResetPassword c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);
}
