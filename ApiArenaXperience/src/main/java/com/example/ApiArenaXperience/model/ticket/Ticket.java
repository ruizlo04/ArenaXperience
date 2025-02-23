package com.example.ApiArenaXperience.model.ticket;

import com.example.ApiArenaXperience.model.event.Evento;
import com.example.ApiArenaXperience.model.user.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ticket_entity")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Usuario user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Evento event;

}
