package com.oasisfashion.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oasisfashion.backend.exception.ResourceNotFoundException;
import com.oasisfashion.backend.model.Cliente;
import com.oasisfashion.backend.repository.ClienteRepository;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Cliente findById(Long id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + id));
    }

    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente update(Long id, Cliente clienteDetalhes) {
        Cliente cliente = findById(id);
        cliente.setNome(clienteDetalhes.getNome());
        cliente.setDocumento(clienteDetalhes.getDocumento());
        cliente.setTelefone(clienteDetalhes.getTelefone());
        cliente.setEndereco(clienteDetalhes.getEndereco());
        cliente.setLimiteCredito(clienteDetalhes.getLimiteCredito());
        cliente.setHistoricoCompras(clienteDetalhes.getHistoricoCompras());
        return clienteRepository.save(cliente);
    }

    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado com id: " + id);
        }
        clienteRepository.deleteById(id);
    }
}