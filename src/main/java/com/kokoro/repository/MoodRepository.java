package com.kokoro.repository;

import com.kokoro.model.Mood;
import com.kokoro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MoodRepository extends JpaRepository<Mood, Long> {
    List<Mood> findByUser(User user);
    List<Mood> findByUserAndMoodDateBetweenOrderByMoodDate(User user, LocalDate from, LocalDate to);
    Optional<Mood> findByUserAndMoodDate(User user, LocalDate date);
    Optional<Mood> findFirstByUserOrderByMoodDateDesc(User user);
}
