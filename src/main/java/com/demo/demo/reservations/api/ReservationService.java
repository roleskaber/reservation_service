package com.demo.demo.reservations.api;

import com.demo.demo.reservations.domain.Reservation;
import com.demo.demo.reservations.db.ReservationEntity;
import com.demo.demo.reservations.db.ReservationRepository;
import com.demo.demo.reservations.domain.ReservationStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservationService {
    private final ReservationRepository repository;

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    public Reservation getReservationById(Long id) {
        ReservationEntity find = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity with id {} not found"));
        return toDomain(find);
    }

    public List<Reservation> getAllReservations() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    public Reservation createReservation(Reservation reservation) {
        if (reservation.status() != null) {
            throw new IllegalArgumentException("Status should be empty");
        }
        checkReservationDatesValid(reservation);
        ReservationEntity entity = toEntity(reservation);
        entity.setStatus(ReservationStatus.PENDING);
        var saved = repository.save(entity);
        return toDomain(saved);
    }


    public Reservation updateReservation(Reservation reservation) {
        checkReservationExists(reservation.id());
        checkReservationDatesValid(reservation);
        var oldReservation = this.getReservationById(reservation.id());
        if (oldReservation.status() != ReservationStatus.PENDING) {
            throw new IllegalStateException("");
        }
        return toDomain(repository.save(toEntity(reservation)));
    }

    public void deleteReservation(Long id) {
        checkReservationExists(id);
        repository.deleteById(id);
    }

    @Transactional
    public Reservation approveReservationById(Long id) {
        repository.setStatus(id, ReservationStatus.APPROVED);
        return getReservationById(id);
    }

    @Transactional
    public Reservation cancelReservationById(Long id) {
        repository.setStatus(id, ReservationStatus.CANCELLED);
        return getReservationById(id);
    }

    private void checkReservationExists(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("");
        }
    }

    private void checkReservationDatesValid(Reservation r) {
        if (!r.endDate().isAfter(r.startDate())) {
            throw new IllegalArgumentException("End Date has to be valid");
        }
    }

    private ReservationEntity toEntity(Reservation re) {
        return new ReservationEntity(null, re.userId(), re.roomId(), re.startDate(), re.endDate(), re.status());
    }

    private Reservation toDomain(ReservationEntity it) {
        return new Reservation(it.getId(), it.getUserId(), it.getRoomId(), it.getStartDate(), it.getEndDate(), it.getStatus());
    }

}
