package com.rest.api.repository;

import com.rest.api.domain.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonCodeRepository extends JpaRepository<CommonCode, String> {

    CommonCode findByCodeId(String codeId);

}
