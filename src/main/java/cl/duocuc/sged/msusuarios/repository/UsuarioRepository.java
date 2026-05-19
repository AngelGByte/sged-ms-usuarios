package cl.duocuc.sged.msusuarios.repository;

import cl.duocuc.sged.msusuarios.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// PATRÓN: Repository Pattern
// Abstrae el acceso a datos desacoplando la lógica de negocio de la persistencia.
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Usuario> findByRol(Usuario.Rol rol);

    List<Usuario> findByActivoTrue();
}
