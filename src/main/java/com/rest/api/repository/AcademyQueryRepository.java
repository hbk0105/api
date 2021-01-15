package com.rest.api.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rest.api.domain.Academy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.rest.api.domain.QAcademy.academy;

@RequiredArgsConstructor
@Repository
public class AcademyQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Academy> findByName(String name) {
        return queryFactory.selectFrom(academy)
                .where(academy.name.eq(name))
                .fetch();
    }
}
