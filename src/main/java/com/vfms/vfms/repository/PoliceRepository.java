package com.vfms.vfms.repository;

import com.vfms.vfms.entity.Police;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PoliceRepository extends JpaRepository<Police, Long> {
    Optional<Police> findByUsername(String username);
}
