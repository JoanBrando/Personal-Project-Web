document.addEventListener('DOMContentLoaded', () => {

    // --- CONFIGURAÇÕES E SELETORES ---
    const API_BASE_URL = 'http://localhost:8080/api'; // URL correta da API

    // Seletores do Login
    const loginForm = document.getElementById('loginForm');

    // Seletores do Modal de Cadastro
    const registerModal = document.getElementById('register-modal');
    const openModalBtn = document.getElementById('open-register-modal');
    const closeModalBtn = document.getElementById('close-register-modal');
    const registerForm = document.getElementById('registerForm');

    // Span do ano no rodapé
    const currentYearSpan = document.getElementById('currentYear');
    if(currentYearSpan) {
        currentYearSpan.textContent = new Date().getFullYear();
    }
    
    // --- FUNÇÕES DE API (GENÉRICAS) ---
    const apiRequest = async (endpoint, method = 'GET', body = null) => {
        try {
            const options = {
                method,
                headers: { 'Content-Type': 'application/json' }
            };
            if (body) options.body = JSON.stringify(body);

            const response = await fetch(`${API_BASE_URL}${endpoint}`, options);

            const responseBody = await response.json();

            if (!response.ok) {
                // Usa a mensagem de erro do backend se disponível
                throw new Error(responseBody.message || 'Ocorreu um erro na requisição.');
            }
            return responseBody;
        } catch (error) {
            console.error(`Erro de API: ${error.message}`);
            alert(`Erro: ${error.message}`); // Exibe o erro para o usuário
            return null;
        }
    };
    
    // --- LÓGICA DO MODAL DE CADASTRO ---
    const openModal = () => registerModal.classList.add('visible');
    const closeModal = () => {
        registerModal.classList.remove('visible');
        registerForm.reset();
    };

    openModalBtn.addEventListener('click', (e) => {
        e.preventDefault();
        openModal();
    });

    closeModalBtn.addEventListener('click', closeModal);

    // Fecha o modal se clicar fora da área de conteúdo
    registerModal.addEventListener('click', (e) => {
        if (e.target === registerModal) {
            closeModal();
        }
    });

    // --- LÓGICA DOS FORMULÁRIOS ---

    // 1. Cadastro de Novo Usuário
    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const nome = document.getElementById('register-nome').value;
            const email = document.getElementById('register-email').value;
            const senha = document.getElementById('register-senha').value;
            const confirmaSenha = document.getElementById('register-confirma-senha').value;
            
            // Validação do frontend
            if (senha !== confirmaSenha) {
                alert('As senhas não coincidem!');
                return;
            }

            const data = { nome, email, senha };

            const result = await apiRequest('/usuarios/registrar', 'POST', data);
            
            if (result) {
                alert('Cadastro realizado com sucesso! Faça o login.');
                closeModal(); // Fecha o modal após o sucesso
            }
        });
    }

    // 2. Login de Usuário
    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const email = document.getElementById('email').value;
            const senha = document.getElementById('senha').value;
            
            const data = { email, senha };
            
            const result = await apiRequest('/usuarios/login', 'POST', data);

            if (result) {
                alert(result.message); // Ex: "Login bem-sucedido!"
                // Redireciona para a página de administração
                window.location.href = 'sistemaadm.html'; // Mude para a página correta
            }
        });
    }
});