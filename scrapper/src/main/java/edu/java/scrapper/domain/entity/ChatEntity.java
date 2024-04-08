package edu.java.scrapper.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat")
public class ChatEntity {

    @Id
    private long id;

    private String name;
    private String state;

    private OffsetDateTime createdAt;

    @ManyToMany(mappedBy = "chats")
    private List<LinkEntity> links;

    public ChatEntity(long id, String name, String state, OffsetDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.createdAt = createdAt;
        links = new ArrayList<>();
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChatEntity that = (ChatEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
