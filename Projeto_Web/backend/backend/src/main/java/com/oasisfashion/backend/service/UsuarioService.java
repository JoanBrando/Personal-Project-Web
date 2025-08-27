package com.oasisfashion.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oasisfashion.backend.dto.UsuarioCreateDTO;
import com.oasisfashion.backend.dto.UsuarioDTO; // Import necessário
import com.oasisfashion.backend.exception.ResourceNotFoundException;
import com.oasisfashion.backend.model.Usuario;
import com.oasisfashion.backend.repository.UsuarioRepository;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private UsuarioDTO toDTO(Usuario usuario) {
        // ... (implementação do método toDTO continua a mesma)
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setDataNascimento(usuario.getDataNascimento());
        dto.setSexo(usuario.getSexo());
        return dto;
    }

    @Transactional(readOnly = true) // Otimização para operações de apenas leitura
    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true) // Otimização para operações de apenas leitura
    public UsuarioDTO findById(Long id) {
        return usuarioRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id));
    }

    public UsuarioDTO save(UsuarioCreateDTO createDTO) {
        Usuario usuario = new Usuario();
        usuario.setNome(createDTO.getNome());
        usuario.setEmail(createDTO.getEmail());
        usuario.setSenha(createDTO.getSenha()); // Lembrete: Criptografar em produção!
        usuario.setDataNascimento(createDTO.getDataNascimento());
        usuario.setSexo(createDTO.getSexo());

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return toDTO(usuarioSalvo);
    }

    public UsuarioDTO update(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id));

        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setDataNascimento(usuarioDTO.getDataNascimento());
        usuario.setSexo(usuarioDTO.getSexo());

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return toDTO(usuarioAtualizado);
    }

    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado com o id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> authenticate(String email, String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isPresent() && usuarioOpt.get().getSenha().equals(senha)) {
            return usuarioOpt;
        }
        return Optional.empty();
    }
}