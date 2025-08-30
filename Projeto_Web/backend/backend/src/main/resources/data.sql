
MERGE INTO perfil_de_usuario (id, nome, descricao) KEY(id) VALUES (1, 'Administrador', 'Acesso total ao sistema.');

MERGE INTO perfil_de_usuario (id, nome, descricao) KEY(id) VALUES (2, 'Cliente', 'Perfil padrão para novos usuários cadastrados.');