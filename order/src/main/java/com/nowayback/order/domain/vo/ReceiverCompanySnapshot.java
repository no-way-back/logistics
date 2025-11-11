package com.nowayback.order.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReceiverCompanySnapshot {

    private String name;
    private String address;
    private String detailAddress;
    private String contact;

    private ReceiverCompanySnapshot(
        String name,
        String address,
        String detailAddress,
        String contact
    ) {
        this.name = name;
        this.address = address;
        this.detailAddress = detailAddress;
        this.contact = contact;
    }

    public static ReceiverCompanySnapshot of(
        String name,
        String address,
        String detailAddress,
        String contact
    ) {
        return new ReceiverCompanySnapshot(name, address, detailAddress, contact);
    }
}
