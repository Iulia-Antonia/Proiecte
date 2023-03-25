package com.example.SocialNettwork;


import com.example.SocialNettwork.Domain.Friendship;
import com.example.SocialNettwork.Domain.Tuple;
import com.example.SocialNettwork.Domain.User;
import com.example.SocialNettwork.Domain.validators.FriendshipValidator;
import com.example.SocialNettwork.Domain.validators.UserValidator;
import com.example.SocialNettwork.Domain.validators.Validator;
import com.example.SocialNettwork.Repository.RepoInMemory;
import com.example.SocialNettwork.Repository.Repository;
import com.example.SocialNettwork.Service.FriendshipService;
import com.example.SocialNettwork.Service.UserService;
import com.example.SocialNettwork.UserInterface.UserInterface;

public class Main {
    public static void main(String[] args) {
        Validator<User> validatorU = new UserValidator();
        Validator<Friendship> validatorF = new FriendshipValidator();
        Repository<Long, User> repoUser = new RepoInMemory<Long, User>(validatorU);
        Repository<Tuple<Long,Long>,Friendship> repoFriendship = new RepoInMemory<Tuple<Long, Long>, Friendship>(validatorF);
        UserService userService = new UserService(repoUser);
        FriendshipService friendshipService = new FriendshipService(repoFriendship,repoUser);
        UserInterface ui = new UserInterface(userService,friendshipService);
        ui.runMenu();
    }
}