package com.vfms.vfms.repository;

import com.vfms.vfms.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findByVehicleNo(String vehicleNo);
    List<Fine> findByOwnerUsername(String ownerUsername);
    List<Fine> findByPaid(boolean paid);
}