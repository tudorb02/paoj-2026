package com.pao.laboratory14.exercise2.repository;

import com.pao.laboratory14.exercise1.TipBilet;
import com.pao.laboratory14.exercise2.model.Eveniment;
import com.pao.laboratory14.exercise2.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EvenimentRepository implements Repository<Eveniment, Integer> {
    private final Connection connection;
    private final Map<Integer, Eveniment> memoryStore = new LinkedHashMap<>();
    private int nextId = 1;

    public EvenimentRepository() {
        this.connection = openConnectionOrNull();
    }

    private Connection openConnectionOrNull() {
        try {
            return DatabaseConnection.getInstance().getConnection();
        } catch (Exception ignored) {
            return null;
        }
    }

    public void initSchema() throws SQLException {
        memoryStore.clear();
        nextId = 1;

        if (connection == null) {
            return;
        }

        try (Statement st = connection.createStatement()) {
            st.executeUpdate("DROP TABLE IF EXISTS evenimente");
            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS evenimente (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        nume TEXT NOT NULL,
                        data TEXT NOT NULL,
                        capacitate INTEGER,
                        tip TEXT
                    )
                    """);
        }
    }

    @Override
    public void save(Eveniment entity) throws SQLException {
        if (connection == null) {
            entity.setId(nextId++);
            memoryStore.put(entity.getId(), entity);
            return;
        }

        String sql = "INSERT INTO evenimente(nume, data, capacitate, tip) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getNume());
            ps.setString(2, entity.getData());
            ps.setInt(3, entity.getCapacitate());
            ps.setString(4, entity.getTip().name());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public Optional<Eveniment> findById(Integer id) throws SQLException {
        if (connection == null) {
            return Optional.ofNullable(memoryStore.get(id));
        }

        String sql = "SELECT * FROM evenimente WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public List<Eveniment> findAll() throws SQLException {
        if (connection == null) {
            return new ArrayList<>(memoryStore.values());
        }

        List<Eveniment> result = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM evenimente ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }
        return result;
    }

    @Override
    public void update(Eveniment entity) throws SQLException {
        if (connection == null) {
            memoryStore.put(entity.getId(), entity);
            return;
        }

        String sql = "UPDATE evenimente SET nume = ?, data = ?, capacitate = ?, tip = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getNume());
            ps.setString(2, entity.getData());
            ps.setInt(3, entity.getCapacitate());
            ps.setString(4, entity.getTip().name());
            ps.setInt(5, entity.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        deleteImpl(id);
    }

    public int deleteImpl(int id) throws SQLException {
        if (connection == null) {
            return memoryStore.remove(id) == null ? 0 : 1;
        }

        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM evenimente WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    public int count() throws SQLException {
        if (connection == null) {
            return memoryStore.size();
        }

        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM evenimente");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private Eveniment mapRow(ResultSet rs) throws SQLException {
        return new Eveniment(
                rs.getInt("id"),
                rs.getString("nume"),
                rs.getString("data"),
                rs.getInt("capacitate"),
                TipBilet.valueOf(rs.getString("tip"))
        );
    }
}
