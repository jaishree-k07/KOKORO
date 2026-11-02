package com.kokoro.repository;

import com.kokoro.model.LittleWin;
import com.kokoro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WinRepository extends JpaRepository<LittleWin, Long> {
    List<LittleWin> findByUserOrderByIdAsc(User user);
}
