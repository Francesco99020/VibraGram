package com.vibragram.backend.model;

public enum UploadSessionStatus {
    ACTIVE("active"),
    COMPLETED("completed"),
    EXPIRED("expired"),
    CANCELLED("cancelled");

    private final String message;

    UploadSessionStatus(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static UploadSessionStatus getStatusFromString(String status){
        return switch (status.toLowerCase()) {
            case "active" -> UploadSessionStatus.ACTIVE;
            case "completed" -> UploadSessionStatus.COMPLETED;
            case "cancelled" -> UploadSessionStatus.CANCELLED;
            default -> UploadSessionStatus.EXPIRED;
        };
    }
}
