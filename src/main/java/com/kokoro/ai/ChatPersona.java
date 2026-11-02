package com.kokoro.ai;

/** ABSTRACTION: the chat service only knows this interface. */
public interface ChatPersona {
    String key();
    String displayName();
    String systemPrompt(String moodHint);
}
