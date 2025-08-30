package com.oasisfashion.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oasisfashion.backend.exception.ResourceNotFoundException;
import com.oasisfashion.backend.model.PerfilDeUsuario;
import com.oasisfashion.backend.repository.PerfilDeUsuarioRepository;

@Service
@Transactional
public class PerfilDeUsuarioService {

    @Autowired
    private PerfilDeUsuarioRepository perfilRepository;

    @Transactional(readOnly = true)
    public List<PerfilDeUsuario> findAll() {
        return perfilRepository.findAll();
    }

    @Transactional(readOnly = true)
    public PerfilDeUsuario findById(Long id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado com id: " + id));
    }

    public PerfilDeUsuario save(PerfilDeUsuario perfil) {
        return perfilRepository.save(perfil);
    }

    public PerfilDeUsuario update(Long id, PerfilDeUsuario perfilDetalhes) {
        PerfilDeUsuario perfil = findById(id);
        perfil.setNome(perfilDetalhes.getNome());
        perfil.setDescricao(perfilDetalhes.getDescricao());
        return perfilRepository.save(perfil);
    }

    public void delete(Long id) {
        if (!perfilRepository.existsById(id)) {
            throw new ResourceNotFoundException("Perfil não encontrado com id: " + id);
        }
        perfilRepository.deleteById(id);
    }
}