package com.rest.api.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rest.api.domain.Academy;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.rest.api.domain.QAcademy.academy;

@RequiredArgsConstructor
public class AcademyRepositoryImpl implements AcademyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Academy> findByName(String name) {
        return queryFactory.selectFrom(academy)
                .where(academy.name.eq(name))
                .fetch();
    }
}
