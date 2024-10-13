package com.psa.brain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "unloading_areas")
public class UnloadingArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "area_number")
    private Integer areaNumber;

    @Column(name = "is_filled")
    private Boolean isFilled = false;

    @ManyToOne
    @JoinColumn(name = "shipment_id", referencedColumnName = "id", nullable = true)
    private Shipment shipment;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "estimated_unloading_end")
    private LocalDateTime estimatedUnloadingEnd;

    // Constructor
    public UnloadingArea() {}

    public UnloadingArea(Integer areaNumber, Boolean isFilled, Shipment shipment, LocalDateTime startTime, LocalDateTime estimatedUnloadingEnd) {
        this.areaNumber = areaNumber;
        this.isFilled = isFilled;
        this.shipment = shipment;
        this.startTime = startTime;
        this.estimatedUnloadingEnd = estimatedUnloadingEnd;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAreaNumber() {
        return areaNumber;
    }

    public void setAreaNumber(Integer areaNumber) {
        this.areaNumber = areaNumber;
    }

    public Boolean getIsFilled() {
        return isFilled;
    }

    public void setIsFilled(Boolean isFilled) {
        this.isFilled = isFilled;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEstimatedUnloadingEnd() {
        return estimatedUnloadingEnd;
    }

    public void setEstimatedUnloadingEnd(LocalDateTime estimatedUnloadingEnd) {
        this.estimatedUnloadingEnd = estimatedUnloadingEnd;
    }
}
