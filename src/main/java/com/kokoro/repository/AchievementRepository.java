package com.kokoro.repository;

import com.kokoro.model.Achievement;
import com.kokoro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    List<Achievement> findByUserOrderByIdAsc(User user);
}
