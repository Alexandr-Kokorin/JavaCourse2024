package edu.java.scrapper.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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

    @ManyToMany
    @JoinTable(
        name = "assignment",
        joinColumns = @JoinColumn(name = "chat_id"),
        inverseJoinColumns = @JoinColumn(name = "link_id")
    )
    private Set<LinkEntity> links = new HashSet<>();

    public ChatEntity(long id, String name, String state, OffsetDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.createdAt = createdAt;
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
