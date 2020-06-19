package kz.bitlab.intranet.entities.user;

import kz.bitlab.intranet.entities.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(
        name = "seq",
        sequenceName = "s_roles",
        initialValue = 1,
        allocationSize = 1
)
public class Roles extends BaseEntity implements GrantedAuthority {

    @Column(name = "role")
    private String role;

    @Column(name = "description")
    private String description;

    @Override
    public String getAuthority() {
        return this.role;
    }

    @Override
    public String toString(){
        return this.description;
    }

}
