package com.kokoro.controller;

import com.kokoro.model.User;
import com.kokoro.repository.JournalRepository;
import com.kokoro.repository.MoodRepository;
import com.kokoro.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class StatsController {
    public record CheckinReq(int score) {}

    private final UserRepository users;
    private final MoodRepository moods;
    private final JournalRepository journal;

    public StatsController(UserRepository users, MoodRepository moods, JournalRepository journal) {
        this.users = users; this.moods = moods; this.journal = journal;
    }

    @PostMapping("/checkin")
    public Map<String, Object> checkin(@RequestAttribute("user") User user, @RequestBody CheckinReq r) {
        user.setCheckinScore(r.score());
        user.setCheckedIn(true);
        users.save(user);
        return Map.of("ok", true);
    }

    /** Streak = consecutive days (ending today or yesterday) with a mood or diary entry. */
    @GetMapping("/streak")
    public Map<String, Object> streak(@RequestAttribute("user") User user) {
        Set<LocalDate> days = new HashSet<>();
        moods.findByUser(user).forEach(m -> days.add(m.getMoodDate()));
        journal.findByUserOrderByCreatedAtDesc(user).forEach(j -> days.add(j.getCreatedAt().toLocalDate()));
        LocalDate d = LocalDate.now();
        if (!days.contains(d)) d = d.minusDays(1);
        int s = 0;
        while (days.contains(d)) { s++; d = d.minusDays(1); }
        return Map.of("streak", s);
    }
}
