/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.renovai;

import java.util.ArrayList;
import java.util.List;
import io.github.sashirestela.openai.domain.chat.ChatMessage;

/**
 *
 * @author david
 */
public class listaMensagens {

    No inicio;

    void inserir(String mensagem) {
        No novo = new No(mensagem);
        if (inicio == null) {
            inicio = novo;
        } else {
            No atual = inicio;
            while (atual.proximo != null) {
                atual = atual.proximo;
            }
            atual.proximo = novo;
        }
    }

    void inserirEm(String mensagem, int posicao) {
        No novo = new No(mensagem);

        if (posicao == 0) {
            novo.proximo = inicio;
            inicio = novo;
            return;
        }

        No atual = inicio;
        for (int i = 0; i < posicao - 1 && atual != null; i++) {
            atual = atual.proximo;
        }

        if (atual == null) {
            System.out.println("Posição inválida!");
            return;
        }

        novo.proximo = atual.proximo;
        atual.proximo = novo;
    }

    void exibir() {
        No atual = inicio;
        while (atual != null) {
            System.out.print(atual.mensagem + " -> ");
            atual = atual.proximo;
        }
        System.out.println("null");
    }
    
    public List<ChatMessage> paraChatMessages() {
        List<ChatMessage> mensagens = new ArrayList<>();
        No atual = inicio;
        while (atual != null) {
            mensagens.add(ChatMessage.UserMessage.of(atual.mensagem));
            atual = atual.proximo;
        }
        return mensagens;
    }
}
