package com.example.vehicleservice.constants;

public final class ExceptionConstants {

    private ExceptionConstants() {
    }


    // =========================================================
    // CUSTOMER
    // =========================================================

    public static final String CUSTOMER_NOT_FOUND =
            "Customer not found with id: ";

    public static final String CUSTOMER_EMAIL_EXISTS =
            "Customer already exists with email: ";

    public static final String CUSTOMER_PHONE_EXISTS =
            "Customer already exists with phone: ";


    // =========================================================
    // VEHICLE
    // =========================================================

    public static final String VEHICLE_NOT_FOUND =
            "Vehicle not found with id: ";

    public static final String VEHICLE_REGISTRATION_EXISTS =
            "Vehicle already exists with registration number: ";


    // =========================================================
    // MECHANIC
    // =========================================================

    public static final String MECHANIC_NOT_FOUND =
            "Mechanic not found with id: ";

    public static final String MECHANIC_EMAIL_EXISTS =
            "Mechanic already exists with email: ";

    public static final String MECHANIC_PHONE_EXISTS =
            "Mechanic already exists with phone: ";

    public static final String MECHANIC_NOT_AVAILABLE =
            "Mechanic is not available: ";


    // =========================================================
    // PART
    // =========================================================

    public static final String PART_NOT_FOUND =
            "Part not found with id: ";

    public static final String PART_NUMBER_EXISTS =
            "Part already exists with part number: ";

    public static final String INSUFFICIENT_PART_STOCK =
            "Insufficient part stock for part: ";


    // =========================================================
    // SERVICE REQUEST
    // =========================================================

    public static final String SERVICE_REQUEST_NOT_FOUND =
            "Service request not found with id: ";

    public static final String SERVICE_REQUEST_NUMBER_EXISTS =
            "Service request already exists with number: ";

    public static final String INVALID_SERVICE_STATUS =
            "Invalid service status transition";


    // =========================================================
    // INVOICE
    // =========================================================

    public static final String INVOICE_NOT_FOUND =
            "Invoice not found with id: ";

    public static final String INVOICE_ALREADY_EXISTS =
            "Invoice already exists for service request: ";


    // =========================================================
    // GENERAL
    // =========================================================

    public static final String INVALID_OPERATION =
            "Invalid operation: ";

    public static final String INTERNAL_SERVER_ERROR =
            "An unexpected error occurred";

    public static final String VALIDATION_FAILED =
            "Validation failed";
}