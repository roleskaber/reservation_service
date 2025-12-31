package com.demo.demo.reservations.db;

import com.demo.demo.reservations.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findAllByStatusIs(ReservationStatus status);

    @Modifying
    @Query("""
            update ReservationEntity e
            set e.status = :status
            where e.id = :id
            """)
    void setStatus(@Param("id") Long id,
                   @Param("status") ReservationStatus status);

    @Query("""
            select e.id from ReservationEntity e
            where e.roomId = :roomId
            and :startDate < e.startDate
            and :endDate > e.endDate
            and e.status = :status
            """)
    List<Long> findConflicts(@Param("roomId") Long roomId, @Param("startDate") Long startDate, @Param("endDate") Long endDate, @Param("status") ReservationStatus status);

}
