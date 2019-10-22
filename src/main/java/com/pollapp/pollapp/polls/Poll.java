package com.pollapp.pollapp.polls;

import com.pollapp.pollapp.dateAudit.UserDateAudit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
@Entity
public class Poll extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 140)
    private String question;
    @NotNull
    private Instant expirationDateTime;

}
