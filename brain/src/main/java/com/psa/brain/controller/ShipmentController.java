package com.psa.brain.controller;


import com.psa.brain.entity.Shipment;
import com.psa.brain.service.ShipmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/shipments")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @GetMapping("/unloading-area/{areaNumber}/current-shipment")
    public ResponseEntity<Shipment> getCurrentShipmentByAreaNumber(@PathVariable Integer areaNumber) {
        Shipment shipment = shipmentService.getCurrentShipmentByAreaNumber(areaNumber);
        if (shipment == null) {
            return ResponseEntity.notFound().build();  // Return 404 Not Found if shipment is null
        }
        return ResponseEntity.ok(shipment);
    }

    @GetMapping("/not-arrived/eta")
    public ResponseEntity<List<Shipment>> getShipmentsNotArrivedOrderedByEta() {
        List<Shipment> shipments = shipmentService.getShipmentsNotArrivedOrderedByEta();
        if (shipments.isEmpty()) {
            return ResponseEntity.noContent().build();  // Return 204 No Content if the list is empty
        }
        return ResponseEntity.ok(shipments);
    }

    @GetMapping("/not-arrived/priority")
    public ResponseEntity<List<Shipment>> getShipmentsNotArrivedOrderedByPriorityScore() {
        List<Shipment> shipments = shipmentService.getShipmentsNotArrivedOrderedByPriorityScore();
        if (shipments.isEmpty()) {
            return ResponseEntity.noContent().build();  // Return 204 No Content if the list is empty
        }
        return ResponseEntity.ok(shipments);
    }
}
