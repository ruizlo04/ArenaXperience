package com.example.ApiArenaXperience.repo;

import com.example.ApiArenaXperience.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Usuario, UUID>,
        JpaSpecificationExecutor<Usuario> {

    Optional<Usuario> findFirstByUsername(String username);

    Optional<Usuario> findByActivationToken(String activationToken);

    boolean existsByUsername(String username);
}
