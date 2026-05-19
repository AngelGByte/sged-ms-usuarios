package cl.duocuc.sged.msusuarios.dto;

import cl.duocuc.sged.msusuarios.entity.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// PATRÓN: DTO (Data Transfer Object)
// Evita exponer la entidad directamente y permite validar la entrada de datos.

public class UsuarioDTO {

    @Data
    public static class Request {
        @NotBlank(message = "El nombre es obligatorio")
        private String nombre;

        @NotBlank(message = "El apellido es obligatorio")
        private String apellido;

        @Email(message = "El email debe ser válido")
        @NotBlank(message = "El email es obligatorio")
        private String email;

        @NotBlank(message = "La contraseña es obligatoria")
        private String password;

        @NotNull(message = "El rol es obligatorio")
        private Usuario.Rol rol;
    }

    @Data
    public static class Response {
        private Long id;
        private String nombre;
        private String apellido;
        private String email;
        private Usuario.Rol rol;
        private Boolean activo;
    }

    @Data
    public static class LoginRequest {
        @Email
        @NotBlank
        private String email;

        @NotBlank
        private String password;
    }

    @Data
    public static class LoginResponse {
        private String token;
        private String tipo = "Bearer";
        private Long id;
        private String nombre;
        private String email;
        private String rol;

        public LoginResponse(String token, Long id, String nombre, String email, String rol) {
            this.token = token;
            this.id = id;
            this.nombre = nombre;
            this.email = email;
            this.rol = rol;
        }
    }
}
