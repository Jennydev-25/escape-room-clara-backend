package dev.jenny.clara.email;

import java.time.LocalDateTime;

import dev.jenny.clara.user.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "email_reads", uniqueConstraints = @UniqueConstraint(columnNames = { "id_user", "id_email" }))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmailReadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "id_email")
    private EmailEntity email;

    private LocalDateTime readAt;
}
