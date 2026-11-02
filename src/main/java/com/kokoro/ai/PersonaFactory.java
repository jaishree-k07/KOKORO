package com.kokoro.ai;

import java.util.Map;

public final class PersonaFactory {
    private static final Map<String, ChatPersona> PERSONAS = Map.of(
            "kelly", new KellyPersona(),
            "hannah", new HannahPersona(),
            "darbie", new DarbiePersona());

    private PersonaFactory() {}

    /** POLYMORPHISM: callers get a ChatPersona and never care which subclass it is. */
    public static ChatPersona get(String key) {
        return PERSONAS.getOrDefault(key == null ? "" : key.toLowerCase(), PERSONAS.get("kelly"));
    }
}
