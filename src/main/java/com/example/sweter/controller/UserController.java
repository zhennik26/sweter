package com.example.sweter.controller;

import com.example.sweter.domain.Role;
import com.example.sweter.domain.User;
import com.example.sweter.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    private final UserRepo userRepo;

    @Autowired
    public UserController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public String userList(Model model){
        model.addAttribute("userList",userRepo.findAll());
        return "userList";
    }
    @GetMapping("{user}")
    public String userEdit(@PathVariable int user, Model model){
        User thisUser =userRepo.findById(user);
        model.addAttribute("user", thisUser );
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }
    @GetMapping("delete/{user}")
    public String userDelete(@PathVariable int user){
        userRepo.deleteUserById(user);
        return "redirect:/user";
    }

    @PatchMapping
    public String userUpdate(/*@RequestParam("id") User user,*/
                             @RequestParam Map<String,String> form,
                             @RequestParam("roles") String[] roles,
                             @ModelAttribute("user") User user
                             )
    {
        User oldUser=userRepo.findById(user.getId());
        user.setPassword(oldUser.getPassword());
        user.setActive(oldUser.isActive());
        user.setRoles(Arrays.stream(roles).map(s->Role.valueOf(s)).collect(Collectors.toSet()));
        System.out.println(user.getId());
        userRepo.save(user);
        return "redirect:/user";
    }




}
