/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.renovai;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import java.util.List;

/**
 *
 * @author david
 */
public class ChatBot {

    SimpleOpenAI openAI = SimpleOpenAI.builder()
            .apiKey(System.getenv("OPENAI_API_KEY"))
            .build();
    
    String conversa(String mensagem, listaMensagens listaMensagem) {
        List<ChatMessage> mensagens = listaMensagem.paraChatMessages();
        mensagens.add(0, ChatMessage.SystemMessage.of(
                "Você é um especialista em sustentabilidade, meio ambiente e fontes de energias renováveis. "
                + "Responda apenas perguntas nesse cunho. Se a pergunta estiver fora do tema, informe sua especialidade e recuse educadamente."
        ));

        var chatRequest = ChatRequest.builder()
                .model("gpt-4o")
                .messages(mensagens)
                .temperature(0.0)
                .maxCompletionTokens(500)
                .build();

        var futureChat = openAI.chatCompletions().create(chatRequest);
        var chatResponse = futureChat.join();

        return chatResponse.firstContent();

    }
}
