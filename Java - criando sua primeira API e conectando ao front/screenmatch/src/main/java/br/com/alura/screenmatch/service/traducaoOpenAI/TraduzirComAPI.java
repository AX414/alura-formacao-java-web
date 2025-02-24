package br.com.alura.screenmatch.service.traducaoOpenAI;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class TraduzirComAPI {
    public static String obterTraducao(String texto) {
        //Cole aqui sua chave da OpenAI
        OpenAiService service = new OpenAiService("colocar o token do openAI aqui");

        //o modelo pode ser gpt-3.5-turbo-instruct também.
        CompletionRequest requisicao = CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt("\nTraduza para o português o texto: " + texto)
                .maxTokens(1000)
                .temperature(0.7)
                .build();

        var resposta = service.createCompletion(requisicao);
        return resposta.getChoices().get(0).getText();
    }
}