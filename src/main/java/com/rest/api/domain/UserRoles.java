package com.rest.api.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@Table
public class UserRoles {

    // https://dublin-java.tistory.com/51
    // https://homoefficio.github.io/2019/04/28/JPA-%EC%9D%BC%EB%8C%80%EB%8B%A4-%EB%8B%A8%EB%B0%A9%ED%96%A5-%EB%A7%A4%ED%95%91-%EC%9E%98%EB%AA%BB-%EC%82%AC%EC%9A%A9%ED%95%98%EB%A9%B4-%EB%B2%8C%EC%96%B4%EC%A7%80%EB%8A%94-%EC%9D%BC/
    // https://coding-start.tistory.com/72

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userRoles_id;

    //@Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //@Id
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    /*
    @OneToMany(fetch = FetchType.EAGER , mappedBy = "userRoles")
    private Collection<Privileges> user_roles;
    */

    @CreationTimestamp    // 입력시 시간 정보를 자동으로 입력해는 어노테이션.
    @Column
    @Temporal(TemporalType.DATE)
    private Date orderDate;

}
