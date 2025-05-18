package org.aldousdev.staffservice.exceptions;

public class DepartmentAlreadyExistsException extends RuntimeException {
    public DepartmentAlreadyExistsException(String name) {
        super("Department with name '" + name + "' already exists");
    }
}
