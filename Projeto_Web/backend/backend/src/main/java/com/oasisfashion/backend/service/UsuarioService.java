package com.oasisfashion.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oasisfashion.backend.dto.ClienteDTO;
import com.oasisfashion.backend.dto.FornecedorDTO;
import com.oasisfashion.backend.dto.PerfilDeUsuarioDTO;
import com.oasisfashion.backend.dto.PessoaDTO;
import com.oasisfashion.backend.dto.UsuarioCreateDTO;
import com.oasisfashion.backend.dto.UsuarioDTO;
import com.oasisfashion.backend.exception.ResourceNotFoundException;
import com.oasisfashion.backend.model.Cliente;
import com.oasisfashion.backend.model.Fornecedor;
import com.oasisfashion.backend.model.PerfilDeUsuario;
import com.oasisfashion.backend.model.Pessoa;
import com.oasisfashion.backend.model.Usuario;
import com.oasisfashion.backend.repository.PerfilDeUsuarioRepository;
import com.oasisfashion.backend.repository.UsuarioRepository;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PerfilDeUsuarioRepository perfilDeUsuarioRepository;

    private Pessoa toPessoaEntity(PessoaDTO dto) {
        if (dto instanceof ClienteDTO) {
            ClienteDTO clienteDTO = (ClienteDTO) dto;
            Cliente cliente = new Cliente();
            cliente.setNome(clienteDTO.getNome());
            cliente.setDocumento(clienteDTO.getDocumento());
            cliente.setTelefone(clienteDTO.getTelefone());
            cliente.setEndereco(clienteDTO.getEndereco());
            cliente.setHistoricoCompras(clienteDTO.getHistoricoCompras());
            cliente.setLimiteCredito(clienteDTO.getLimiteCredito());
            return cliente;
        } else if (dto instanceof FornecedorDTO) {
            FornecedorDTO fornecedorDTO = (FornecedorDTO) dto;
            Fornecedor fornecedor = new Fornecedor();
            fornecedor.setNome(fornecedorDTO.getNome());
            fornecedor.setDocumento(fornecedorDTO.getDocumento());
            fornecedor.setTelefone(fornecedorDTO.getTelefone());
            fornecedor.setEndereco(fornecedorDTO.getEndereco());
            fornecedor.setListaProdutosFornecidos(fornecedorDTO.getListaProdutosFornecidos());
            fornecedor.setPrazoEntregaMedioDias(fornecedorDTO.getPrazoEntregaMedioDias());
            return fornecedor;
        }
        throw new IllegalArgumentException("Tipo de PessoaDTO desconhecido ou não suportado: " + dto.getClass().getName());
    }

    private UsuarioDTO toUsuarioDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setDataNascimento(usuario.getDataNascimento());
        dto.setSexo(usuario.getSexo());

        PerfilDeUsuarioDTO perfilDTO = new PerfilDeUsuarioDTO();
        perfilDTO.setId(usuario.getPerfil().getId());
        perfilDTO.setNome(usuario.getPerfil().getNome());
        perfilDTO.setDescricao(usuario.getPerfil().getDescricao());
        dto.setPerfil(perfilDTO);

        if (usuario.getPessoa() instanceof Cliente) {
            Cliente cliente = (Cliente) usuario.getPessoa();
            ClienteDTO clienteDTO = new ClienteDTO();
            clienteDTO.setNome(cliente.getNome());
            clienteDTO.setDocumento(cliente.getDocumento());
            clienteDTO.setTelefone(cliente.getTelefone());
            clienteDTO.setEndereco(cliente.getEndereco());
            clienteDTO.setHistoricoCompras(cliente.getHistoricoCompras());
            clienteDTO.setLimiteCredito(cliente.getLimiteCredito());
            dto.setPessoa(clienteDTO);
        } else if (usuario.getPessoa() instanceof Fornecedor) {
            Fornecedor fornecedor = (Fornecedor) usuario.getPessoa();
            FornecedorDTO fornecedorDTO = new FornecedorDTO();
            fornecedorDTO.setNome(fornecedor.getNome());
            fornecedorDTO.setDocumento(fornecedor.getDocumento());
            fornecedorDTO.setTelefone(fornecedor.getTelefone());
            fornecedorDTO.setEndereco(fornecedor.getEndereco());
            fornecedorDTO.setListaProdutosFornecidos(fornecedor.getListaProdutosFornecidos());
            fornecedorDTO.setPrazoEntregaMedioDias(fornecedor.getPrazoEntregaMedioDias());
            dto.setPessoa(fornecedorDTO);
        }
        return dto;
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(this::toUsuarioDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioDTO findById(Long id) {
        return usuarioRepository.findById(id)
                .map(this::toUsuarioDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id));
    }

    public UsuarioDTO save(UsuarioCreateDTO createDTO) {
        PerfilDeUsuario perfil = perfilDeUsuarioRepository.findById(createDTO.getPerfilId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado com o id: " + createDTO.getPerfilId()));

        Usuario usuario = new Usuario();
        usuario.setEmail(createDTO.getEmail());
        usuario.setSenha(createDTO.getSenha()); // Lembrete: Criptografar em produção!
        usuario.setDataNascimento(createDTO.getDataNascimento());
        usuario.setSexo(createDTO.getSexo());
        usuario.setPerfil(perfil);
        usuario.setPessoa(toPessoaEntity(createDTO.getPessoa()));

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return toUsuarioDTO(usuarioSalvo);
    }

    public UsuarioDTO update(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id));

        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setDataNascimento(usuarioDTO.getDataNascimento());
        usuario.setSexo(usuarioDTO.getSexo());
        // Nota: Geralmente não se atualiza perfil ou pessoa por este endpoint, mas é possível adicionar a lógica se necessário.
        
        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return toUsuarioDTO(usuarioAtualizado);
    }

    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado com o id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> authenticate(String email, String senha) {
        return usuarioRepository.findByEmail(email)
                .filter(usuario -> usuario.getSenha().equals(senha)); // Lembrete: Usar BCrypt em produção
    }
}