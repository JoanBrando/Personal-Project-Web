package com.oasisfashion.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oasisfashion.backend.model.PerfilDeUsuario;
import com.oasisfashion.backend.service.PerfilDeUsuarioService;

@RestController
@RequestMapping("/api/perfis")
public class PerfilDeUsuarioController {

    @Autowired
    private PerfilDeUsuarioService perfilService;

    @GetMapping
    public List<PerfilDeUsuario> listarPerfis() {
        return perfilService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilDeUsuario> buscarPerfilPorId(@PathVariable Long id) {
        return ResponseEntity.ok(perfilService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PerfilDeUsuario> criarPerfil(@RequestBody PerfilDeUsuario perfil) {
        return new ResponseEntity<>(perfilService.save(perfil), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilDeUsuario> atualizarPerfil(@PathVariable Long id, @RequestBody PerfilDeUsuario perfil) {
        return ResponseEntity.ok(perfilService.update(id, perfil));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPerfil(@PathVariable Long id) {
        perfilService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
