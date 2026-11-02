package com.kokoro.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Mood {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private User user;
    private LocalDate moodDate;
    private String emoji;
    @Column(length = 1000)
    private String note;
    private LocalDateTime createdAt = LocalDateTime.now();
}
