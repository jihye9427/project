package com.kh.project.domain.entity;

import lombok.Getter;

/**
 * 회원 상태
 */
@Getter
public enum MemberStatus {
    WITHDRAWN("탈퇴", "탈퇴"),
    ACTIVE("활성화", "활성"),
    INACTIVE("비활성화", "비활성"),
    SUSPENDED("중지", "중지");

    private final String code;
    private final String description;

    MemberStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 코드로 MemberStatus 찾기
     */
    public static MemberStatus fromCode(String code) {
        for (MemberStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 상태 코드: " + code);
    }

    /**
     * 로그인 가능 여부
     */
    public boolean canLogin() {
        return this == ACTIVE;
    }
}