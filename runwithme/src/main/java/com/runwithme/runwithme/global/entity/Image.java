package com.runwithme.runwithme.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "image_seq")
    private Long seq;

    @Column(name = "image_original_name", nullable = false)
    private String originalName;

    @Column(name = "image_saved_name", unique = true, nullable = false)
    private String savedName;

    @Builder
    public Image(Long seq, String originalName, String savedName) {
        this.seq = seq;
        this.originalName = originalName;
        this.savedName = savedName;
    }
}
