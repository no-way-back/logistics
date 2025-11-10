package com.nowayback.hub.domain.hubnotice.entity;

import com.nowayback.common.audit.BaseEntity;
import com.nowayback.hub.domain.hubnotice.vo.NoticeContent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_hub_notices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubNotice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "notice_id", nullable = false)
    private UUID noticeId;

    @Column(name = "hub_id", nullable = false)
    private UUID hubId;

    @Embedded
    private NoticeContent content;
}