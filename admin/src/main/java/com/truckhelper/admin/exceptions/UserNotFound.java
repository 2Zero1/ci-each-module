package com.truckhelper.admin.exceptions;

import com.truckhelper.core.models.UserId;

public class UserNotFound extends RuntimeException {
    public UserNotFound(UserId userId) {
        super("User Not Found (ID: " + userId + ")");
    }
}
