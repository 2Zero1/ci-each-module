package com.truckhelper.admin.applications;

import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;

import com.truckhelper.admin.dtos.AdminCreateDto;
import com.truckhelper.admin.dtos.AdminUpdateDto;
import com.truckhelper.admin.exceptions.AdminNotFound;
import com.truckhelper.admin.exceptions.DuplicatePhoneNumberError;

import com.truckhelper.admin.repositories.AdminRepository;
import com.truckhelper.core.models.Admin;
import com.truckhelper.core.models.AdminId;
import com.truckhelper.core.models.Grade;
import com.truckhelper.core.models.PhoneNumber;

@Service
@Transactional
public class AdminService {
    private AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public void create(AdminCreateDto adminCreateDto) {
        String name = adminCreateDto.getName();
        PhoneNumber phoneNumber = PhoneNumber.of(adminCreateDto.getPhoneNumber());

        Optional<Admin> admin = adminRepository.findByPhoneNumber(phoneNumber);

        if (admin.isPresent()) {
            throw new DuplicatePhoneNumberError(phoneNumber);
        }

        Admin newAdmin = Admin.builder()
                .id(AdminId.generate())
                .name(name)
                .phoneNumber(phoneNumber)
                .grade(Grade.LV1)
                .inactive(adminCreateDto.isInactive())
                .build();

        adminRepository.save(newAdmin);
    }

    public Admin update(AdminId id, AdminUpdateDto adminUpdateDto) {
        PhoneNumber phoneNumber = PhoneNumber.of(adminUpdateDto.getPhoneNumber());

        checkDuplicatePhoneNumber(id, phoneNumber);

        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new AdminNotFound(id));

        admin.changeName(adminUpdateDto.getName());
        admin.changePhoneNumber(phoneNumber);
        admin.changeInactive(adminUpdateDto.isInactive());

        adminRepository.save(admin);

        return admin;
    }

    private void checkDuplicatePhoneNumber(AdminId id, PhoneNumber phoneNumber) {
        adminRepository.findByPhoneNumber(phoneNumber)
                .ifPresent((v) -> {
                    if (!v.id().equals(id)) {
                        throw new DuplicatePhoneNumberError(phoneNumber);
                    }
                });
    }
}
