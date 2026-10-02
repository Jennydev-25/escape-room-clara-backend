package dev.jenny.clara.contact;

import java.time.LocalDateTime;

import dev.jenny.clara.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contact_messages")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ContactMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contact_message")
    private Long id;

    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private ContactType type;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity user;
}