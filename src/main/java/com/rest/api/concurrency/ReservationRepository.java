package com.rest.api.concurrency;

import com.rest.api.concurrency.Reservation;
import com.rest.api.domain.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
