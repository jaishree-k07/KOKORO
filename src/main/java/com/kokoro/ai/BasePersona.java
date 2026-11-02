package com.kokoro.ai;

/** INHERITANCE: shared behaviour lives here; subclasses only supply their tone. */
public abstract class BasePersona implements ChatPersona {
    private final String key;
    private final String displayName;

    protected BasePersona(String key, String displayName) {
        this.key = key;                       // ENCAPSULATION: private fields
        this.displayName = displayName;
    }

    protected abstract String tone();

    @Override public String key() { return key; }
    @Override public String displayName() { return displayName; }

    @Override
    public String systemPrompt(String moodHint) {
        return "You are " + displayName + ", a companion inside Kokoro, a mental wellness app. " + tone()
             + " Keep replies short (2-5 sentences). You are not a therapist: never diagnose, "
             + "and gently suggest a counsellor or helpline if the user seems to be struggling seriously."
             + (moodHint == null ? "" : " The user's most recently logged mood: " + moodHint + ".");
    }
}
