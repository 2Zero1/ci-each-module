package com.truckhelper.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.truckhelper.core.models.User;
import com.truckhelper.core.models.UserId;

public interface UserRepository extends JpaRepository<User, UserId> {
}
