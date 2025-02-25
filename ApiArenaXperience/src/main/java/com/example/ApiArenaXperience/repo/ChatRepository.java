package com.example.ApiArenaXperience.repo;

import com.example.ApiArenaXperience.model.chat.Chat;
import com.example.ApiArenaXperience.model.user.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
    Page<Chat> findBySenderOrReceiver(Usuario sender, Usuario receiver, Pageable pageable);

}
