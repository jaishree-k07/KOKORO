# Kokoro – Personalized Mental Wellness Companion (Spring Boot + MySQL)

## Run it

1. Install JDK 17+, Maven, and MySQL (running on `localhost:3306`).
2. Copy your image files (`penguin.png`, `penguin1.webp`, `moodjar.png`) into `src/main/resources/static/`.
3. Set the following environment variables (or configure them in `application.properties`):

   - `DB_USER` – MySQL username.
   - `DB_PASS` – MySQL password.
   - `LLM_API_KEY` – NVIDIA API key.
   - `LLM_URL` – NVIDIA API endpoint.
   - `LLM_MODEL` – NVIDIA model name.

   **NVIDIA API Configuration:**
   
   - API URL: `https://integrate.api.nvidia.com/v1/chat/completions`
   - Model: `mistralai/mistral-large-4-1t-a52b`

4. Run the application:

   ```bash
   mvn spring-boot:run
   ```

5. Open [http://localhost:8080](http://localhost:8080) in your browser.

   The `kokoro` database and its tables are created automatically.

## AI Integration

Kokoro integrates the NVIDIA API using an OpenAI-compatible Chat Completions endpoint. The Spring Boot backend sends user messages and conversation context to the NVIDIA-hosted Mistral model and receives AI-generated responses.

The `LlmClient` component handles API communication, while the persona classes manage the different AI companion personalities.

## Project Structure

`controller` → `service` → `repository` → MySQL

The `ai` package contains `ChatPersona`, `BasePersona`, `Kelly`, `Hannah`, `Darbie`, and `LlmClient`.
