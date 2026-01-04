package com.springLearn.jdbc.repository;

import com.springLearn.jdbc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepo {

    private JdbcTemplate template;

    public void save(User user)
    {
        String sql = "insert into jdbc.users (age,name,tech) values (?,?,? )";

        int rows = template.update(sql,user.getAge(),user.getName(),user.getTech());
        System.out.println(rows + " row/s Affected");
    }

    public List<User> findAll()
    {
        String sql  =  "Select * from jdbc.users";

        RowMapper<User> mapper = new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {

                User user = new User();
                user.setId(rs.getInt(1));
                user.setName(rs.getString(2));
                user.setAge(rs.getInt(3));
                user.setTech(rs.getString(4));
                return user;
            }
        };

        return template.query(sql,mapper);
    }

    public JdbcTemplate getTemplate() {
        return template;
    }

    @Autowired
    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }
}
