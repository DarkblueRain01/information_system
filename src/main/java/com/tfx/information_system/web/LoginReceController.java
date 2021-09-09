package com.tfx.information_system.web;

import com.tfx.information_system.po.User;
import com.tfx.information_system.service.UserService;
import com.tfx.information_system.util.MD5Utils;
import com.tfx.information_system.vo.RegisterInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/Reception")
public class LoginReceController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String LoginPage(){

        return "loginReception";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes){
        User user =userService.checkUser(username,password);
        if(user!=null&&(user.getType()==0)){
            user.setPassword(null);
            session.setAttribute("user",user);


            return "firstPage";
        }else{
            attributes.addFlashAttribute("message","用户名和密码错误");
            return "redirect:/Reception";
            //请求重定向无法保存model中的信息 重定向后是一个新的请求
        }

    }




    @GetMapping("/toRegis")
    public String toRegisPage(){
        return "register";
    }

    @PostMapping("/register")
    public String registration(RegisterInfo ri,RedirectAttributes attributes){
        User user = new User();
        user.setUsername(ri.getUsername());
        user.setPassword(MD5Utils.code(ri.getPassword()));
        user.setNickname(ri.getNickname());
        user.setEmail(ri.getEmail());
        User u = userService.saveUser(user);
        if(u==null){
            attributes.addFlashAttribute("message","注册失败");
            return "register";
        }else{
            return "redirect:/Reception";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/Reception";
    }

}
