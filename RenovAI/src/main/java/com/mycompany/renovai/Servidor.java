package com.mycompany.renovai;

import static spark.Spark.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map;

public class Servidor {
    public static void main(String[] args) {
        port(4567);
        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "*");
            res.header("Access-Control-Allow-Headers", "*");
        });
        options("/*", (req, res) -> {
            String hdrs = req.headers("Access-Control-Request-Headers");
            if (hdrs != null) res.header("Access-Control-Allow-Headers", hdrs);
            String mtd = req.headers("Access-Control-Request-Method");
            if (mtd != null) res.header("Access-Control-Allow-Methods", mtd);
            res.status(200);
            return "OK";
        });

        Gson gson = new Gson();
        Servidor chatbot = new Servidor();

        get("/ping", (req, res) -> "RenovAI online!");

        post("/perguntar", (req, res) -> {
            res.type("application/json");
            JsonObject j;
            try {
                j = JsonParser.parseString(req.body()).getAsJsonObject();
            } catch (Exception e) {
                res.status(400);
                return gson.toJson(Map.of("erro", "JSON inválido."));
            }

            listaMensagens historico = new listaMensagens();
            String ultimaPergunta = null;

            if (j.has("historico")) {
                JsonArray arr = j.getAsJsonArray("historico");
                if (arr.size() == 0) {
                    res.status(400);
                    return gson.toJson(Map.of("erro", "Histórico vazio."));
                }
                for (var el : arr) {
                    String m = el.getAsString().trim();
                    historico.inserir(m);
                }
                ultimaPergunta = arr.get(arr.size() - 1).getAsString().trim();
            } else if (j.has("mensagem")) {
                String m = j.get("mensagem").getAsString().trim();
                if (m.isEmpty()) {
                    res.status(400);
                    return gson.toJson(Map.of("erro", "Mensagem vazia."));
                }
                historico.inserir(m);
                ultimaPergunta = m;
            } else {
                res.status(400);
                return gson.toJson(Map.of("erro", "Envie 'historico' ou 'mensagem'."));
            }

            String resposta;
            try {
                resposta = chatbot.conversa(ultimaPergunta, historico);
            } catch (Exception ex) {
                res.status(500);
                return gson.toJson(Map.of("erro", "Falha interna."));
            }
            return gson.toJson(Map.of("resposta", resposta));
        });
    }
}
