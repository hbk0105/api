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
@IdClass(UserRolesId.class)
public class UserRoles {


    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userRoles_id;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @CreationTimestamp    // 입력시 시간 정보를 자동으로 입력해는 어노테이션.
    @Column
    @Temporal(TemporalType.DATE)
    private Date orderDate;

}
