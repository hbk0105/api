package com.rest.api.concurrency;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@NoArgsConstructor
@Getter
@Setter
@Entity
// https://velog.io/@conatuseus/%EC%97%94%ED%8B%B0%ED%8B%B0-%EB%A7%A4%ED%95%91-2-msk0kq84v5
@SequenceGenerator(
        name = "RESERVATION_SEQ_GENERATOR",
        sequenceName = "RESERVATION_SEQ", //매핑할 데이터베이스 시퀀스 이름
        initialValue = 1, allocationSize = 50)
@Table
@Data
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "RESERVATION_SEQ_GENERATOR")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "res_id")
    private Long id;

    @Column(name="no")
    private Long no;

}
