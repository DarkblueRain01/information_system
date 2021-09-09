package com.tfx.information_system.web;

import com.tfx.information_system.dao.UserRepository;
import com.tfx.information_system.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value="/{id}",produces="application/json;charset=UTF-8")
    public Optional<User> findById(@PathVariable Integer id){
        return this.userRepository.findById(Long.valueOf(id));
    }



}
