package com.demo.demo;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservationService {
    private ReservationRepository repository;

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
        if (reservation.id() != null) {
            throw new IllegalArgumentException("Id should be empty");
        }
        if (reservation.status() != null) {
            throw new IllegalArgumentException("Status should be empty");
        }
        var saved = repository.save(toEntity(reservation));
        return toDomain(saved);
    }


    public Reservation updateReservation(Reservation reservation) {
        checkReservationExists(reservation.id());
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

    public Reservation approveReservationById(Long id) {
        Reservation reservation = this.getReservationById(id);
        ReservationEntity entity = toEntity(reservation);
        entity.setStatus(ReservationStatus.APPROVED);
        ReservationEntity result = repository.save(entity);
        return toDomain(result);
    }

    private void checkReservationExists(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("");
        }
    }

    private ReservationEntity toEntity(Reservation re) {
        return new ReservationEntity(null, re.userId(), re.roomId(), re.startDate(), re.endDate(), re.status());
    }

    private Reservation toDomain(ReservationEntity it) {
        return new Reservation(it.getId(), it.getUserId(), it.getRoomId(), it.getStartDate(), it.getEndDate(), it.getStatus());
    }
}
