package io.github.vadman1.springbootwebfinaltask.exception;

import java.time.LocalDateTime;

public class ErrorMessageResponse {

    private String message;
    private String detailedMessage;
    private LocalDateTime dateTime;

    public ErrorMessageResponse(String message, String detailedMessage, LocalDateTime dateTime) {
        this.message = message;
        this.detailedMessage = detailedMessage;
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}