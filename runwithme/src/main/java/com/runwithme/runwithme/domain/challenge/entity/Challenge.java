package com.runwithme.runwithme.domain.challenge.entity;

import com.runwithme.runwithme.domain.user.entity.User;
import com.runwithme.runwithme.global.entity.Image;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_seq")
    private User manager;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "image_seq", nullable = true)
    private Image image;

    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "goal_days")
    private Long goalDays;

    @Column(name = "goal_type", length = 10)
    private String goalType;

    @Column(name = "goal_amount")
    private Long goalAmount;

    @Column(name = "time_start")
    private LocalDate timeStart;

    @Column(name = "time_end")
    private LocalDate timeEnd;
    @Column(name = "password", length = 10)
    private String password;

    @Column(name = "cost")
    private Long cost;
    @Column(name = "now_member")
    private Long nowMember;
    @Column(name = "max_member")
    private Long maxMember;

    @Column(name = "check_yn", nullable = false)
    private boolean checkYN;

    @Column(name = "reg_time")
    private LocalDateTime regTime;

    @Builder
    public Challenge(User manager, Image image, String name, String description, Long goalDays, String goalType, Long goalAmount, LocalDate timeStart, LocalDate timeEnd, String password, Long cost, Long nowMember, Long maxMember) {
        this.manager = manager;
        this.image = image;
        this.name = name;
        this.description = description;
        this.goalDays = goalDays;
        this.goalType = goalType;
        this.goalAmount = goalAmount;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.password = password;
        this.cost = cost;
        this.nowMember = nowMember;
        this.maxMember = maxMember;
    }
}
