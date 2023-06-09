package com.truckhelper.admin.exceptions;

import com.truckhelper.core.models.PhoneNumber;

public class DuplicatePhoneNumberError extends RuntimeException {
    public DuplicatePhoneNumberError(PhoneNumber phoneNumber) {
        super("Duplicate Phone Number (Phone Number: " + phoneNumber + ")");
    }
}
