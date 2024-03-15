package edu.java.scrapper.domain.mappers;

import edu.java.scrapper.domain.dto.Link;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import org.springframework.jdbc.core.RowMapper;

public class LinkMapper implements RowMapper<Link> {

    @Override
    public Link mapRow(ResultSet rs, int rowNum) {
        try {
            return new Link(
                rs.getInt("id"),
                new URI(rs.getString("url")),
                rs.getTimestamp("last_update").toLocalDateTime().atOffset(ZoneOffset.UTC),
                rs.getTimestamp("last_check").toLocalDateTime().atOffset(ZoneOffset.UTC)
            );
        } catch (URISyntaxException | SQLException e) {
            return null;
        }
    }
}
