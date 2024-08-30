package com.shhwang0930.mytg.exam.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class ExamEntity {
    @Id
    private int jmcd;

    @Column
    private String jmfldnm;

    @Column
    private String seriesnm;

    @Builder
    public ExamEntity(int jmcd, String jmfldnm, String seriesnm) {
        this.jmcd = jmcd;
        this.jmfldnm = jmfldnm;
        this.seriesnm = seriesnm;
    }
}
