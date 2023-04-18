package com.procesos.negocio.parcial.repository;

import com.procesos.negocio.parcial.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
