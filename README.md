# Kokoro – Personalized Mental Wellness Companion (Spring Boot + MySQL)

## Run it
1. Install JDK 17+, Maven, MySQL (running on localhost:3306).
2. Copy your image files (penguin.png, penguin1.webp, moodjar.png) into `src/main/resources/static/`.
3. Set environment variables (or edit application.properties):
   - DB_USER, DB_PASS      (MySQL login, default root/root)
   - LLM_API_KEY           (OpenAI key, or Gemini/Groq key; optional URL/model below)
   - LLM_URL, LLM_MODEL    (default: OpenAI gpt-4o-mini)
   Gemini example: LLM_URL=https://generativelanguage.googleapis.com/v1beta/openai/chat/completions  LLM_MODEL=gemini-2.0-flash
4. `mvn spring-boot:run`  then open http://localhost:8080
   (the `kokoro` database and all tables are created automatically)

## Structure (matches Review 1 slides)
controller -> service -> repository -> MySQL, plus `ai` package (ChatPersona, BasePersona, Kelly/Hannah/Darbie, LlmClient).
