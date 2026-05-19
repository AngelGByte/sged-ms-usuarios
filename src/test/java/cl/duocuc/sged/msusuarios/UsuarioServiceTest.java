package cl.duocuc.sged.msusuarios;

import cl.duocuc.sged.msusuarios.dto.UsuarioDTO;
import cl.duocuc.sged.msusuarios.entity.Usuario;
import cl.duocuc.sged.msusuarios.repository.UsuarioRepository;
import cl.duocuc.sged.msusuarios.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioMock;
    private UsuarioDTO.Request requestMock;

    @BeforeEach
    void setUp() {
        usuarioMock = Usuario.builder()
                .id(1L)
                .nombre("Angel")
                .apellido("Venegas")
                .email("angel@colegio.cl")
                .password("encoded123")
                .rol(Usuario.Rol.DOCENTE)
                .activo(true)
                .build();

        requestMock = new UsuarioDTO.Request();
        requestMock.setNombre("Angel");
        requestMock.setApellido("Venegas");
        requestMock.setEmail("angel@colegio.cl");
        requestMock.setPassword("123456");
        requestMock.setRol(Usuario.Rol.DOCENTE);
    }

    @Test
    void crear_deberiaRetornarUsuario_cuandoEmailNoExiste() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded123");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);

        UsuarioDTO.Response response = usuarioService.crear(requestMock);

        assertNotNull(response);
        assertEquals("Angel", response.getNombre());
        assertEquals("angel@colegio.cl", response.getEmail());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void crear_deberiaLanzarExcepcion_cuandoEmailYaExiste() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> usuarioService.crear(requestMock));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void buscarPorId_deberiaRetornarUsuario_cuandoExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));

        UsuarioDTO.Response response = usuarioService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(Usuario.Rol.DOCENTE, response.getRol());
    }

    @Test
    void buscarPorId_deberiaLanzarExcepcion_cuandoNoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.buscarPorId(99L));
    }

    @Test
    void listar_deberiaRetornarListaDeUsuariosActivos() {
        when(usuarioRepository.findByActivoTrue()).thenReturn(List.of(usuarioMock));

        List<UsuarioDTO.Response> resultado = usuarioService.listar();

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getActivo());
    }

    @Test
    void desactivar_deberiaDesactivarUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));
        when(usuarioRepository.save(any())).thenReturn(usuarioMock);

        usuarioService.desactivar(1L);

        assertFalse(usuarioMock.getActivo());
        verify(usuarioRepository, times(1)).save(usuarioMock);
    }
}
