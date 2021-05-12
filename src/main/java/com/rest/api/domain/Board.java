package com.rest.api.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mysema.commons.lang.Assert;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Board  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY , mappedBy = "board" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Comment> comment;

    @Builder
    public Board(Long id , String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }


    @Getter
    @Setter
    public static class Response implements Serializable{

        private static final long serialVersionUID = -7353484588260422449L;
        private Long board_id;
        private String title;
        private String content;
        private String email;
        private String firstName;
        private String lastName;

        // 안전한 객채 생성 패턴 - https://cheese10yun.github.io/spring-builder-pattern/
        @Builder
        public Response(Long id ,String title , String content ,String email , String firstName , String lastName ) {
            Assert.hasText(String.valueOf(id), "id must not be empty");
            Assert.hasText(title, "title must not be empty");
            Assert.hasText(content, "content must not be empty");
            Assert.hasText(email, "email must not be empty");
            Assert.hasText(firstName, "firstName must not be empty");
            Assert.hasText(lastName, "lastName must not be empty");
            this.board_id = id;
            this.title = title;
            this.content = content;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
        }

    }

}
