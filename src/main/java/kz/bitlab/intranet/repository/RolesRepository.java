package kz.bitlab.intranet.repository;

import kz.bitlab.intranet.entities.user.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findById(Long id);
}
