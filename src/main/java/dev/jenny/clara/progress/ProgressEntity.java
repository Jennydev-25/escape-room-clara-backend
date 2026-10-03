package dev.jenny.clara.progress;

import java.time.LocalDateTime;

import dev.jenny.clara.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "progress")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProgressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_user", unique = true)
    private UserEntity user;

    private int currentChapter;
    private String hudLetters;
    private LocalDateTime updatedAt;
    private boolean investigationSubmitted;
    private Long timeSpentSeconds;

    @Column(columnDefinition = "TEXT")
    private String freeNote;
}
