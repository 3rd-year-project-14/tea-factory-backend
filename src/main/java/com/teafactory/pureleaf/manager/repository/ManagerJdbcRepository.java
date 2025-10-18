package com.teafactory.pureleaf.manager.repository;

import com.teafactory.pureleaf.manager.dto.ManagerInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ManagerJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public ManagerJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<ManagerInfo> ROW_MAPPER = new RowMapper<>() {
        @Override
        public ManagerInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            ManagerInfo m = new ManagerInfo();
            m.setId(rs.getLong("id"));
            m.setEmail(rs.getString("email"));
            m.setName(rs.getString("name"));
            m.setRole(rs.getString("role"));
            long factoryId = rs.getLong("factory_id");
            if (rs.wasNull()) {
                m.setFactoryId(null);
            } else {
                m.setFactoryId(factoryId);
            }
            return m;
        }
    };

    public List<ManagerInfo> findAllManagers() {
        String sql = "SELECT id, email, name, role, factory_id FROM users WHERE role IN (?,?,?,?)";
        return jdbcTemplate.query(sql, ROW_MAPPER,
                "FACTORY_MANAGER",
                "INVENTORY_MANAGER",
                "FERTILIZER_MANAGER",
                "TRANSPORT_MANAGER");
    }

    public List<ManagerInfo> findManagersByFactoryId(Long factoryId) {
        String sql = "SELECT id, email, name, role, factory_id FROM users WHERE factory_id = ? AND role IN (?,?,?,?)";
        return jdbcTemplate.query(sql, ROW_MAPPER,
                factoryId,
                "FACTORY_MANAGER",
                "INVENTORY_MANAGER",
                "FERTILIZER_MANAGER",
                "TRANSPORT_MANAGER");
    }
}

