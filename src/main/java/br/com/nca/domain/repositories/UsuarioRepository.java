package br.com.nca.domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.nca.domain.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID>{

    @Query("""
            SELECT u FROM Usuario u
            WHERE u.email = :pEmail
            """)
    Usuario findByUserEmail(@Param("pEmail") String email);
}
