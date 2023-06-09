package com.truckhelper.admin.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.truckhelper.core.models.User;
import com.truckhelper.core.models.UserId;


public interface UserRepository extends JpaRepository<User, UserId> {
    @Query(value = """
            SELECT u FROM User u
            WHERE (u.person.name LIKE %:query%) OR (u.person.phoneNumber.value LIKE %:query%)
            OR (u.person.email.value LIKE %:query%) OR (u.person.address.address1 LIKE %:query%)
            OR (u.truck.vehiclePlate LIKE %:query%)
            """)
    List<User> findAll(@Param("query") String query);
}
