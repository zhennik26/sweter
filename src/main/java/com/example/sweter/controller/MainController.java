package com.example.sweter.controller;

import com.example.sweter.domain.Message;
import com.example.sweter.domain.User;
import com.example.sweter.repos.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class MainController {
    private final MessageRepository messageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    public MainController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping
    public String home(Model model){
        return "home";
    }


    @GetMapping("/main")
    public  String main(@ModelAttribute(name = "newMessage") Message newMessage, @ModelAttribute(name = "filter") String filter, Model model){
        Iterable<Message> messages = messageRepository.findAll();
        model.addAttribute("messages",messages);
        return "main";
    }
    @PostMapping("/main")
    public String addMessage(
            @RequestParam ("file") MultipartFile file,
            @AuthenticationPrincipal User user,
            @ModelAttribute Message newMessage) throws IOException {

        if (file!=null){
            File uploadDir=new File(uploadPath);
            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }
        }
        String uID= UUID.randomUUID().toString();
        String resultFilename = uID+"."+file.getOriginalFilename();
        file.transferTo(new File(uploadPath+"/"+resultFilename));
        newMessage.setFilename(resultFilename);
        newMessage.setAuthor(user);
        messageRepository.save(newMessage);
        return "redirect:/main";
    }
    @PostMapping("/filter")
    public String filter(@ModelAttribute(name = "filter") String filter, Model model, @ModelAttribute(name = "newMessage") Message newMessage ){
        //System.out.println(filter);
        if (filter!=null&&!filter.isEmpty()){
            model.addAttribute("messages",messageRepository.findByTag(filter));
            return "main";
        }
        else {
            return "redirect:/main";
        }


    }

}
