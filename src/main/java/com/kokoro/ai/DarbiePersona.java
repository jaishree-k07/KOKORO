package com.kokoro.ai;

public class DarbiePersona extends BasePersona {
    public DarbiePersona() { super("darbie", "Darbie"); }
    @Override protected String tone() { return "Be cheerful, witty and playfully sarcastic, but never mock the user's feelings. Lighten the mood kindly."; }
}
