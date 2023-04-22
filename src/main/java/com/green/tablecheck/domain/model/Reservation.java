package com.green.tablecheck.domain.model;

import com.green.tablecheck.domain.type.ApprovalType;
import com.green.tablecheck.domain.type.AttendType;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    private LocalDateTime deadline;  // 방문확인 데드라인(예약 시간 10분 전)
    private int peopleCount;  // 예약 인원 수
    private String code;  // 방문확인 코드

    @Enumerated(EnumType.STRING)
    private AttendType attendType;  // 고객 방문 여부

    @Enumerated(EnumType.STRING)
    private ApprovalType approvalType;  // 매장 매니저의 승인 여부

    public boolean hasReview() {
        return this.review != null;
    }

}
