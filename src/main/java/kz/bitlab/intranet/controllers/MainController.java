package kz.bitlab.intranet.controllers;

import kz.bitlab.intranet.entities.user.Users;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class MainController extends BaseController{

    @GetMapping(path = "/")
    public String index(Model model){
        if(getUserData() == null){
            return "login";
        }
        return "index";
    }

    @GetMapping(path = "/login")
    public String login(Model model){
        return "login";
    }



}
