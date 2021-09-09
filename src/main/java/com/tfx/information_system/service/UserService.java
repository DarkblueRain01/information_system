package com.tfx.information_system.service;



import com.tfx.information_system.po.User;

public interface UserService {
    User checkUser(String username, String password);

    User saveUser(User user);
}
