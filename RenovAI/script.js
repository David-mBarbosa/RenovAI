const chatbox = document.getElementById("chatbox");
const mensagem = document.getElementById("mensagem");
let historicoUsuario = [];

function adicionarMensagem(texto, autor = "RenovAI") {
  const wrapper = document.createElement("div");
  wrapper.className = "mb-4";
  if (autor === "Você") {
    wrapper.innerHTML = `<strong>Você:</strong> ${texto}`;
    chatbox.appendChild(wrapper);
    wrapper.scrollIntoView({ behavior: "smooth" });
    return;
  }
  const regex = /(\d+)\.\s*\*\*(.*?)\*\*:\s*([\s\S]*?)(?=(\d+\.\s*\*\*|$))/g;
  const matches = [...texto.matchAll(regex)];
  if (matches.length) {
    const idx = matches[0].index;
    const introText = texto.slice(0, idx).trim();
    const introDiv = document.createElement("div");
    introDiv.innerHTML = `<strong>🌿 RenovAI:</strong> <span class="text-gray-700">${introText.replace(/\n/g, "<br>")}</span>`;
    chatbox.appendChild(introDiv);
    introDiv.scrollIntoView({ behavior: "smooth" });

    const grid = document.createElement("div");
    grid.className = "grid gap-4 mt-4";
    matches.forEach((m, i) => {
      const titulo = m[2].trim();
      const descricao = m[3].trim();
      const icone = escolherIcone(titulo.toLowerCase());
      const card = document.createElement("div");
      card.className = `bg-green-50 border-l-4 border-green-400 p-4 rounded shadow-sm animate-fade-in-up`;
      card.style.animationDelay = `${i * 100}ms`;
      card.innerHTML = `<div class="flex items-center gap-2 mb-1"><span class="text-green-600 text-xl">${icone}</span><span class="font-semibold text-green-800">${titulo}</span></div><p class="text-gray-700 leading-relaxed">${descricao}</p>`;
      grid.appendChild(card);
    });
    chatbox.appendChild(grid);
    grid.scrollIntoView({ behavior: "smooth" });
    return;
  }
  const fallback = document.createElement("div");
  const formatted = texto
    .replace(/\*\*(.*?)\*\*/g, "<strong>$1</strong>")
    .replace(/\n{2,}/g, "<br><br>")
    .replace(/\n/g, "<br>");
  fallback.innerHTML = `<strong>🌿 RenovAI:</strong> ${formatted}`;
  chatbox.appendChild(fallback);
  fallback.scrollIntoView({ behavior: "smooth" });
}

function enviar() {
  const texto = mensagem.value.trim();
  if (!texto) return;
  adicionarMensagem(texto, "Você");
  historicoUsuario.push(texto);
  mensagem.value = "";
  adicionarMensagem("<em class='text-gray-500'>Digitando...</em>");
  fetch("http://127.0.0.1:4567/perguntar", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ historico: historicoUsuario })
  })
    .then(res => res.json())
    .then(data => {
      const last = chatbox.lastChild;
      if (last && last.innerText.includes("Digitando")) chatbox.removeChild(last);
      adicionarMensagem(data.resposta);
    })
    .catch(() => {
      const last = chatbox.lastChild;
      if (last && last.innerText.includes("Digitando")) chatbox.removeChild(last);
      adicionarMensagem("Erro ao se conectar com o servidor.");
    });
}

function limpar() {
  chatbox.innerHTML = "";
  historicoUsuario = [];
}

function escolherIcone(titulo) {
  if (titulo.includes("solar")) return "☀️";
  if (titulo.includes("eólica")) return "💨";
  if (titulo.includes("hidrelétrica")) return "💧";
  if (titulo.includes("biomassa")) return "♻️";
  if (titulo.includes("geotérmica")) return "🌋";
  return "🌿";
}

mensagem.addEventListener("keydown", e => {
  if (e.key === "Enter") enviar();
});

window.addEventListener("DOMContentLoaded", () => {
  adicionarMensagem("Olá! 👋 Eu sou o RenovAI, seu assistente especialista em sustentabilidade e energias renováveis. Como posso ajudar você hoje?");
});
