package edu.java.scrapper.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "link")
public class LinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String url;
    private String type;
    private String data;

    private OffsetDateTime lastUpdate;
    private OffsetDateTime lastCheck;

    @ManyToMany
    @JoinTable(
        name = "assignment",
        joinColumns = @JoinColumn(name = "link_id"),
        inverseJoinColumns = @JoinColumn(name = "chat_id")
    )
    private List<ChatEntity> chats;

    public LinkEntity(
        String url,
        String type,
        String data,
        OffsetDateTime lastUpdate,
        OffsetDateTime lastCheck,
        List<ChatEntity> chats
    ) {
        this.url = url;
        this.type = type;
        this.data = data;
        this.lastUpdate = lastUpdate;
        this.lastCheck = lastCheck;
        this.chats = chats;
    }
}
