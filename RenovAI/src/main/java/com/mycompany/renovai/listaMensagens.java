package com.mycompany.renovai;

public class listaMensagens {
    public No inicio;
    public void inserir(String mensagem) {
        No novo = new No(mensagem);
        if (inicio == null) {
            inicio = novo;
        } else {
            No atual = inicio;
            while (atual.proximo != null) atual = atual.proximo;
            atual.proximo = novo;
        }
    }
}
