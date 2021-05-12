package com.rest.api.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Getter
@Setter
@Entity
@Table
public class Files {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column
    private String origFilename;

    @Column
    private String fileName;

    @Column
    private String filePath;

    @Column(name="size")
    private long size;

    @Column
    private String mimeType;
    // https://override1592.tistory.com/14

    @CreationTimestamp    // 입력시 시간 정보를 자동으로 입력해는 어노테이션.
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date registDate;

    @Override
    public String toString() {
        return "File [id=" + id + ", fileName=" + fileName + ", size=" + size + ", mimeType=" + mimeType
                + ", registDate=" + registDate + "]";
    }




}