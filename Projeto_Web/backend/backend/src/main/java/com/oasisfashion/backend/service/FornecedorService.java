package com.oasisfashion.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oasisfashion.backend.exception.ResourceNotFoundException;
import com.oasisfashion.backend.model.Fornecedor;
import com.oasisfashion.backend.repository.FornecedorRepository;

@Service
@Transactional
public class FornecedorService {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Transactional(readOnly = true)
    public List<Fornecedor> findAll() {
        return fornecedorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Fornecedor findById(Long id) {
        return fornecedorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado com id: " + id));
    }

    public Fornecedor save(Fornecedor fornecedor) {
        return fornecedorRepository.save(fornecedor);
    }

    public Fornecedor update(Long id, Fornecedor fornecedorDetalhes) {
        Fornecedor fornecedor = findById(id);
        fornecedor.setNome(fornecedorDetalhes.getNome());
        fornecedor.setDocumento(fornecedorDetalhes.getDocumento());
        fornecedor.setTelefone(fornecedorDetalhes.getTelefone());
        fornecedor.setEndereco(fornecedorDetalhes.getEndereco());
        fornecedor.setListaProdutosFornecidos(fornecedorDetalhes.getListaProdutosFornecidos());
        fornecedor.setPrazoEntregaMedioDias(fornecedorDetalhes.getPrazoEntregaMedioDias());
        return fornecedorRepository.save(fornecedor);
    }

    public void delete(Long id) {
        if (!fornecedorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fornecedor não encontrado com id: " + id);
        }
        fornecedorRepository.deleteById(id);
    }
}