package com.rest.api.batch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author dalgun
 * @since 2019-10-30
 */
@Repository
public interface LockTableRepository extends JpaRepository<LockTable, Long> {
    LockTable findByEmail(String email);
}