package cl.duocuc.sged.msusuarios.service;

import cl.duocuc.sged.msusuarios.dto.UsuarioDTO;
import cl.duocuc.sged.msusuarios.entity.Usuario;
import cl.duocuc.sged.msusuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// PATRÓN: Service Layer
// Centraliza la lógica de negocio separándola del controlador y del repositorio.
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioDTO.Response crear(UsuarioDTO.Request request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado: " + request.getEmail());
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(request.getRol())
                .activo(true)
                .build();

        return toResponse(usuarioRepository.save(usuario));
    }

    public List<UsuarioDTO.Response> listar() {
        return usuarioRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UsuarioDTO.Response buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        return toResponse(usuario);
    }

    public UsuarioDTO.Response buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));
        return toResponse(usuario);
    }

    public List<UsuarioDTO.Response> listarPorRol(Usuario.Rol rol) {
        return usuarioRepository.findByRol(rol)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UsuarioDTO.Response actualizar(Long id, UsuarioDTO.Request request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setRol(request.getRol());

        return toResponse(usuarioRepository.save(usuario));
    }

    public void desactivar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    // PATRÓN: DTO - convierte entidad a respuesta sin exponer campos sensibles
    private UsuarioDTO.Response toResponse(Usuario usuario) {
        UsuarioDTO.Response response = new UsuarioDTO.Response();
        response.setId(usuario.getId());
        response.setNombre(usuario.getNombre());
        response.setApellido(usuario.getApellido());
        response.setEmail(usuario.getEmail());
        response.setRol(usuario.getRol());
        response.setActivo(usuario.getActivo());
        return response;
    }
}
