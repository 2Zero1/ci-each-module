package com.truckhelper.admin.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.modelmapper.ModelMapper;

import com.truckhelper.admin.applications.AdminService;
import com.truckhelper.admin.dtos.AdminCreateDto;
import com.truckhelper.admin.dtos.AdminDetailDto;
import com.truckhelper.admin.dtos.AdminDto;
import com.truckhelper.admin.dtos.AdminUpdateDto;
import com.truckhelper.admin.dtos.AdminsDto;
import com.truckhelper.admin.exceptions.AdminNotFound;
import com.truckhelper.admin.exceptions.AuthenticationError;
import com.truckhelper.admin.exceptions.AuthorizationError;
import com.truckhelper.admin.exceptions.DuplicatePhoneNumberError;
import com.truckhelper.admin.repositories.AdminRepository;
import com.truckhelper.core.models.Admin;
import com.truckhelper.core.models.AdminId;
import com.truckhelper.core.models.Grade;

@CrossOrigin
@RestController
@RequestMapping("admins")
public class AdminController {
    private final ModelMapper modelMapper;

    private AdminService adminService;

    private AdminRepository adminRepository;

    public AdminController(
            ModelMapper modelMapper,
            AdminService adminService,
            AdminRepository adminRepository
    ) {
        this.modelMapper = modelMapper;
        this.adminService = adminService;
        this.adminRepository = adminRepository;
    }

    @GetMapping
    public AdminsDto list(
            @RequestAttribute Optional<Admin> currentAdmin
    ) {
        checkSuperAdmin(currentAdmin);

        List<Admin> admins = adminRepository.findAll(
                Sort.by(
                        Sort.Order.asc("inactive"),
                        Sort.Order.desc("id")
                )
        );

        return new AdminsDto(
                admins.stream()
                        .map(this::adminToDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("{id}")
    public AdminDetailDto detail(
            @RequestAttribute Optional<Admin> currentAdmin,
            @PathVariable String id
    ) {
        checkSuperAdmin(currentAdmin);

        AdminId adminId = new AdminId(id);

        Admin admin = adminRepository.findById(new AdminId(id))
                .orElseThrow(() -> new AdminNotFound(adminId));

        if (admin.grade() == Grade.MASTER) {
            throw new AuthorizationError();
        }

        return new AdminDetailDto(adminToDto(admin));
    }

    @GetMapping("me")
    public AdminDetailDto profile(
            @RequestAttribute Optional<Admin> currentAdmin
    ) {
        Admin admin = currentAdmin.orElseThrow(AuthenticationError::new);

        return new AdminDetailDto(adminToDto(admin));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String create(
            @RequestAttribute Optional<Admin> currentAdmin,
            @RequestBody AdminCreateDto adminCreateDto
    ) {
        checkSuperAdmin(currentAdmin);

        adminService.create(adminCreateDto);

        return "Created";
    }

    @PatchMapping("{id}")
    public String update(
            @PathVariable String id,
            @RequestAttribute Optional<Admin> currentAdmin,
            @RequestBody AdminUpdateDto adminUpdateDto
    ) {
        checkSuperAdmin(currentAdmin);

        adminService.update(new AdminId(id), adminUpdateDto);

        return "Updated";
    }

    private void checkSuperAdmin(Optional<Admin> currentAdmin) {
        Admin admin = currentAdmin.orElseThrow(AuthenticationError::new);

        if (admin.grade() != Grade.MASTER) {
            throw new AuthorizationError();
        }
    }

    private AdminDto adminToDto(Admin admin) {
        return modelMapper.map(admin, AdminDto.class);
    }

    @ExceptionHandler(DuplicatePhoneNumberError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private String duplicatePhoneNumber() {
        return "Duplicate phone number";
    }

    @ExceptionHandler(AdminNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private String notFound() {
        return "Not found admin";
    }
}
