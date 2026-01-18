package com.todolist.dao;

import com.todolist.entities.Task;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    private final String url = "jdbc:mysql://localhost:3306/todo_db";
    private final String username = "root";
    private final String password = ""; 

    private Connection getConnection() throws SQLException {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (Exception e) {}
        return DriverManager.getConnection(url, username, password);
    }

    public List<Task> findTasks(Long userId, String searchTerm, String statusFilter) {
        List<Task> tasks = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM task WHERE user_id = ?");
        
        if (searchTerm != null && !searchTerm.isEmpty()) sql.append(" AND description LIKE ?");
        if (statusFilter != null && !statusFilter.equals("ALL")) sql.append(" AND status = ?");

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            ps.setLong(idx++, userId);
            if (searchTerm != null && !searchTerm.isEmpty()) ps.setString(idx++, "%" + searchTerm + "%");
            if (statusFilter != null && !statusFilter.equals("ALL")) ps.setString(idx++, statusFilter);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Task t = new Task();
                t.setId(rs.getLong("id"));
                t.setDescription(rs.getString("description"));
                t.setStatus(rs.getString("status"));
                tasks.add(t);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return tasks;
    }

    public int countTasks(Long userId, String status) {
        String sql = (status.equals("ALL")) ? 
            "SELECT COUNT(*) FROM task WHERE user_id = ?" : 
            "SELECT COUNT(*) FROM task WHERE user_id = ? AND status = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            if (!status.equals("ALL")) ps.setString(2, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public void toggleStatus(Long taskId, String currentStatus) {
        String newStatus = currentStatus.equals("DONE") ? "PENDING" : "DONE";
        String sql = "UPDATE task SET status = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setLong(2, taskId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void delete(Long taskId) {
        String sql = "DELETE FROM task WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, taskId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void save(Task task) {
        String sql = "INSERT INTO task (description, status, user_id) VALUES (?, 'PENDING', ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, task.getDescription());
            ps.setLong(2, task.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}