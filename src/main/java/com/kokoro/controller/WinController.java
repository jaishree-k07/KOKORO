package com.kokoro.controller;

import com.kokoro.model.Achievement;
import com.kokoro.model.LittleWin;
import com.kokoro.model.User;
import com.kokoro.repository.AchievementRepository;
import com.kokoro.repository.WinRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class WinController {
    public record WinReq(String text) {}
    public record WinDto(Long id, String text) {}
    public record AchDto(Long id, String title) {}

    private final WinRepository wins;
    private final AchievementRepository achievements;

    public WinController(WinRepository wins, AchievementRepository achievements) {
        this.wins = wins; this.achievements = achievements;
    }

    @GetMapping("/wins")
    public List<WinDto> list(@RequestAttribute("user") User user) {
        return wins.findByUserOrderByIdAsc(user).stream().map(w -> new WinDto(w.getId(), w.getText())).toList();
    }

    @PostMapping("/wins")
    public Map<String, Object> add(@RequestAttribute("user") User user, @RequestBody WinReq r) {
        LittleWin w = new LittleWin();
        w.setUser(user);
        w.setText(r.text());
        wins.save(w);
        return Map.of("ok", true);
    }

    /** Completing a win moves it into Achievements. */
    @PutMapping("/wins/{id}/complete")
    @Transactional
    public ResponseEntity<?> complete(@RequestAttribute("user") User user, @PathVariable Long id) {
        var w = wins.findById(id).filter(x -> x.getUser().getId().equals(user.getId()));
        if (w.isEmpty()) return ResponseEntity.status(404).body(Map.of("error", "Not found"));
        Achievement a = new Achievement();
        a.setUser(user);
        a.setTitle(w.get().getText());
        achievements.save(a);
        wins.delete(w.get());
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @GetMapping("/achievements")
    public List<AchDto> achievements(@RequestAttribute("user") User user) {
        return achievements.findByUserOrderByIdAsc(user).stream().map(a -> new AchDto(a.getId(), a.getTitle())).toList();
    }

    @DeleteMapping("/achievements/{id}")
    public ResponseEntity<?> delete(@RequestAttribute("user") User user, @PathVariable Long id) {
        var a = achievements.findById(id).filter(x -> x.getUser().getId().equals(user.getId()));
        if (a.isEmpty()) return ResponseEntity.status(404).body(Map.of("error", "Not found"));
        achievements.delete(a.get());
        return ResponseEntity.ok(Map.of("ok", true));
    }
}
