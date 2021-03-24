package com.rest.api.domain;

import com.fasterxml.jackson.annotation.*;
import com.mysema.commons.lang.Assert;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.jboss.aerogear.security.otp.api.Base32;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@Entity
@ToString
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class User {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long user_id;

    private String firstName;

    private String lastName;

    private String email;

    @Column(length = 60)
    private String password;

    private boolean enabled;

    private boolean isUsing2FA;

    private String secret;

    @ColumnDefault("false") //default false
    private boolean mailCertification;

    @Column
    private LocalDateTime mailCertificationtDate;

    /*@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;*/

    @OneToMany(fetch = FetchType.EAGER , mappedBy = "user")
    private Collection<UserRoles> roles;


    public User() {
        super();
        this.secret = Base32.random();
        this.enabled = false;
    }

    public User(String s, String s1, String facebook) {
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((getEmail() == null) ? 0 : getEmail().hashCode());
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
        final User user = (User) obj;
        if (!getEmail().equals(user.getEmail())) {
            return false;
        }
        return true;
    }

  /*  @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("User [id=")
                .append(id)
                .append(", firstName=").append(firstName)
                .append(", lastName=").append(lastName)
                .append(", email=").append(email)
                .append(", mailCertification=").append(mailCertification)
                .append(", mailCertificationDate=").append(mailCertificationtDate)
                .append(", enabled=").append(enabled)
                .append(", isUsing2FA=").append(isUsing2FA)
                .append(", secret=").append(secret)
                .append(", roles=").append(roles)
                .append("]");
        return builder.toString();
    }
*/
    @Getter
    @Setter
    public static class Request {
        private String email;
        private String firstName;
        private String lastName;
        private String password;
        private boolean mailCertification;
        private boolean enabled;
    }

    @Getter
    @Setter
    public static class Response {
        //private Long id;
        private String email;
        private String firstName;
        private String lastName;
        private Collection<Role.Response> roles;

        // 안전한 객채 생성 패턴
        @Builder
        public Response(String email , String firstName , String lastName , Collection<Role.Response> roles) {
            //Assert.hasText(String.valueOf(id), "id must not be empty");
            Assert.hasText(email, "email must not be empty");
            Assert.hasText(firstName, "firstName must not be empty");
            Assert.hasText(lastName, "lastName must not be empty");
            Assert.hasText(String.valueOf(roles), "roles must not be empty");

            //this.id = id;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.roles = roles;
        }

    }


    @Builder
    public User(String email, String firstName, String lastName ,String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

}

