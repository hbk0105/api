package com.rest.api.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@Table
public class Privileges {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long privileges_id;

    @ManyToOne
    @JoinColumn(name = "privilege_id")
    private Privilege privilege;

    @ManyToOne
    @JoinColumn(name = "userRoles_id")
    private Role role;

    /*
    @ManyToOne
    @JoinColumn(name = "userRoles_id")
    private UserRoles userRoles;
    */

    @CreationTimestamp    // 입력시 시간 정보를 자동으로 입력해는 어노테이션.
    @Column
    @Temporal(TemporalType.DATE)
    private Date orderDate;

}
