import React, { useState, useEffect } from 'react';
import './App.css';
import shipLogo from './ship.pic.jpg';  // Import the ship logo image

// Sample cargo data
const initialCargoData = [
  { id: 'CARGO001', ETA: '2024-10-15 14:00', type: 'Perishable', unloadingTime: 2, emergency: true },
  { id: 'CARGO002', ETA: '2024-10-15 16:00', type: 'Non-Perishable', unloadingTime: 4, emergency: false },
  { id: 'CARGO003', ETA: '2024-10-15 14:00', type: 'Perishable', unloadingTime: 2, emergency: true },
    { id: 'CARGO004', ETA: '2024-10-15 16:00', type: 'Non-Perishable', unloadingTime: 4, emergency: false },
    { id: 'CARGO005', ETA: '2024-10-15 14:00', type: 'Perishable', unloadingTime: 2, emergency: true },
      { id: 'CARGO006', ETA: '2024-10-15 16:00', type: 'Non-Perishable', unloadingTime: 4, emergency: false },
      { id: 'CARGO007', ETA: '2024-10-15 14:00', type: 'Perishable', unloadingTime: 2, emergency: true },
        { id: 'CARGO008', ETA: '2024-10-15 16:00', type: 'Non-Perishable', unloadingTime: 4, emergency: false },
        { id: 'CARGO009', ETA: '2024-10-15 14:00', type: 'Perishable', unloadingTime: 2, emergency: true },
                { id: 'CARGO010', ETA: '2024-10-15 16:00', type: 'Non-Perishable', unloadingTime: 4, emergency: false },
  // Add more sample cargo data as needed
];

// Unloading area status simulation
const initialUnloadingAreas = [
  { areaID: 'Area1', status: 'Busy', cargoID: 'CARGO001', startTime: '14:00', endTime: '16:00' },
  { areaID: 'Area2', status: 'Free', cargoID: null, startTime: null, endTime: null },
  { areaID: 'Area3', status: 'Free', cargoID: null, startTime: null, endTime: null },
  { areaID: 'Area4', status: 'Busy', cargoID: 'CARGO002', startTime: '16:00', endTime: '18:00' },
];

function App() {
  const [cargoData, setCargoData] = useState(initialCargoData);
  const [unloadingAreas, setUnloadingAreas] = useState(initialUnloadingAreas);
  const [urgentCargo, setUrgentCargo] = useState([]);

  // Priority score calculation
  function calculatePriorityScore(cargo) {
    let score = 0;
    const etaDiff = new Date(cargo.ETA) - new Date(); // time until arrival
    score += (1 / etaDiff) * 1000; // Earlier arrival = higher score
    if (cargo.type === 'Perishable') score += 50; // Perishable goods = higher score
    score += (10 / cargo.unloadingTime) * 20; // Lower unloading time = higher score
    if (cargo.emergency) score += 100; // Emergency = maximum score
    return score;
  }

  // Sort cargo by priority on mount and update regularly
  useEffect(() => {
    const updatePriority = () => {
      const sortedCargo = [...cargoData].sort((a, b) => calculatePriorityScore(b) - calculatePriorityScore(a));
      setUrgentCargo(sortedCargo);
    };

    updatePriority();
    const interval = setInterval(updatePriority, 600000); // Every 10 minutes
    return () => clearInterval(interval); // Cleanup interval
  }, [cargoData]);

  return (
    <div className="dashboard">
      {/* Left Panel: All Incoming Cargos */}
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
              <tr key={cargo.id}>
                <td>{cargo.id}</td>
                <td>{cargo.ETA}</td>
                <td>{cargo.type}</td>
                <td>{cargo.unloadingTime} hours</td>
                <td>{cargo.emergency ? 'Yes' : 'No'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Right Top Section: Incoming Cargo */}
      <div className="incoming-cargo">
        <h2>Incoming Cargo</h2>
        {urgentCargo.length > 0 && (
          <div className="cargo-info">
            <img src={shipLogo} alt="Ship Logo" className="ship-logo" />
            <div>
              <p><strong>ID:</strong> {urgentCargo[0].id}</p>
              <p><strong>ETA:</strong> {urgentCargo[0].ETA}</p>
            </div>
          </div>
        )}
      </div>

      {/* Right Bottom Section: Unloading Areas */}
      <div className="unloading-areas">
        <h2>Unloading Areas</h2>
        <div className="areas-row">
          {unloadingAreas.map((area) => (
            <div key={area.areaID} className={`area ${area.status === 'Free' ? 'free' : 'busy'}`}>
              <p>{area.areaID}</p>
              {area.status === 'Busy' && (
                <>
                  <img src={shipLogo} alt="Ship" className="ship-logo" />
                  <p><strong>Cargo ID:</strong> {area.cargoID}</p>
                  <p><strong>Start:</strong> {area.startTime}</p>
                  <p><strong>End:</strong> {area.endTime}</p>
                </>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default App;
