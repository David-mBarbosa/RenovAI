/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.renovai;

import java.util.Scanner;

/**
 *
 * @author david
 */
public class RenovAI {
    public static void main(String[] args) {
        
        Scanner scn = new Scanner(System.in);
        ChatBot renovAI = new ChatBot();
        listaMensagens listaMensagens = new listaMensagens();

        System.out.println("Quer consultar a RenovAI? (sim/não)");
        String resposta = scn.nextLine();

        if (resposta.equalsIgnoreCase("não") || resposta.equalsIgnoreCase("sair")) {
            System.out.println("Encerrando conversa.");
            return;
        }

        while (true) {
            System.out.println("Pergunte à RenovAI ('sair' para encerrar):");
            String pergunta = scn.nextLine();

            if (pergunta.equalsIgnoreCase("sair")) {
                System.out.println("Encerrando conversa.");
                break;
            }

            listaMensagens.inserir(pergunta);
            String respostaIA = renovAI.conversa(pergunta, listaMensagens);
            System.out.println("RenovAI: " + respostaIA);
        }
    }
}

