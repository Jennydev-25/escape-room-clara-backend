package dev.jenny.clara.chat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_messages")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ChatMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contact;
    private String character;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int minChapter;
    private int displayOrder;
}
