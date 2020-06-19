package kz.bitlab.intranet.services;

import kz.bitlab.intranet.entities.user.Roles;

public interface RolesService {
    Roles getOneById(Long id);
}
