package com.pao.laboratory04.bonus;

public class InvalidTransitionException extends RuntimeException {
    private Status fromStatus;
    private Status toStatus;

    public InvalidTransitionException(Status fromStatus, Status toStatus) {
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
    }

    public Status getFromStatus() {
        return fromStatus;
    }

    public Status getToStatus() {
        return toStatus;
    }

    @Override
    public String getMessage() {
        return "Nu se poate trece din " + fromStatus + " în " + toStatus;
    }
}
