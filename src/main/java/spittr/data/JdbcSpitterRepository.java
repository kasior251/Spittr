package spittr.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import spittr.Spitter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcSpitterRepository implements SpitterRepository {

    private JdbcOperations jdbc;

    @Autowired
    public JdbcSpitterRepository(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Spitter save(Spitter spitter) {
        jdbc.update(
                "insert into Spitter(firstname, lastname, email, username, password)" +
                        " values (?, ?, ?, ?, ?)",
                spitter.getFirstname(),
                spitter.getLastname(),
                spitter.getEmail(),
                spitter.getUsername(),
                spitter.getPassword()
        );
        return spitter;
    }

    @Override
    public Spitter findByUsername(String username) {
        return jdbc.queryForObject(
                "select id, firstname, lastname, email, username, password" +
                        " from Spitter" +
                        " where username = ?",
                        new SpitterRowMapper(),
                        username);
    }

    private static class SpitterRowMapper implements RowMapper<Spitter> {
        public Spitter mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Spitter(
                    rs.getLong("id"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("email"),
                    rs.getString("username"),
                    null
            );
        }
    }
}
