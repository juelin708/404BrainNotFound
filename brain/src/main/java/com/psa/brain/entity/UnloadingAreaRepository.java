package com.psa.brain.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UnloadingAreaRepository extends JpaRepository<UnloadingArea, Long> {
    @Query("SELECT u FROM UnloadingArea u WHERE u.isFilled =  false")
    List<UnloadingArea> findByIsFilledFalse();

    @Query("SELECT u FROM UnloadingArea u WHERE u.isFilled =  true")
    List<UnloadingArea> findByIsFilledTrue();

    @Query("SELECT u FROM UnloadingArea u WHERE u.isFilled = true AND u.estimatedUnloadingEnd < :currentTime")
    List<UnloadingArea> findUnloadingAreasWhereUnloadingHasEnded(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT u.shipment FROM UnloadingArea u WHERE u.areaNumber = :areaNumber AND u.isFilled = true")
    Shipment findCurrentShipmentByAreaNumber(Integer areaNumber);
}

