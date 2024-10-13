package com.psa.brain.service;

import com.psa.brain.entity.CargoType;
import com.psa.brain.entity.Shipment;
import com.psa.brain.entity.ShipmentRepository;
import com.psa.brain.entity.UnloadingArea;
import com.psa.brain.entity.UnloadingAreaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.time.Duration;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private UnloadingAreaRepository unloadingAreaRepository;

    // Update ETA every 10 minutes
    @Scheduled(fixedRate = 6000000)  // 600,000 ms = 10 minutes
    public void updateETAs() {
        List<Shipment> shipments = shipmentRepository.findAllShipmentsNotArrivedOrderedByEta(LocalDateTime.now());
        Random random = new Random();

        for (Shipment shipment : shipments) {
            if (shipment.getEta() != null) {
                shipment.setEta(shipment.getEta().plusMinutes(random.nextInt(60) - 30)); 
                shipment.setPriorityScore((float) calculatePriorityScore(shipment));
                shipmentRepository.save(shipment);
            }
        }
    }

    public double calculatePriorityScore(Shipment shipment) {
        // Weights for each factor 
        double weightEta = 0.7;            
        double weightGoodsType = 0.1;      
        double weightUnloadingTime = 0.1;  
        double weightUrgency = 0.1;        
    
        // ETA Score (closer ETA gets higher score)
        long minutesUntilArrival = Duration.between(LocalDateTime.now(), shipment.getEta()).toMinutes();
        long maxMinutes = 48 * 60;  
        double etaScore = Math.max(0, 100.0 * (maxMinutes - minutesUntilArrival) / maxMinutes);
    
        // Goods Type Score (perishable goods get higher priority)
        double goodsTypeScore = shipment.getCargoType() == CargoType.PERISHABLE ? 100 : 50;
    
        // Unloading Time Score (shorter unloading time gets higher priority)
        double unloadingTime = shipment.getEstimatedUnloadingTime() != null ? shipment.getEstimatedUnloadingTime() : 240;  // Default max unloading time of 240 mins
        double maxUnloadingTime = 240;  // 4 hours
        double unloadingTimeScore = 100.0 * (maxUnloadingTime - unloadingTime) / maxUnloadingTime;
    
        // Urgency Score (0 = Not Urgent, 1 = Moderate, 2 = Very Urgent)
        double urgencyScore = 50 * shipment.getUrgency();  // Scale urgency: 0, 50, 100 for not urgent, moderate, very urgent
    
        // Calculate final priority score using weighted sum
        return (weightEta * etaScore) +
               (weightGoodsType * goodsTypeScore) +
               (weightUnloadingTime * unloadingTimeScore) +
               (weightUrgency * urgencyScore);
    }

    public List<Shipment> getShipmentsNotArrivedOrderedByPriorityScore() {
        return shipmentRepository.findAllShipmentsNotArrivedOrderedByPriorityScore(LocalDateTime.now());
    }

    public List<Shipment> getShipmentsNotArrivedOrderedByEta() {
        return shipmentRepository.findAllShipmentsNotArrivedOrderedByEta(LocalDateTime.now());
    }

    @Scheduled(fixedRate = 60000)  // Run every 1 minute
    public void checkArrivalsAndAssignUnloadingAreas() {
        // Step 1: Get all shipments that have arrived (ETA has passed but not yet in an unloading area)
        List<Shipment> arrivedShipments = shipmentRepository.findArrivedShipmentsWithNoEstimatedLeavingTime(LocalDateTime.now());

        // Step 2: Get all available unloading areas (areas that are not filled)
        List<UnloadingArea> availableAreas = unloadingAreaRepository.findByIsFilledFalse();

        // Step 3: Assign available unloading areas to the arrived shipments
        for (Shipment shipment : arrivedShipments) {
            if (!availableAreas.isEmpty()) {
                // Get the first available unloading area
                UnloadingArea area = availableAreas.remove(0);

                // Assign the shipment to the unloading area
                area.setShipment(shipment);
                area.setIsFilled(true);  // Mark the area as filled
                area.setStartTime(LocalDateTime.now());  // Set the unloading start time
                area.setEstimatedUnloadingEnd(LocalDateTime.now().plusMinutes(shipment.getEstimatedUnloadingTime()));  // Set the estimated unloading end time

                // Update the shipment's scheduled leaving time based on unloading duration
                shipment.setEstimatedLeavingTime(area.getEstimatedUnloadingEnd());

                // Save the updated shipment and unloading area in the database
                shipmentRepository.save(shipment);
                unloadingAreaRepository.save(area);
            }
        }
    }

    // Scheduled task to check if unloading has finished and free up unloading areas
    @Scheduled(fixedRate = 60000)  // Run every 1 minute
    public void checkUnloadingCompletion() {
        // Step 1: Get all unloading areas that are filled
        List<UnloadingArea> filledAreas = unloadingAreaRepository.findByIsFilledTrue();

        // Step 2: Check if the unloading process has finished
        for (UnloadingArea area : filledAreas) {
            if (area.getEstimatedUnloadingEnd() != null && area.getEstimatedUnloadingEnd().isBefore(LocalDateTime.now())) {
                area.setIsFilled(false);
                area.setShipment(null);  

                unloadingAreaRepository.save(area);
            }
        }
    }

    public Shipment getCurrentShipmentByAreaNumber(Integer areaNumber) {
        return unloadingAreaRepository.findCurrentShipmentByAreaNumber(areaNumber);
    }
}
