package com.rest.api.batch;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author dalgun
 * @since 2019-10-30
 */
public interface PayInfoRepository extends JpaRepository<PayInfo, Long> {

    List<PayInfo> findAllBySuccessYn(boolean successYn);
    int countAllBySuccessYn(boolean successYn);


}