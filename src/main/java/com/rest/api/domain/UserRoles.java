package com.rest.api.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@Table
public class UserRoles {

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
