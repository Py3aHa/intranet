package kz.bitlab.intranet.services.impl;

import kz.bitlab.intranet.entities.user.Roles;
import kz.bitlab.intranet.repository.RolesRepository;
import kz.bitlab.intranet.services.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

@Service
@EnableWebSecurity
public class RolesServiceImpl implements RolesService {

    @Autowired
    private RolesRepository rolesRepository;

    @Override
    public Roles getOneById(Long id) {
        return rolesRepository.getOne(id);
    }
}
