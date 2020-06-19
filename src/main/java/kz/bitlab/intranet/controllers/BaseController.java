package kz.bitlab.intranet.controllers;

import kz.bitlab.intranet.entities.user.Users;
import kz.bitlab.intranet.services.RolesService;
import kz.bitlab.intranet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class BaseController {

    @Autowired
    protected UserService userService;

    public Users getUserData(){
        Users userData = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User)authentication.getPrincipal();
            userData = userService.getUserByEmail(secUser.getUsername());
        }
        return userData;
    }

}
