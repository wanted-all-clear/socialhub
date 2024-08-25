package com.allclear.socialhub.user.domain;

import com.allclear.socialhub.common.domain.Timestamped;
import com.allclear.socialhub.user.type.UserCertifyStatus;
import com.allclear.socialhub.user.type.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 20)
    private String password;

    private LocalDateTime deletedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserCertifyStatus certifyStatus;

    /**
     * 사용자 인증 상태를 UNAUTHENTICATED에서 AUTHENTICATED로 변경합니다.
     */
    public void authenticateUser() {

        if (this.certifyStatus == UserCertifyStatus.UNAUTHENTICATED) {
            this.certifyStatus = UserCertifyStatus.AUTHENTICATED;
        }
    }

}
