package com.rest.api.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@Entity
//https://m.blog.naver.com/PostView.nhn?blogId=rorean&logNo=221593255071&proxyReferer=https:%2F%2Fwww.google.com%2F
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@Table
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long role_id;

    @OneToMany(fetch = FetchType.LAZY , mappedBy = "role" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Collection<UserRoles> role;

    @OneToMany(fetch = FetchType.LAZY , mappedBy = "role" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Collection<Privileges> privileges;

    /*@ManyToMany(mappedBy = "roles")
    private Collection<User> users;*/
/*

    @ManyToMany
    @JoinTable(name = "roles_privileges", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    private Collection<Privilege> privileges;
*/

    @Column
    private String name;

    public Role() {
        super();
    }

    public Role(final String name) {
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
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Role role = (Role) obj;
        if (!getName().equals(role.getName())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Role [name=").append(name).append("]").append("[id=").append(role_id).append("]");
        return builder.toString();
    }


    @Getter
    @Setter
    public static class Response {
        //private Long id;
        private String name;

        @Builder
        public Response(String name){
            this.name=name;
        }

    }

}