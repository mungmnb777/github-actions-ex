package com.runwithme.runwithme.domain.record.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeTotalRecord {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long seq;

    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "challenge_seq")
    private Long challengeSeq;

    @Column(name = "total_time")
    private Long totalTime;

    @Column(name = "total_distance")
    private Long totalDistance;

    @Column(name = "total_calorie")
    private Long totalCalorie;

    @Column(name = "total_longest_time")
    private Long totalLongestTime;

    @Column(name = "total_longest_distance")
    private Long totalLongestDistance;

    @Column(name = "total_avg_speed")
    private Long totalAvgSpeed;

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    public void setTotalDistance(Long totalDistance) {
        this.totalDistance = totalDistance;
    }
}
