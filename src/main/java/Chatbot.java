import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionChoice;

import java.util.List;

public class Chatbot {
    private OpenAiService openAiService;

    public Chatbot() {
        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("OpenAI API key not set in environment variables.");
        }
        openAiService = new OpenAiService(apiKey);
    }

    public String getResponse(String query) {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(query)
                .model("text-davinci-003")
                .maxTokens(150)
                .build();

        List<CompletionChoice> choices = openAiService.createCompletion(completionRequest).getChoices();
        if (choices != null && !choices.isEmpty()) {
            return choices.get(0).getText().trim();
        } else {
            return "I'm sorry, I didn't understand that. Please try asking something else.";
        }
    }
}
