package cl.duocuc.sged.msusuarios.controller;

import cl.duocuc.sged.msusuarios.dto.UsuarioDTO;
import cl.duocuc.sged.msusuarios.entity.Usuario;
import cl.duocuc.sged.msusuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "CRUD de usuarios del sistema SGED")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @Operation(summary = "Crear usuario")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioDTO.Response> crear(@Valid @RequestBody UsuarioDTO.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(request));
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios activos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'INSPECTOR')")
    public ResponseEntity<List<UsuarioDTO.Response>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'INSPECTOR', 'DOCENTE')")
    public ResponseEntity<UsuarioDTO.Response> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @GetMapping("/rol/{rol}")
    @Operation(summary = "Listar usuarios por rol")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'INSPECTOR')")
    public ResponseEntity<List<UsuarioDTO.Response>> listarPorRol(@PathVariable Usuario.Rol rol) {
        return ResponseEntity.ok(usuarioService.listarPorRol(rol));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioDTO.Response> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO.Request request) {
        return ResponseEntity.ok(usuarioService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar usuario (soft delete)")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        usuarioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
