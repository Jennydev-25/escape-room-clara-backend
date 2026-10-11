package dev.jenny.clara.chat;

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
@Table(name = "chat_message_reads", uniqueConstraints = @UniqueConstraint(columnNames = { "id_user", "id_chat_message" }))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ChatMessageReadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "id_chat_message")
    private ChatMessageEntity message;

    private LocalDateTime readAt;
}
