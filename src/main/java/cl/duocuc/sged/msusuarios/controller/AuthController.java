package cl.duocuc.sged.msusuarios.controller;

import cl.duocuc.sged.msusuarios.dto.UsuarioDTO;
import cl.duocuc.sged.msusuarios.entity.Usuario;
import cl.duocuc.sged.msusuarios.repository.UsuarioRepository;
import cl.duocuc.sged.msusuarios.security.JwtUtil;
import cl.duocuc.sged.msusuarios.security.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Login y generación de token JWT")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y retorna un token JWT")
    public ResponseEntity<?> login(@Valid @RequestBody UsuarioDTO.LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtUtil.generateToken(userDetails, usuario.getRol().name());

        return ResponseEntity.ok(new UsuarioDTO.LoginResponse(
            token,
            usuario.getId(),
            usuario.getNombre() + " " + usuario.getApellido(),
            usuario.getEmail(),
            usuario.getRol().name()
        ));
    }
}
