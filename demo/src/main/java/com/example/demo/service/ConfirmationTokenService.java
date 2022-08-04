package com.example.demo.service;


import com.example.demo.domain.ConfirmationToken;
import com.example.demo.repo.ConfirmationTokenRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService  {

    private final ConfirmationTokenRepo confirmationTokenRepo;

    public void ConfirmationToken(ConfirmationToken token){
        confirmationTokenRepo.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepo.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepo.updateConfirmedAt(
                token, LocalDateTime.now());
    }
    public void delete(String token){
        confirmationTokenRepo.deleteAllByTokenEquals(token);
    }
}
