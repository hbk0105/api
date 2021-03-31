package com.rest.api.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mysema.commons.lang.Assert;
import lombok.*;

import javax.persistence.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long comment_id;

    @Column
    private String title;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @Setter
    public static class Response {

        private Long board_id;
        private Long comment_id;
        private String title;
        private String content;
        private String email;
        private String firstName;
        private String lastName;

        // 안전한 객채 생성 패턴
        @Builder
        public Response(Long board_id , Long comment_id ,  String title , String content ,String email , String firstName , String lastName ) {
            Assert.hasText(String.valueOf(board_id), "board_id must not be empty");
            Assert.hasText(String.valueOf(comment_id), "comment_id must not be empty");
            Assert.hasText(title, "title must not be empty");
            Assert.hasText(content, "content must not be empty");
            Assert.hasText(email, "email must not be empty");
            Assert.hasText(firstName, "firstName must not be empty");
            Assert.hasText(lastName, "lastName must not be empty");
            this.board_id = board_id;
            this.comment_id = comment_id;
            this.title = title;
            this.content = content;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
        }

    }


}
