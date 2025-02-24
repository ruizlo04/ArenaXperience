package com.example.ApiArenaXperience.repo;

import com.example.ApiArenaXperience.model.user.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Usuario, UUID>,
        JpaSpecificationExecutor<Usuario> {

    Optional<Usuario> findFirstByUsername(String username);

    Optional<Usuario> findByActivationToken(String activationToken);

    boolean existsByUsername(String username);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.events WHERE u.id = :id")
    Optional<Usuario> findByIdWithEvents(@Param("id") UUID id);

    @Query("SELECT u FROM Usuario u JOIN FETCH u.tickets WHERE u.username = :username")
    Optional<Usuario> findByUsernameWithTickets(@Param("username") String username);
}
