package com.pao.laboratory07.exercise1;

import java.util.ArrayDeque;
import java.util.Deque;

public class Order {
    private OrderState state;
    private Deque<OrderState> history;

    public Order(OrderState state) {
        this.state = state;
        this.history = new ArrayDeque<>();
    }

    public OrderState getState() {
        return state;
    }

    public boolean nextState() {
        if (state.isFinal()) {
            return false;
        }
        history.push(state);
        state = state.next();
        return true;
    }

    public boolean cancel() {
        if (state.isFinal()) {
            return false;
        }
        history.push(state);
        state = OrderState.CANCELED;
        return true;
    }

    public boolean undoState() {
        if (history.isEmpty()) {
            return false;
        }
        state = history.pop();
        return true;
    }
}
