package dev.jenny.clara.puzzle;

import dev.jenny.clara.folder.FolderEntity;
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
@Table(name = "puzzles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PuzzleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_folder")
    private FolderEntity folder;

    @Enumerated(EnumType.STRING)
    private PuzzleType type;

    private int minChapter;
    private int displayOrder;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String hint1;

    @Column(columnDefinition = "TEXT")
    private String hint2;

    @Column(columnDefinition = "TEXT")
    private String hint3;
}
