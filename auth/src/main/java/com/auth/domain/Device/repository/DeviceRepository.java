package com.auth.domain.Device.repository;


import com.auth.domain.Device.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device,String> {

    boolean existsByDeviceFingerprint(String deviceFingerprint);

    boolean existsByDeviceHash(String deviceHash);
}
