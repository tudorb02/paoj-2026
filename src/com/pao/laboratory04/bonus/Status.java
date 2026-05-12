package com.pao.laboratory04.bonus;

public enum Status {
    TODO {
        @Override
        public boolean canTransitionTo(Status next) {
            return next == IN_PROGRESS || next == CANCELLED;
        }
    },
    IN_PROGRESS {
        @Override
        public boolean canTransitionTo(Status next) {
            return next == DONE || next == CANCELLED;
        }
    },
    DONE {
        @Override
        public boolean canTransitionTo(Status next) {
            return false;
        }
    },
    CANCELLED {
        @Override
        public boolean canTransitionTo(Status next) {
            return false;
        }
    };

    public abstract boolean canTransitionTo(Status next);
}
