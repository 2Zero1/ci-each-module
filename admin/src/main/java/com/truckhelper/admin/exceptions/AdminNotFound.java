package com.truckhelper.admin.exceptions;


import com.truckhelper.core.models.AdminId;

public class AdminNotFound extends RuntimeException {
    public AdminNotFound(AdminId adminId) {
        super("Admin Not Found (ID: " + adminId + ")");
    }
}
