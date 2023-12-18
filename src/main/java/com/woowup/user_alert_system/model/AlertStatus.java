package com.woowup.user_alert_system.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class AlertStatus {
    @Getter
    @Setter
    private boolean status;

    public AlertStatus (){
        this.status = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlertStatus that = (AlertStatus) o;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }
}
