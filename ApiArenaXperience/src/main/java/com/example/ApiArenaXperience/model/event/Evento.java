package com.example.ApiArenaXperience.model.event;

import com.example.ApiArenaXperience.model.ticket.Ticket;
import com.example.ApiArenaXperience.model.user.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
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
    private double price;

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

    @OneToMany(mappedBy = "event",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Ticket> tickets = new HashSet<>();

    private String file;

    public void addAttendee(Usuario usuario) {
        attendees.add(usuario);
        usuario.getEvents().add(this);
    }

    public void removeAttendee(Usuario usuario) {
        attendees.remove(usuario);
        usuario.getEvents().remove(this);
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
        ticket.setEvent(this);
    }

    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
        ticket.setEvent(null);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Evento evento = (Evento) o;
        return getId() != null && Objects.equals(getId(), evento.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
