package com.auth.domain.Device.repository;


import com.auth.domain.Device.entity.Device;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device,String> {

    boolean existsByDeviceFingerprint(String deviceFingerprint);

    boolean existsByDeviceHash(String deviceHash);

    Optional<Device> findByDeviceFingerprintAndUserId(String deviceFingerprint, String id);

    List<Device> findTrustedDevices(String userId);
}
