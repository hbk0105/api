package com.rest.api.repository;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rest.api.concurrency.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.rest.api.domain.QBoard.board;
import static com.rest.api.concurrency.QReservation.reservation;


@RequiredArgsConstructor
@Repository
public class ReservationQueryRepository {

    private final JPAQueryFactory queryFactory;

    // https://lelecoder.com/145
    // https://velog.io/@ljinsk3/Querydsl-4
    @Autowired
    EntityManager em;

    public long findById(Reservation r){
        long total = queryFactory.select(reservation).from(reservation)
                .where(reservation.no.eq(r.getNo()))
                .fetchCount();
        return total;
    }


}
