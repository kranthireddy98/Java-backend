package com.cache.repository;

import com.cache.db.DatabaseConnection;
import com.cache.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserRepository {

    public User gerUserById(int id) {
        String sql = "SELECT id,name FROM Users WHERE ID=?";


        try (Connection con = DatabaseConnection.getConnection();

             PreparedStatement ps = con.prepareStatement(sql)) {

            Thread.sleep(1000);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
