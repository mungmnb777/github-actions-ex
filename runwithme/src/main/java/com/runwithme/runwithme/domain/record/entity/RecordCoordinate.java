package com.runwithme.runwithme.domain.record.entity;

import jakarta.persistence.*;
import lombok.Setter;

@Setter
@Entity
@Table(name = "t_record_coordinate")
public class RecordCoordinate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long Seq;

    @JoinColumn(name = "run_record_seq")
    @ManyToOne(fetch = FetchType.LAZY)
    private RunRecord runRecord;

    @Column
    private double latitude;

    @Column
    private double longitude;
}
