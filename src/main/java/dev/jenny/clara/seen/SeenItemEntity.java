package dev.jenny.clara.seen;

import java.time.LocalDateTime;

import dev.jenny.clara.user.UserEntity;
import jakarta.persistence.Column;
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
@Table(name = "seen_items", uniqueConstraints = @UniqueConstraint(columnNames = { "id_user", "item_key" }))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SeenItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity user;

    @Column(name = "item_key", nullable = false)
    private String itemKey;

    private LocalDateTime seenAt;
}
