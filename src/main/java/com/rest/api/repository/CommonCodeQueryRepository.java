package com.rest.api.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rest.api.domain.Board;
import com.rest.api.domain.CommonCode;
import com.rest.api.domain.QCommonCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.rest.api.domain.QBoard.board;
import static com.rest.api.domain.QCommonCode.commonCode;


@RequiredArgsConstructor
@Repository
public class CommonCodeQueryRepository {

    private final JPAQueryFactory queryFactory;

    // https://lelecoder.com/145
    // https://velog.io/@ljinsk3/Querydsl-4
    @Autowired
    EntityManager em;


    public List<CommonCode> findByAll() {
        return queryFactory.selectFrom(commonCode)
                .where(commonCode.parent.isNull())
                .fetch();
    }

}
