package com.kokoro.controller;

import com.kokoro.model.User;
import com.kokoro.service.ChatService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    public record ChatReq(String persona, String message) {}
    public record MsgDto(String role, String message) {}

    private final ChatService chat;

    public ChatController(ChatService chat) { this.chat = chat; }

    @PostMapping
    public Map<String, Object> send(@RequestAttribute("user") User user, @RequestBody ChatReq r) {
        return chat.chat(user, r.persona(), r.message());
    }

    @GetMapping("/history")
    public List<MsgDto> history(@RequestAttribute("user") User user, @RequestParam String persona) {
        return chat.history(user, persona).stream().map(m -> new MsgDto(m.getRole(), m.getMessage())).toList();
    }
}
