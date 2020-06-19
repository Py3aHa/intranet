package kz.bitlab.intranet.controllers;

import kz.bitlab.intranet.entities.user.Roles;
import kz.bitlab.intranet.entities.user.Users;
import kz.bitlab.intranet.repository.UserRepository;
import kz.bitlab.intranet.services.RolesService;
import kz.bitlab.intranet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(path = "/admin")
public class AdminController extends BaseController{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesService rolesService;

    @GetMapping(path = "/users")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String users(Model model, @RequestParam(name = "page", defaultValue = "1") int page){
        int count = userRepository.countAllByDeletedAtNull();
        int tabSize = (count+9)/10;
        if(page < 1){
            page = 1;
        }
        Pageable pageable = PageRequest.of(page-1,10);
        List<Users> users = userService.getAllUsers(pageable);
        model.addAttribute("tabSize", tabSize);
        model.addAttribute("user", getUserData());
        model.addAttribute("users", users);

        return "admin/users";
    }

    @GetMapping(path = "/userdetails/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String userDetails(Model model, @PathVariable(name = "id") Long id){

        model.addAttribute("user", getUserData());
        Users userDetail = userService.getOneById(id);
        model.addAttribute("userDetail", userDetail);
        return "admin/userdetail";

    }

    @PostMapping(path = "/changeUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String changeUser(@RequestParam(name = "id") Long id, @RequestParam(name = "email") String email,
                             @RequestParam(name = "lastName") String lastName, @RequestParam(name = "firstName") String firstName,
                             @RequestParam(name = "student", required = false) Long student,
                             @RequestParam(name = "teacher", required = false) Long teacher, @RequestParam(name = "admin", required = false) Long admin){
        Roles role = new Roles();
        Set<Roles> setRoles = new HashSet<>();
        if(student != null){
            role = rolesService.getOneById(student);
            setRoles.add(role);
        }
        if(teacher != null){
            role = rolesService.getOneById(teacher);
            setRoles.add(role);
        }
        if(admin != null){
            role = rolesService.getOneById(admin);
            setRoles.add(role);
        }
        Users user = userRepository.getOne(id);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUpdatedAt(new Date());
        user.setRoles(setRoles);
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping(path = "/addNewUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addNewUserPage(){
        return "/admin/addNewUser";
    }

    @PostMapping(path = "/addUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addUser(Model model, @RequestParam(name = "email") String email, @RequestParam(name = "lastName") String lastName,
                          @RequestParam(name = "firstName") String firstName, @RequestParam(name = "student", required = false) Long student,
                          @RequestParam(name = "teacher", required = false) Long teacher, @RequestParam(name = "admin", required = false) Long admin,
                          @RequestParam(name = "password") String password, @RequestParam(name = "repass") String repass){
        String redirect = "redirect:/admin/addUser?error";
        if(userService.getUserByEmail(email) == null){
            if (password.length() > 5) {
                if(password.equals(repass)){
                    Roles role = new Roles();
                    Set<Roles> setRoles = new HashSet<>();
                    if(student != null){
                        role = rolesService.getOneById(student);
                        setRoles.add(role);
                    }
                    if(teacher != null){
                        role = rolesService.getOneById(teacher);
                        setRoles.add(role);
                    }
                    if(admin != null){
                        role = rolesService.getOneById(admin);
                        setRoles.add(role);
                    }
                    Users user = new Users(email, password, firstName, lastName, setRoles);
                    user.setCreatedAt(new Date());
                    userRepository.save(user);
                    redirect = "redirect:/admin/users";
                }
            }
        }

        return redirect;
    }

    @PostMapping(path = "/deleteUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUser(@RequestParam(name = "id") Long id){
        userRepository.delete(userService.getOneById(id));
        return "redirect:/admin/users";
    }

    @GetMapping(path = "/test")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String test(){
        return "/admin/test";
    }
}
