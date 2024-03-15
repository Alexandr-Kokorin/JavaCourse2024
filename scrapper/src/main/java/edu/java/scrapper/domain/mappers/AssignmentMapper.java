package edu.java.scrapper.domain.mappers;

import edu.java.scrapper.domain.dto.Assignment;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class AssignmentMapper implements RowMapper<Assignment> {

    @Override
    public Assignment mapRow(ResultSet rs, int rowNum) {
        try {
            return new Assignment(
                rs.getInt("chat_id"),
                rs.getInt("link_id")
            );
        } catch (SQLException e) {
            return null;
        }
    }
}
