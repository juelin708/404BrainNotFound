package com.psa.brain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true, name = "shipment_id")
    private String shipmentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "cargo_type")
    private CargoType cargoType;  // 'perishable' or 'not perishable'

    @Column(name = "eta")
    private LocalDateTime eta;

    @Column(name = "scheduled_arrival_time")
    private LocalDateTime scheduledArrivalTime;

    @Column(name = "estimated_leaving_time")
    private LocalDateTime estimatedLeavingTime;

    @Column(name = "next_mode")
    private String nextMode;

    @Column(name = "urgency")
    private Integer urgency;  // 0 = Not urgent, 1 = Moderate, 2 = Very urgent

    @Column(name = "estimated_unloading_time")
    private Integer estimatedUnloadingTime;  // In minutes

    @Column(name = "priority_score")
    private Float priorityScore;

    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    // Constructor
    public Shipment() {
        this.createdAt = LocalDateTime.now();
    }

    public Shipment(String shipmentId, CargoType cargoType, LocalDateTime eta, LocalDateTime scheduledArrivalTime,
                    LocalDateTime scheduledLeavingTime, String nextMode, Integer urgency, Integer estimatedUnloadingTime,
                    Float priorityScore) {
        this.shipmentId = shipmentId;
        this.cargoType = cargoType;
        this.eta = eta;
        this.scheduledArrivalTime = scheduledArrivalTime;
        this.estimatedLeavingTime = scheduledLeavingTime;
        this.nextMode = nextMode;
        this.urgency = urgency;
        this.estimatedUnloadingTime = estimatedUnloadingTime;
        this.priorityScore = priorityScore;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public CargoType getCargoType() {
        return cargoType;
    }

    public void setCargoType(CargoType cargoType) {
        this.cargoType = cargoType;
    }

    public LocalDateTime getEta() {
        return eta;
    }

    public void setEta(LocalDateTime eta) {
        this.eta = eta;
    }

    public LocalDateTime getScheduledArrivalTime() {
        return scheduledArrivalTime;
    }

    public void setScheduledArrivalTime(LocalDateTime scheduledArrivalTime) {
        this.scheduledArrivalTime = scheduledArrivalTime;
    }

    public LocalDateTime getEstimatedLeavingTime() {
        return estimatedLeavingTime;
    }

    public void setEstimatedLeavingTime(LocalDateTime estimatedLeavingTime) {
        this.estimatedLeavingTime = estimatedLeavingTime;
    }

    public String getNextMode() {
        return nextMode;
    }

    public void setNextMode(String nextMode) {
        this.nextMode = nextMode;
    }

    public Integer getUrgency() {
        return urgency;
    }

    public void setUrgency(Integer urgency) {
        this.urgency = urgency;
    }

    public Integer getEstimatedUnloadingTime() {
        return estimatedUnloadingTime;
    }

    public void setEstimatedUnloadingTime(Integer estimatedUnloadingTime) {
        this.estimatedUnloadingTime = estimatedUnloadingTime;
    }

    public Float getPriorityScore() {
        return priorityScore;
    }

    public void setPriorityScore(Float priorityScore) {
        this.priorityScore = priorityScore;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}