package com.demo.demo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository
        extends JpaRepository<ReservationEntity, Long> {
//    List<ReservationEntity> findAllByStatusIs(ReservationStatus status);

    @Transactional
    @Query("select r from ReservationEntity r where r.status = :status")
    List<ReservationEntity> setStatus(Long id,
                                      ReservationStatus status);
}
