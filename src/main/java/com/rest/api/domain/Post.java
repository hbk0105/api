package com.rest.api.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.sql.Date;

@Getter
@NoArgsConstructor
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column
    private String title;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date createdDate;

    @Builder
    public Post(Long id , String title) {
        this.id = id;
        this.title = title;
    }
}
