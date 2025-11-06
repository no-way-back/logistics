package com.nowayback.delivery.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipientInfo {

    private String address;
    private String name;
    private String slackId;

    private RecipientInfo(String address, String name, String slackId) {
        this.address = address;
        this.name = name;
        this.slackId = slackId;
    }

    public static RecipientInfo of(String address, String name, String slackId) {
        return new RecipientInfo(address, name, slackId);
    }
}
