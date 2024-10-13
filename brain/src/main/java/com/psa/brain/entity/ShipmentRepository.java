package com.psa.brain.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    @Query("SELECT s FROM Shipment s ORDER BY s.priorityScore DESC")
    List<Shipment> findAllByOrderByPriorityScoreDesc();

    @Query("SELECT s FROM Shipment s WHERE s.eta > :currentTime ORDER BY s.eta ASC")
    List<Shipment> findAllShipmentsNotArrivedOrderedByEta(LocalDateTime currentTime);

    @Query("SELECT s FROM Shipment s WHERE s.eta < :currentTime AND s.estimatedLeavingTime IS NULL")
    List<Shipment> findArrivedShipmentsWithNoEstimatedLeavingTime(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT s FROM Shipment s WHERE s.eta > :currentTime ORDER BY s.priorityScore DESC")
    List<Shipment> findAllShipmentsNotArrivedOrderedByPriorityScore(LocalDateTime currentTime);
}
