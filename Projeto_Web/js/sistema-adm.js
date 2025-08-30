document.addEventListener('DOMContentLoaded', () => {
            
    // --- CONFIGURAÇÕES E SELETORES ---
    const API_BASE_URL = 'http://localhost:8080/api';
    const navLinks = document.querySelectorAll('.nav-link');
    const contentSections = document.querySelectorAll('.content-section');
    const modal = document.getElementById('crud-modal');
    const modalTitle = document.getElementById('modal-title');
    const crudForm = document.getElementById('crud-form');
    const formFields = document.getElementById('form-fields');
    const editIdField = document.getElementById('edit-id');
    
    const entityEndpoints = {
        perfil: 'perfis',
        cliente: 'clientes',
        fornecedor: 'fornecedores'
    };
    
    let currentEntity = 'perfis';

    // --- FUNÇÕES DE API (COMUNICAÇÃO COM O BACKEND) ---
    
    const apiRequest = async (endpoint, method = 'GET', body = null) => {
        try {
            const options = {
                method,
                headers: { 'Content-Type': 'application/json' }
            };
            if (body) options.body = JSON.stringify(body);
            
            const response = await fetch(`${API_BASE_URL}${endpoint}`, options);

            if (!response.ok) {
                const errorText = await response.text();
                if (errorText.includes('<!DOCTYPE html>')) {
                     throw new Error(`Endpoint não encontrado ou erro no servidor. Rota chamada: ${response.url}`);
                }
                const errorData = JSON.parse(errorText);
                throw new Error(errorData.message || 'Erro na comunicação com o servidor.');
            }
            return response.status === 204 ? null : response.json();
        } catch (error) {
            alert(`Erro de API: ${error.message}`);
            return null;
        }
    };
    
    // --- FUNÇÕES DE RENDERIZAÇÃO (EXIBIÇÃO DOS DADOS) ---

    const renderTable = {
        perfis: (data) => {
            const tableBody = document.getElementById('perfis-table-body');
            tableBody.innerHTML = data.map(item => `
                <tr>
                    <td>${item.id}</td>
                    <td>${item.nome}</td>
                    <td>${item.descricao}</td>
                    <td><button class="btn btn-icon btn-edit" data-id="${item.id}" data-entity="perfil"><i class="fas fa-edit"></i></button></td>
                    <td><button class="btn btn-icon btn-delete" data-id="${item.id}" data-entity="perfil"><i class="fas fa-trash-alt"></i></button></td>
                </tr>
            `).join('');
        },
        clientes: (data) => {
            const tableBody = document.getElementById('clientes-table-body');
            tableBody.innerHTML = data.map(item => `
                <tr>
                    <td>${item.id}</td>
                    <td>${item.nome}</td>
                    <td>${item.documento}</td>
                    <td>${item.telefone || 'N/A'}</td>
                    <td><button class="btn btn-icon btn-edit" data-id="${item.id}" data-entity="cliente"><i class="fas fa-edit"></i></button></td>
                    <td><button class="btn btn-icon btn-delete" data-id="${item.id}" data-entity="cliente"><i class="fas fa-trash-alt"></i></button></td>
                </tr>
            `).join('');
        },
        fornecedores: (data) => {
             const tableBody = document.getElementById('fornecedores-table-body');
            tableBody.innerHTML = data.map(item => `
                <tr>
                    <td>${item.id}</td>
                    <td>${item.nome}</td>
                    <td>${item.documento}</td>
                    <td>${item.telefone || 'N/A'}</td>
                    <td><button class="btn btn-icon btn-edit" data-id="${item.id}" data-entity="fornecedor"><i class="fas fa-edit"></i></button></td>
                    <td><button class="btn btn-icon btn-delete" data-id="${item.id}" data-entity="fornecedor"><i class="fas fa-trash-alt"></i></button></td>
                </tr>
            `).join('');
        }
    };

    // CORREÇÃO APLICADA AQUI
    const loadData = async () => {
        const endpoint = `/${currentEntity}`;
        const data = await apiRequest(endpoint);
        if (data && renderTable[currentEntity]) {
            renderTable[currentEntity](data);
        }
    };
    
    // --- LÓGICA DO MODAL E FORMULÁRIOS ---

    const getFormFieldsHTML = (entity, data = {}) => {
        const commonFields = `
            <div class="form-group">
                <label for="nome">Nome</label>
                <input type="text" id="nome" name="nome" value="${data.nome || ''}" required>
            </div>
        `;
        switch(entity) {
            case 'perfil':
                return commonFields + `
                    <div class="form-group">
                        <label for="descricao">Descrição</label>
                        <textarea id="descricao" name="descricao" rows="3">${data.descricao || ''}</textarea>
                    </div>`;
            case 'cliente':
                return commonFields + `
                    <div class="form-group"><label for="documento">Documento (CPF)</label><input type="text" id="documento" name="documento" value="${data.documento || ''}" required></div>
                    <div class="form-group"><label for="telefone">Telefone</label><input type="text" id="telefone" name="telefone" value="${data.telefone || ''}"></div>
                    <div class="form-group"><label for="endereco">Endereço</label><input type="text" id="endereco" name="endereco" value="${data.endereco || ''}"></div>
                    <div class="form-group"><label for="limiteCredito">Limite de Crédito</label><input type="number" id="limiteCredito" name="limiteCredito" step="0.01" value="${data.limiteCredito || ''}"></div>
                    <div class="form-group"><label for="historicoCompras">Histórico</label><textarea id="historicoCompras" name="historicoCompras">${data.historicoCompras || ''}</textarea></div>
                `;
            case 'fornecedor':
                 return commonFields + `
                    <div class="form-group"><label for="documento">Documento (CNPJ)</label><input type="text" id="documento" name="documento" value="${data.documento || ''}" required></div>
                    <div class="form-group"><label for="telefone">Telefone</label><input type="text" id="telefone" name="telefone" value="${data.telefone || ''}"></div>
                    <div class="form-group"><label for="endereco">Endereço</label><input type="text" id="endereco" name="endereco" value="${data.endereco || ''}"></div>
                    <div class="form-group"><label for="prazoEntregaMedioDias">Prazo de Entrega (dias)</label><input type="number" id="prazoEntregaMedioDias" name="prazoEntregaMedioDias" value="${data.prazoEntregaMedioDias || ''}"></div>
                    <div class="form-group"><label for="listaProdutosFornecidos">Produtos</label><textarea id="listaProdutosFornecidos" name="listaProdutosFornecidos">${data.listaProdutosFornecidos || ''}</textarea></div>
                `;
            default: return '';
        }
    };
    
    const openModal = (title, fieldsHTML, id = null) => {
        modalTitle.textContent = title;
        formFields.innerHTML = fieldsHTML;
        editIdField.value = id || '';
        modal.classList.add('visible');
    };

    const closeModal = () => {
        modal.classList.remove('visible');
        crudForm.reset();
    };

    // --- EVENT LISTENERS (AÇÕES DO USUÁRIO) ---

    navLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            currentEntity = e.target.closest('a').dataset.target;

            navLinks.forEach(l => l.classList.remove('active'));
            e.target.closest('a').classList.add('active');

            contentSections.forEach(s => s.classList.remove('active'));
            document.getElementById(currentEntity).classList.add('active');
            
            loadData();
        });
    });

    document.body.addEventListener('click', async (e) => {
        const button = e.target.closest('button');
        if (!button) return;

        const { entity, id } = button.dataset;
        const endpointName = entityEndpoints[entity];

        if (button.classList.contains('btn-add')) {
            openModal(`Cadastrar Novo ${entity.charAt(0).toUpperCase() + entity.slice(1)}`, getFormFieldsHTML(entity));
        }

        if (button.classList.contains('btn-edit')) {
            const data = await apiRequest(`/${endpointName}/${id}`);
            if (data) {
                openModal(`Editar ${entity.charAt(0).toUpperCase() + entity.slice(1)}`, getFormFieldsHTML(entity, data), id);
            }
        }

        if (button.classList.contains('btn-delete')) {
            if (confirm(`Tem certeza que deseja excluir o item de ID ${id}?`)) {
                await apiRequest(`/${endpointName}/${id}`, 'DELETE');
                loadData();
            }
        }
        
        if (button.classList.contains('btn-cancel')) {
            closeModal();
        }
    });

    // CORREÇÃO APLICADA AQUI
    crudForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const id = editIdField.value;
        const formData = new FormData(crudForm);
        const data = Object.fromEntries(formData.entries());
        
        Object.keys(data).forEach(key => {
            const input = crudForm.querySelector(`[name="${key}"]`);
            if (input && input.type === 'number' && input.value === '') {
                data[key] = null;
            }
        });

        const endpoint = id ? `/${currentEntity}/${id}` : `/${currentEntity}`;
        const method = id ? 'PUT' : 'POST';
        
        const result = await apiRequest(endpoint, method, data);
        if(result !== null || method === 'PUT' || method === 'DELETE') { 
            closeModal();
            loadData();
        }
    });

    // --- INICIALIZAÇÃO ---
    document.getElementById('currentYear').textContent = new Date().getFullYear();
    loadData();
});
