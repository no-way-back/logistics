package com.nowayback.hub.domain.hubnotice.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeContent {

    @Column(name = "content", columnDefinition = "TEXT")
    private String value;
}