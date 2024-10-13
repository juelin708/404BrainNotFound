import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';
import shipLogo from './ship.png';  // Import the ship logo image

function App() {
  const [cargoData, setCargoData] = useState([]);
  const [unloadingAreas, setUnloadingAreas] = useState([]);
  const [urgentCargo, setUrgentCargo] = useState(null);
  const areaNumbers = [1, 2, 3, 4];  // Define the area numbers you want to fetch data for

  useEffect(() => {
    // Fetch cargo data
    const fetchCargoData = async () => {
      try {
        const response = await axios.get('http://localhost:8080/shipments/not-arrived/eta');  // Correct API
        setCargoData(response.data);
      } catch (error) {
        console.error('Error fetching cargo data:', error);
      }
    };

    // Fetch urgent cargo by priority
    const fetchUrgentCargo = async () => {
      try {
        const response = await axios.get('http://localhost:8080/shipments/not-arrived/priority');  // Correct API
        setUrgentCargo(response.data[0]);
      } catch (error) {
        console.error('Error fetching urgent cargo:', error);
      }
    };

    // Fetch the current shipment for each unloading area
    const fetchUnloadingAreaData = async (areaNumber) => {
      try {
        const response = await axios.get(`http://localhost:8080/shipments/unloading-area/${areaNumber}/current-shipment`);
        
        // If a shipment is found, return the area details and shipment
        if (response.data) {
          return {
            areaNumber,
            isFilled: true,
            shipment: response.data, // Pass the shipment data directly
          };
        } else {
          // If no shipment found (area is empty), return as not filled
          return {
            areaNumber,
            isFilled: false,
            shipment: null,  // No shipment if not filled
          };
        }
      } catch (error) {
        console.error(`Error fetching unloading area ${areaNumber}:`, error);
        // In case of error, return as empty
        return {
          areaNumber,
          isFilled: false,
          shipment: null,
        };
      }
    };

    // Fetch data for all unloading areas
    const fetchAllUnloadingAreas = async () => {
      const areaDataPromises = areaNumbers.map((areaNumber) => fetchUnloadingAreaData(areaNumber));
      const areasData = await Promise.all(areaDataPromises);
      setUnloadingAreas(areasData);
    };

    // Fetch all data on component mount
    fetchCargoData();
    fetchUrgentCargo();
    fetchAllUnloadingAreas();

    // Refresh every 10 minutes
    const interval = setInterval(() => {
      fetchCargoData();
      fetchUrgentCargo();
      fetchAllUnloadingAreas();
    }, 600000);  // 10 minutes in milliseconds

    return () => clearInterval(interval);
  }, []);

  return (
    <div className="dashboard">
      {/* All Incoming Cargos */}
      <div className="all-cargos">
        <h2>All Incoming Cargos</h2>
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>ETA</th>
              <th>Type</th>
              <th>Unloading Time</th>
              <th>Emergency</th>
            </tr>
          </thead>
          <tbody>
            {cargoData.map((cargo) => (
              <tr key={cargo.shipmentId}>
                <td>{cargo.shipmentId}</td>
                <td>{cargo.eta}</td>
                <td>{cargo.cargoType}</td>
                <td>{cargo.estimatedUnloadingTime} mins</td>
                <td>{cargo.urgency === 2 ? 'Yes' : 'No'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Incoming Cargo */}
      <div className="right-panel">
        <div className="incoming-cargo">
          <h2>Incoming Cargo</h2>
          {urgentCargo && (
            <div className="cargo-info">
              <img src={shipLogo} alt="Ship Logo" className="ship-logo" />
              <div>
                <p><strong>ID:</strong> {urgentCargo.shipmentId}</p>
                <p><strong>ETA:</strong> {urgentCargo.eta}</p>
              </div>
            </div>
          )}
        </div>

        {/* Unloading Areas */}
        <div className="unloading-areas">
  <h2>Unloading Areas</h2>
  <div className="areas-row">
    {unloadingAreas.map((area) => (
      <div key={area.areaNumber} className={`area ${area.isFilled ? 'busy' : 'free'}`}>
        <p>{`Area ${area.areaNumber}`}</p>
        {area.isFilled && area.shipment && (
          <>
            <img src={shipLogo} alt="Ship" className="ship-logo" />
            <p className="cargo-id"><strong>Cargo ID:</strong> {area.shipment.shipmentId}</p>
            <p className="start-time"><strong>Start:</strong> {new Date(area.shipment.eta).toLocaleString()}</p>
            <p className="end-time"><strong>End:</strong> {new Date(area.shipment.estimatedLeavingTime).toLocaleString()}</p>
          </>
        )}
      </div>
    ))}
  </div>
</div>

      </div>
    </div>
  );
}

export default App;
