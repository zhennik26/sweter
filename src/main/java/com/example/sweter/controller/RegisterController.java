package com.example.sweter.controller;

import com.example.sweter.domain.Role;
import com.example.sweter.domain.User;
import com.example.sweter.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Controller
//@RequestMapping("/register")
public class RegisterController {

    private final UserRepo userRepo;
    @Autowired
    public RegisterController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/register")
    public String register(@ModelAttribute(name = "user") User user, Model model){
        System.out.println("GET");
        return "register";
    }

    @PostMapping("/register")
    public String addUser(@ModelAttribute User user, Model model){
        System.out.println("POST");
        User userFromDB=userRepo.findByUsername(user.getUsername());
        if (userFromDB!=null){
            model.addAttribute("Message","user is exist");
            return "register";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);
        return "redirect:/login";
    }
}
