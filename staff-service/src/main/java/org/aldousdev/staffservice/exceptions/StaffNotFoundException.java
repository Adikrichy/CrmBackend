package org.aldousdev.staffservice.exceptions;
public class StaffNotFoundException extends RuntimeException {
    public StaffNotFoundException(Long id) {
        super("Staff not found with id: " + id);
    }
}
