package com.rest.api.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rest.api.domain.User;
import com.rest.api.domain.UserRoles;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import static com.rest.api.domain.QUser.user;
import static com.rest.api.domain.QUserRoles.userRoles;

@RequiredArgsConstructor
@Repository
public class UserQueryRepository {

    private final JPAQueryFactory queryFactory;

    // https://lelecoder.com/145
    @Autowired
    EntityManager em;

    @Transactional
    public long update(User u){
        // https://stackoverflow.com/questions/47144587/how-to-update-a-jpa-entity-using-querydsl
        // clear를 해주지 않을 경우 update 자동으로 두번 실행.
        em.clear();
        long id = queryFactory.update(user)
                //.set(user.mailCertification, true)
                .where(user.user_id.eq(u.getUser_id()))
                .execute();
        em.flush();
        em.clear();

        // https://dotoridev.tistory.com/2
        return id;
    }

}
