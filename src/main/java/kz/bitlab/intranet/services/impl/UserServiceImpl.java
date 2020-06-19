package kz.bitlab.intranet.services.impl;

import kz.bitlab.intranet.entities.user.Users;
import kz.bitlab.intranet.repository.UserRepository;
import kz.bitlab.intranet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableWebSecurity
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Users user = getUserByEmail(s);
        if(user!=null){
            return new User(user.getEmail(), user.getPassword(), user.getRoles());
        }else{
            return null;
        }
    }

    @Override
    public Users getUserByEmail(String email) {
        return userRepository.findByEmailAndDeletedAtNull(email);
    }

    @Override
    public List<Users> getAllUsers(Pageable pageable) {
        return userRepository.findAllByDeletedAtNullOrderById(pageable);
    }

    @Override
    public Users getOneById(Long id) {
        return userRepository.findByDeletedAtNullAndId(id);
    }
}
