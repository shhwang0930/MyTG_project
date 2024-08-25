package com.shhwang0930.mytg.exam.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamDTO {

    private int jmcd; //종목코드
    private String jmfldnm; //종목명
    private String serisenm; //계열명

    public static ExamDTO fromEntity(ExamEntity exam){
        return ExamDTO.builder()
                .jmcd(exam.getJmcd())
                .serisenm(exam.getSeriesnm())
                .jmfldnm(exam.getJmfldnm())
                .build();
    }

    public static List<ExamDTO> fromEntityList(List<ExamEntity> examList){
        return examList.stream()
                .map(ExamDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ExamEntity toEntity() {
        return ExamEntity.builder()
                .jmcd(this.jmcd)
                .seriesnm(this.serisenm)
                .jmfldnm(this.jmfldnm)
                .build();
    }
}
