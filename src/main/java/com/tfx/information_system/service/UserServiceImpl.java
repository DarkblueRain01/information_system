package com.tfx.information_system.service;



import com.tfx.information_system.dao.UserRepository;
import com.tfx.information_system.po.User;
import com.tfx.information_system.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("UserService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Value("${default.avatar}")
    private String avatar;

    @Override
    public User checkUser(String username, String password) {
        User user=userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }

    @Override
    public User saveUser(User user) {
        user.setAvatar(avatar);
        user.setCreateTime(new Date());
        user.setType(0);
        user.setUpdateTime(new Date());
        return userRepository.save(user);
    }
}
