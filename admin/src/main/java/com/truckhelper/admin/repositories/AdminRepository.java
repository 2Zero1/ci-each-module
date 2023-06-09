package com.truckhelper.admin.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.truckhelper.core.models.Admin;
import com.truckhelper.core.models.AdminId;
import com.truckhelper.core.models.KakaoId;
import com.truckhelper.core.models.PhoneNumber;


public interface AdminRepository extends JpaRepository<Admin, AdminId> {
    Optional<Admin> findByPhoneNumber(PhoneNumber phoneNumber);

    @Query(value = """
            SELECT admin FROM Admin admin WHERE admin.kakaoId = :kakaoId
            AND admin.inactive = false 
            """)
    Optional<Admin> findAdminByKakaoId(@Param("kakaoId") KakaoId kakaoId);

    List<Admin> findByInactiveFalse();
}
