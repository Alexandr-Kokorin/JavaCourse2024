package edu.java.scrapper.domain.mappers;

import edu.java.scrapper.domain.dto.Chat;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import org.springframework.jdbc.core.RowMapper;

public class ChatMapper implements RowMapper<Chat> {

    @Override
    public Chat mapRow(ResultSet rs, int rowNum) {
        try {
            return new Chat(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("state"),
                rs.getTimestamp("created_at").toLocalDateTime().atOffset(ZoneOffset.UTC)
            );
        } catch (SQLException e) {
            return null;
        }
    }
}
