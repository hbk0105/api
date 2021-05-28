package com.rest.api.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class CommonCode {

    // https://medium.com/@jason.moon.kr/selfjoin-relation-in-jpa-58942284d72
    // https://treasurebear.tistory.com/73
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_id")
    private String codeId;

    @Column(name = "code_name")
    private String codeName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CommonCode parent;

    //@OneToMany(mappedBy = "parent")
    @OneToMany(mappedBy = "parent")
    private List<CommonCode> children;

    @Getter
    @Setter
    public static class Response {
        //private Long id;
        private String codeId;
        private String codeName;
        private CommonCode parent;
        private Collection<CommonCode> children;

        // 안전한 객채 생성 패턴
        @Builder
        public Response(String codeId , String codeName , CommonCode parent , List<CommonCode> children) {
            this.codeId = codeId;
            this.codeName = codeName;
            this.parent = parent;
            this.children = children;
        }

    }

}
