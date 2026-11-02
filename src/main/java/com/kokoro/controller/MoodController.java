package com.kokoro.controller;

import com.kokoro.model.Mood;
import com.kokoro.model.User;
import com.kokoro.repository.MoodRepository;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/moods")
public class MoodController {
    public record MoodReq(String emoji, String note, LocalDate moodDate) {}
    public record MoodDto(LocalDate moodDate, String emoji, String note) {}

    private final MoodRepository moods;

    public MoodController(MoodRepository moods) { this.moods = moods; }

    @GetMapping
    public List<MoodDto> month(@RequestAttribute("user") User user, @RequestParam String month) {
        YearMonth ym = YearMonth.parse(month);
        return moods.findByUserAndMoodDateBetweenOrderByMoodDate(user, ym.atDay(1), ym.atEndOfMonth())
                .stream().map(m -> new MoodDto(m.getMoodDate(), m.getEmoji(), m.getNote())).toList();
    }

    /** One mood per day: saving again for the same date overwrites it. */
    @PostMapping
    public Map<String, Object> save(@RequestAttribute("user") User user, @RequestBody MoodReq r) {
        LocalDate date = r.moodDate() != null ? r.moodDate() : LocalDate.now();
        Mood m = moods.findByUserAndMoodDate(user, date).orElseGet(Mood::new);
        m.setUser(user);
        m.setMoodDate(date);
        m.setEmoji(r.emoji());
        m.setNote(r.note());
        moods.save(m);
        return Map.of("ok", true);
    }
}
