package fpt.aptech.projectcard.domain;

import java.time.LocalDateTime;

public class ResetPassword {

    private Long id;

    private String token;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    private User appUser;

    public ResetPassword(String token,
                             LocalDateTime createdAt,
                             LocalDateTime expiresAt,
                             User appUser) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.appUser = appUser;
    }
}
