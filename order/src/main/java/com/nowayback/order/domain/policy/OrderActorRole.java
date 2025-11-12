package com.nowayback.order.domain.policy;


public enum OrderActorRole {
    MASTER,
    HUB_MANAGER,
    DELIVERY_MANAGER,
    COMPANY_MANAGER
    ;

    public boolean isMaster() {
        return this == MASTER;
    }
}
