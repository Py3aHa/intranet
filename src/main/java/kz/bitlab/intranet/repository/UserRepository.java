package kz.bitlab.intranet.repository;

import kz.bitlab.intranet.entities.user.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    int countAllByDeletedAtNull();
    Users findByEmailAndDeletedAtNull(String email);
    List<Users> findAllByDeletedAtNullOrderById(Pageable pageable);
    Users findByDeletedAtNullAndId(Long id);
}
