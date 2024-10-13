CREATE DATABASE logistics;

USE logistics;

CREATE TABLE shipments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- Auto-incrementing primary key
    shipment_id VARCHAR(255) NOT NULL UNIQUE,  -- Unique shipment ID
    cargo_type ENUM('PERISHABLE', 'NOT_PERISHABLE') NOT NULL,  -- Type of goods (perishable or not perishable)
    eta TIMESTAMP NULL,  -- Estimated Time of Arrival
    scheduled_arrival_time TIMESTAMP NULL,  -- Scheduled Arrival Time
    estimated_leaving_time TIMESTAMP NULL, -- Scheduled Leaving Time
    next_mode VARCHAR(255),  -- Next mode of transport (e.g., truck, rail, air)
    urgency TINYINT NOT NULL,  -- Urgency (0 = not urgent, 1 = moderate, 2 = very urgent)
    estimated_unloading_time INT NOT NULL,  -- Estimated unloading time in minutes
    priority_score FLOAT,  -- Calculated priority score
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Timestamp of record creation
);

CREATE TABLE unloading_areas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- Auto-incrementing primary key for each unloading area
    area_number INT NOT NULL,  -- The number of the unloading area (e.g., 1, 2, 3, etc.)
    is_filled BOOLEAN NOT NULL DEFAULT FALSE,  -- Whether the area is currently occupied (TRUE for filled, FALSE for empty)
    shipment_id BIGINT,  -- The ID of the shipment being unloaded in this area (can be NULL if area is empty)
    FOREIGN KEY (shipment_id) REFERENCES shipments(id) ON DELETE SET NULL,  -- Link to the shipments table
    start_time TIMESTAMP NULL,  -- The time when unloading started
    estimated_unloading_end TIMESTAMP NULL  -- Estimated time when the unloading will be finished
);

INSERT INTO shipments (shipment_id, cargo_type, eta, scheduled_arrival_time, next_mode, urgency, estimated_unloading_time)
VALUES 
('SHP001', 'PERISHABLE', NOW() + INTERVAL 2 HOUR, NOW() + INTERVAL 3 HOUR, 'truck', 2, 60),
('SHP002', 'NOT_PERISHABLE', NOW() + INTERVAL 4 HOUR, NOW() + INTERVAL 5 HOUR, 'rail', 1, 90),
('SHP003', 'PERISHABLE', NOW() + INTERVAL 6 HOUR, NOW() + INTERVAL 7 HOUR, 'air', 2, 30),
('SHP004', 'NOT_PERISHABLE', NOW() + INTERVAL 10 HOUR, NOW() + INTERVAL 12 HOUR, 'truck', 0, 180),
('SHP005', 'PERISHABLE', NOW() + INTERVAL 3 HOUR, NOW() + INTERVAL 4 HOUR, 'ship', 1, 120);

INSERT INTO unloading_areas (area_number, is_filled, shipment_id, start_time, estimated_unloading_end)
VALUES 
(1, FALSE, NULL, NULL, NULL),
(2, FALSE, NULL, NULL, NULL),
(3, FALSE, NULL, NULL, NULL),
(4, FALSE, NULL, NULL, NULL);

-- Insert future shipments
INSERT INTO shipments (shipment_id, cargo_type, eta, scheduled_arrival_time, next_mode, urgency, estimated_unloading_time, priority_score)
VALUES 
('SHP006', 'PERISHABLE', NOW() + INTERVAL 1 DAY, NOW() + INTERVAL 1 DAY + INTERVAL 2 HOUR, 'truck', 1, 90, 75.5),
('SHP007', 'NOT_PERISHABLE', NOW() + INTERVAL 2 DAY, NOW() + INTERVAL 2 DAY + INTERVAL 3 HOUR, 'rail', 2, 120, 85.0),
('SHP008', 'PERISHABLE', NOW() + INTERVAL 3 DAY, NOW() + INTERVAL 3 DAY + INTERVAL 1 HOUR, 'air', 1, 60, 65.0),
('SHP009', 'NOT_PERISHABLE', NOW() + INTERVAL 4 DAY, NOW() + INTERVAL 4 DAY + INTERVAL 2 HOUR, 'ship', 0, 180, 40.0),
('SHP010', 'PERISHABLE', NOW() + INTERVAL 5 DAY, NOW() + INTERVAL 5 DAY + INTERVAL 4 HOUR, 'truck', 2, 100, 95.5),
('SHP011', 'NOT_PERISHABLE', NOW() + INTERVAL 6 DAY, NOW() + INTERVAL 6 DAY + INTERVAL 2 HOUR, 'rail', 0, 150, 55.5),
('SHP012', 'PERISHABLE', NOW() + INTERVAL 7 DAY, NOW() + INTERVAL 7 DAY + INTERVAL 1 HOUR, 'air', 1, 70, 68.0),
('SHP013', 'NOT_PERISHABLE', NOW() + INTERVAL 8 DAY, NOW() + INTERVAL 8 DAY + INTERVAL 3 HOUR, 'truck', 0, 110, 45.0),
('SHP014', 'PERISHABLE', NOW() + INTERVAL 9 DAY, NOW() + INTERVAL 9 DAY + INTERVAL 2 HOUR, 'ship', 2, 80, 90.5),
('SHP015', 'NOT_PERISHABLE', NOW() + INTERVAL 10 DAY, NOW() + INTERVAL 10 DAY + INTERVAL 3 HOUR, 'truck', 1, 140, 72.0);

INSERT INTO shipments (shipment_id, cargo_type, eta, scheduled_arrival_time, next_mode, urgency, estimated_unloading_time, priority_score)
VALUES 
('SHP016', 'PERISHABLE', NOW() + INTERVAL 1 HOUR, NOW() + INTERVAL 30 MINUTE, 'truck', 2, 90, 98.0);

INSERT INTO shipments (shipment_id, cargo_type, eta, scheduled_arrival_time, next_mode, urgency, estimated_unloading_time, priority_score)
VALUES 
('SHP017', 'NOT_PERISHABLE', NOW() - INTERVAL 1 HOUR, NOW() - INTERVAL 2 HOUR, 'rail', 1, 120, 80.0);