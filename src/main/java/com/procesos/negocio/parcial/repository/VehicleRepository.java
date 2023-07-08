package com.procesos.negocio.parcial.repository;

import com.procesos.negocio.parcial.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Vehicle findByCarVin(String carVin);
    boolean existsByCarVin(String carVin);
//    @Query(value = "SELECT MAX(id) FROM vehicle", nativeQuery = true)
//    Long getMaxId();
}
