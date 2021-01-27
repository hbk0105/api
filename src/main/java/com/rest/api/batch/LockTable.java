package com.rest.api.batch;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.rest.api.util.BooleanToYNConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author dalgun
 * @since 2019-10-30
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LockTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column
    private String email;

    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime checkDataTime;

    @Convert(converter = BooleanToYNConverter.class)
    private boolean useYn;

}