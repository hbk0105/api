package com.rest.api.repository;


import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rest.api.domain.Board;
import com.rest.api.domain.BoardPaginationDto;
import com.rest.api.domain.QBoard;
import com.rest.api.util.QueryDslUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import static com.rest.api.domain.QBoard.board;
import static org.springframework.util.ObjectUtils.isEmpty;


@RequiredArgsConstructor
@Repository
public class BoardQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Autowired
    EntityManager em;

    public List<Board> findByAll() {
        return queryFactory.selectFrom(board).fetch();
    }

    public List<Board> findByName(String name) {
        return queryFactory.selectFrom(board)
                .where(board.name.eq(name))
                .fetch();
    }

    public void save(Board board){
        em.persist(board);
    }

    // https://jojoldu.tistory.com/529?category=637935
    public List<BoardPaginationDto> paginationCoveringIndex(String name, int pageNo, int pageSize, Pageable pageable) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        // 1) 커버링 인덱스로 대상 조회
        List<Long> ids = queryFactory
                .select(board.id)
                .from(board)
                //.where(board.name.like(name + "%"))
                //.where(board.name.contains(name))
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                //.orderBy(board.id.desc())
                .limit(pageSize)
                //.offset(pageNo * pageSize)
                .offset((pageNo - 1) * pageSize)
                .fetch();

        // 1-1) 대상이 없을 경우 추가 쿼리 수행 할 필요 없이 바로 반환
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }

        // 2)
        return queryFactory
                .select(Projections.fields(BoardPaginationDto.class,
                        board.id.as("bookId"),
                        board.name))
                .from(board)
                .where(board.id.in(ids))
                //.orderBy(board.id.desc())
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .fetch(); // where in id만 있어 결과 정렬이 보장되지 않는다.
    }

    // https://uchupura.tistory.com/7
    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "id":
                        OrderSpecifier<?> orderId = QueryDslUtil.getSortedColumn(direction, board.id, "id");
                        ORDERS.add(orderId);
                        break;
                    case "name":
                        OrderSpecifier<?> orderUser = QueryDslUtil.getSortedColumn(direction, board.name, "name");
                        ORDERS.add(orderUser);
                        break;
                    case "content":
                        OrderSpecifier<?> orderCategory = QueryDslUtil.getSortedColumn(direction, board.content, "content");
                        ORDERS.add(orderCategory);
                        break;
                    default:
                        break;
                }
            }
        }

        return ORDERS;
    }

}
