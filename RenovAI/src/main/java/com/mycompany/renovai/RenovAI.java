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
        System.out.println("Pergunte ao RenovAI: ");
        String texto = scn.nextLine();
        
        if(texto == null || texto.equalsIgnoreCase("sair")){
            System.out.println("Encerrando conversa");
        }else{
            ChatBot RenovAI = new ChatBot();
            listaMensagens listaMensagens = new listaMensagens();
            listaMensagens.inserir(texto);
            System.out.println(RenovAI.conversa(texto, listaMensagens));
        }
}
}