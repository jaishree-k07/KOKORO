package com.kokoro.repository;

import com.kokoro.model.JournalEntry;
import com.kokoro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JournalRepository extends JpaRepository<JournalEntry, Long> {
    List<JournalEntry> findByUserOrderByCreatedAtDesc(User user);
}
