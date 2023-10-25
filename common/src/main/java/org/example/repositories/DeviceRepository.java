package org.example.repositories;

import org.example.models.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, String> {
    List<Device> findAllByUserId(Integer userId);
}
