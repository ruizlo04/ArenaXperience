package com.example.ApiArenaXperience.repo;

import com.example.ApiArenaXperience.model.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
}
