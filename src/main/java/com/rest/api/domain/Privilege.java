package com.rest.api.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@Table
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long privilege_id;

    @Column
    private String name;

    // https://m.blog.naver.com/PostView.nhn?blogId=rorean&logNo=221594572152&proxyReferer=https:%2F%2Fwww.google.com%2F
    @OneToMany(fetch = FetchType.LAZY , mappedBy = "privilege")
    private Collection<Privileges> privileges;

    /*
    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;
    */

    public Privilege() {
        super();
    }

    public Privilege(final String name) {
        super();
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Privilege other = (Privilege) obj;
        if (getName() == null) {
            if (other.getName() != null)
                return false;
        } else if (!getName().equals(other.getName()))
            return false;
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Privilege [name=").append(name).append("]").append("[id=").append(privilege_id).append("]");
        return builder.toString();
    }
}
