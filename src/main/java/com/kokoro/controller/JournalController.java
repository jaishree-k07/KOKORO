package com.kokoro.controller;

import com.kokoro.model.JournalEntry;
import com.kokoro.model.User;
import com.kokoro.repository.JournalRepository;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/journal")
public class JournalController {
    public record EntryReq(String content) {}
    public record EntryDto(Long id, String content, LocalDateTime createdAt) {}

    private final JournalRepository journal;

    public JournalController(JournalRepository journal) { this.journal = journal; }

    @GetMapping
    public List<EntryDto> list(@RequestAttribute("user") User user) {
        return journal.findByUserOrderByCreatedAtDesc(user).stream()
                .map(e -> new EntryDto(e.getId(), e.getContent(), e.getCreatedAt())).toList();
    }

    @PostMapping
    public Map<String, Object> add(@RequestAttribute("user") User user, @RequestBody EntryReq r) {
        JournalEntry e = new JournalEntry();
        e.setUser(user);
        e.setContent(r.content());
        journal.save(e);
        return Map.of("ok", true);
    }
}
