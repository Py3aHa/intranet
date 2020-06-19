package kz.bitlab.intranet.services;

import kz.bitlab.intranet.entities.user.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    Users getUserByEmail(String email);
    List<Users> getAllUsers(Pageable pageable);
    Users getOneById(Long id);
}
