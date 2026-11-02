package com.kokoro.service;

import com.kokoro.ai.ChatPersona;
import com.kokoro.ai.LlmClient;
import com.kokoro.ai.PersonaFactory;
import com.kokoro.model.ChatMessage;
import com.kokoro.model.User;
import com.kokoro.repository.ChatMessageRepository;
import com.kokoro.repository.MoodRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {
    private static final List<String> CRISIS_WORDS = List.of(
            "suicide", "kill myself", "end my life", "want to die", "self harm", "self-harm", "hurt myself", "don't want to live");

    private static final String CRISIS_REPLY =
            "I'm really glad you told me, and I'm worried about you. You deserve support from a real person right now. "
          + "If you are in India, you can call Tele-MANAS at 14416 (free, 24/7) or emergency services at 112. "
          + "Elsewhere, findahelpline.com lists local lines. If you're in immediate danger, please call your local emergency number "
          + "or reach out to someone you trust. I'm here too. 💜";

    private final ChatMessageRepository messages;
    private final MoodRepository moods;
    private final LlmClient llm;

    public ChatService(ChatMessageRepository messages, MoodRepository moods, LlmClient llm) {
        this.messages = messages; this.moods = moods; this.llm = llm;
    }

    public Map<String, Object> chat(User user, String personaKey, String text) {
        ChatPersona persona = PersonaFactory.get(personaKey);
        String lower = text.toLowerCase();
        boolean crisis = CRISIS_WORDS.stream().anyMatch(lower::contains);   // safety check BEFORE the LLM

        String reply;
        if (crisis) {
            reply = CRISIS_REPLY;
        } else {
            String mood = moods.findFirstByUserOrderByMoodDateDesc(user).map(m -> m.getEmoji()).orElse(null);
            List<ChatMessage> history = new ArrayList<>(messages.findTop10ByUserAndPersonaOrderByIdDesc(user, persona.key()));
            Collections.reverse(history);
            reply = llm.complete(persona.systemPrompt(mood), history, text);
        }
        save(user, persona.key(), "user", text);
        save(user, persona.key(), "assistant", reply);
        return Map.of("reply", reply, "crisis", crisis);
    }

    public List<ChatMessage> history(User user, String personaKey) {
        return messages.findByUserAndPersonaOrderByIdAsc(user, PersonaFactory.get(personaKey).key());
    }

    private void save(User user, String persona, String role, String text) {
        ChatMessage m = new ChatMessage();
        m.setUser(user); m.setPersona(persona); m.setRole(role); m.setMessage(text);
        messages.save(m);
    }
}
