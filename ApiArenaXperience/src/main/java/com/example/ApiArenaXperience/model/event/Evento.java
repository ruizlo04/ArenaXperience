package com.example.ApiArenaXperience.model.event;

import com.example.ApiArenaXperience.model.user.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "event_entity")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private LocalDate date;
    private int capacity;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Usuario createdBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_attendees",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Usuario> attendees = new HashSet<>();

    public void addAttendee(Usuario usuario) {
        attendees.add(usuario);
        usuario.getEvents().add(this);
    }

    public void removeAttendee(Usuario usuario) {
        attendees.remove(usuario);
        usuario.getEvents().remove(this);
    }
}
