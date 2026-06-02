package com.pao.laboratory13.exercise1;

public class ProtocolEngine {
    private State state = State.INIT;
    private int historyCount = 0;

    public String execute(String rawCommand) {
        if (rawCommand == null || rawCommand.trim().isEmpty()) {
            return null;
        }

        String line = rawCommand.trim();
        String[] parts = line.split("\\s+", 2);
        String command = parts[0];
        String argument = parts.length > 1 ? parts[1].trim() : "";

        switch (command) {
            case "AUTH":
                return auth(argument);
            case "OPEN":
                return open(argument);
            case "SEND":
                return send(argument);
            case "BROADCAST":
                return broadcast(argument);
            case "HISTORY":
                return history(argument);
            case "CLOSE":
                return close(argument);
            default:
                return "ERR E_PARSE UNKNOWN_COMMAND";
        }
    }

    private String auth(String user) {
        if (user.isEmpty()) {
            return "ERR E_PARSE AUTH";
        }
        if (state == State.CLOSED) {
            return "ERR E_STATE CLOSED";
        }

        state = State.AUTH;
        historyCount = 0;
        return "OK AUTH user=" + user;
    }

    private String open(String argument) {
        if (!argument.isEmpty()) {
            return "ERR E_PARSE OPEN";
        }
        if (state == State.CLOSED) {
            return "ERR E_STATE CLOSED";
        }
        if (state == State.OPEN) {
            return "ERR E_STATE ALREADY_OPEN";
        }
        if (state != State.AUTH) {
            return "ERR E_STATE NOT_OPEN";
        }

        state = State.OPEN;
        return "OK OPEN";
    }

    private String send(String payload) {
        if (payload.isEmpty()) {
            return "ERR E_PARSE SEND";
        }
        if (state == State.CLOSED) {
            return "ERR E_STATE CLOSED";
        }
        if (state != State.OPEN) {
            return "ERR E_STATE NOT_OPEN";
        }

        historyCount++;
        return "OK OPEN sent";
    }

    private String broadcast(String payload) {
        if (payload.isEmpty()) {
            return "ERR E_PARSE BROADCAST";
        }
        if (state == State.CLOSED) {
            return "ERR E_STATE CLOSED";
        }
        if (state != State.OPEN) {
            return "ERR E_STATE NOT_OPEN";
        }

        historyCount++;
        return "OK OPEN broadcast";
    }

    private String history(String argument) {
        if (!argument.isEmpty()) {
            return "ERR E_PARSE HISTORY";
        }
        if (state == State.CLOSED) {
            return "ERR E_STATE CLOSED";
        }
        if (state != State.OPEN) {
            return "ERR E_STATE NOT_OPEN";
        }

        return "OK OPEN history=" + historyCount;
    }

    private String close(String argument) {
        if (!argument.isEmpty()) {
            return "ERR E_PARSE CLOSE";
        }
        if (state == State.CLOSED) {
            return "ERR E_STATE CLOSED";
        }
        if (state != State.OPEN) {
            return "ERR E_STATE NOT_OPEN";
        }

        state = State.CLOSED;
        return "OK CLOSED";
    }

    private enum State {
        INIT,
        AUTH,
        OPEN,
        CLOSED
    }
}
