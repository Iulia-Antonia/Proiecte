package com.example.SocialNettwork.Repository;

import com.example.SocialNettwork.Domain.Friendship;
import com.example.SocialNettwork.Domain.Status;
import com.example.SocialNettwork.Domain.Tuple;
import com.example.SocialNettwork.Domain.validators.Validator;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RepoFriendshipDB implements Repository<Tuple<Long,Long>, Friendship> {
    private String url;
    private String userName;
    private String password;
    private Validator<Friendship> validator;

    public RepoFriendshipDB(String url, String userName, String password, Validator<Friendship> validator){
        this.url=url;
        this.userName=userName;
        this.password=password;
        this.validator=validator;
    }

    @Override
    public Optional<Friendship> getOne(Tuple<Long,Long> id){
        try(Connection connection = DriverManager.getConnection(url,userName,password);
            PreparedStatement statement = connection.prepareStatement("SELECT * from Friendships");
            ResultSet resultSet = statement.executeQuery();){
            while(resultSet.next()){
                Long id1 = resultSet.getLong("IdFriend1");
                Long id2 = resultSet.getLong("IdFriend2");
                Status status = Status.valueOf(resultSet.getString("Status"));
                Tuple<Long,Long> f = new Tuple<>(id1,id2);
                if(f.equals(id)){
                    LocalDate data = resultSet.getDate("Date").toLocalDate();
                    Friendship friendship = new Friendship(data,f,status);
                    return Optional.ofNullable(friendship);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public Iterable<Friendship> getAll(){
        Set<Friendship> friendships = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url,userName,password);
            PreparedStatement statement = connection.prepareStatement("SELECT * from Friendships");
            ResultSet resultSet = statement.executeQuery()){

            while(resultSet.next()){
                Long id1 = resultSet.getLong("IdFriend1");
                Long id2 = resultSet.getLong("IdFriend2");
                Status status = Status.valueOf(resultSet.getString("Status"));
                Tuple<Long,Long> f = new Tuple<>(id1,id2);
                LocalDate data = resultSet.getDate("Date").toLocalDate();
                Friendship friendship = new Friendship(data,f,status);
                friendships.add(friendship);
            }
            return friendships;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return friendships;
    }
    @Override
    public Optional<Friendship> save(Friendship entity){
        String sql = "insert into Friendships(IdFriend1, IdFriend2, Date, Status) values (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url,userName,password);
             PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setLong(1,entity.getID().getLeft());
            ps.setLong(2,entity.getID().getRight());
            ps.setDate(3, Date.valueOf(entity.getDate()));
            ps.setString(4,entity.getStatus_prietenie().toString());

            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public Optional<Friendship> delete(Tuple<Long,Long> id){
        String sql = "DELETE from Friendships where (IdFrined1 = ? and IdFriend2 = ?) or (IdFrined2 = ? and IdFriend1 = ?)";
        try (Connection connection = DriverManager.getConnection(url,userName,password);
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setLong(1,id.getRight());
            statement.setLong(2,id.getLeft());
            statement.setLong(4,id.getRight());
            statement.setLong(3,id.getLeft());
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public Optional<Friendship> update(Friendship entity){
        //NU PREA E NEVOIE DE UPDATE PENTRU O PRIETENIE, DEOARECE DATA TREBUIE SA FIE CEA LA CARE S-A CREAT PRIETENIA
//        String sql1 = "UPDATE Friendships SET FirstName = ? WHERE Id = ?";
//        String sql2 = "UPDATE Friendships SET LasttName = ? WHERE Id = ?";
//        try (Connection connection = DriverManager.getConnection(url,userName,password);
//             PreparedStatement ps1 = connection.prepareStatement(sql1);
//             PreparedStatement ps2 = connection.prepareStatement(sql2)){
//
//            ps1.setString(1,entity.getFirstName());
//            ps1.setLong(2,entity.getID());
//            ps2.setString(1,entity.getFirstName());
//            ps2.setLong(2,entity.getID());
//
//            ps1.executeUpdate();
//            ps2.executeUpdate();
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
        return Optional.empty();
    }
    @Override
    public int getSize() {
        Set<Friendship> friendships = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url,userName,password);
            PreparedStatement statement = connection.prepareStatement("SELECT * from Friendships");
            ResultSet resultSet = statement.executeQuery()){

            return resultSet.getFetchSize();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
}

