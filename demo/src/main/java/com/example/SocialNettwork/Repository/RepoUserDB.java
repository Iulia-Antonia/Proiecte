package com.example.SocialNettwork.Repository;


import com.example.SocialNettwork.Domain.User;
import com.example.SocialNettwork.Domain.validators.Validator;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RepoUserDB implements Repository<Long, User>{
    private String url;
    private String userName;
    private String password;
    private Validator<User> validator;

    public RepoUserDB(String url, String userName, String password, Validator<User> validator){
        this.url=url;
        this.userName=userName;
        this.password=password;
        this.validator=validator;
    }

    @Override
    public Optional<User> getOne(Long id){
        try(Connection connection = DriverManager.getConnection(url,userName,password);
            PreparedStatement statement = connection.prepareStatement("SELECT * from Users");
            ResultSet resultSet = statement.executeQuery();){
            while(resultSet.next()){
                Long id_user = resultSet.getLong("Id");
                if(id_user.equals(id)){
                    String firstName = resultSet.getString("FirstName");
                    String lastName = resultSet.getString("LastName");
                    String cod = resultSet.getString("Cod");

                    User u = new User(firstName,lastName,cod);
                    u.setId(id_user);
                    return Optional.ofNullable(u);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public Iterable<User> getAll(){
        Set<User> users = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url,userName,password);
            PreparedStatement statement = connection.prepareStatement("SELECT * from Users");
            ResultSet resultSet = statement.executeQuery()){

            while(resultSet.next()){
                Long id = resultSet.getLong("Id");
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                String cod = resultSet.getString("Cod");

                User user = new User(firstName,lastName,cod);
                user.setId(id);
                users.add(user);
            }
            return users;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }
    @Override
    public Optional<User> save(User entity){
        String sql = "insert into Users(FirstName,LastName) values (?, ?)";
        try (Connection connection = DriverManager.getConnection(url,userName,password);
             PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1,entity.getFirstName());
            ps.setString(2,entity.getLastName());

            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public Optional<User> delete(Long id){
        String sql = "DELETE from Users where Id = ?";
        try (Connection connection = DriverManager.getConnection(url,userName,password);
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setLong(1,id);
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public Optional<User> update(User entity){
        String sql1 = "UPDATE Users SET FirstName = ? WHERE Id = ?";
        String sql2 = "UPDATE Users SET LasttName = ? WHERE Id = ?";
        try (Connection connection = DriverManager.getConnection(url,userName,password);
             PreparedStatement ps1 = connection.prepareStatement(sql1);
             PreparedStatement ps2 = connection.prepareStatement(sql2)){

            ps1.setString(1,entity.getFirstName());
            ps1.setLong(2,entity.getID());
            ps2.setString(1,entity.getFirstName());
            ps2.setLong(2,entity.getID());

            ps1.executeUpdate();
            ps2.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int getSize() {
        Set<User> users = new HashSet<>();
//        String sql = "SELECT COUNT(Id) FROM Users";
//        try(Connection connection = DriverManager.getConnection(url,userName,password);
//            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(Id) FROM Users");
//            int nr = statement.getUpdateCount()){
        try(Connection connection = DriverManager.getConnection(url,userName,password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users");
            ResultSet resultSet = statement.executeQuery()){

            int nr=0;
            while(resultSet.next()){nr++;}
            return nr;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

}

