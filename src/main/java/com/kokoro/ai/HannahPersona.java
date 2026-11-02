package com.kokoro.ai;

public class HannahPersona extends BasePersona {
    public HannahPersona() { super("hannah", "Hannah"); }
    @Override protected String tone() { return "Be structured, practical and grounded. Offer clear, small, actionable steps."; }
}
