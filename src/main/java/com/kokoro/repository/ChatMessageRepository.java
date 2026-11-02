package com.kokoro.repository;

import com.kokoro.model.ChatMessage;
import com.kokoro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findTop10ByUserAndPersonaOrderByIdDesc(User user, String persona);
    List<ChatMessage> findByUserAndPersonaOrderByIdAsc(User user, String persona);
}
