package com.shhwang0930.mytg.exam.service;

import com.shhwang0930.mytg.exam.model.entity.ExamEntity;
import com.shhwang0930.mytg.exam.repository.ExamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.*;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ExamServiceTest {
    @Mock
    private ExamRepository examRepository;  // DB 연동 부분을 Mocking

    @InjectMocks
    private ExamService examService;        // 실제 서비스 클래스를 테스트

    private HttpURLConnection mockConnection;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Mock HttpURLConnection 생성
        URL mockUrl = new URL("http://mockurl");
        mockConnection = mock(HttpURLConnection.class);
    }

    @Test
    @DisplayName("Exam API 목록 가져오기 - 정상 응답")
    void testExamList() throws Exception {
        // Given: Mock API Response
        // Mock API 응답 데이터
        String mockXmlResponse = "<response><body><items><item>"
                + "<jmcd>123</jmcd>"
                + "<seriesnm>정보처리기사</seriesnm>"
                + "<jmfldnm>정보통신</jmfldnm>"
                + "</item></items></body></response>";

        // Mock HttpURLConnection 설정
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        when(mockConnection.getResponseCode()).thenReturn(200);
        when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream(mockXmlResponse.getBytes()));

        // Mock URL 설정
        URL mockUrl = mock(URL.class);
        when(mockUrl.openConnection()).thenReturn(mockConnection);

        // 실제 테스트 대상 메서드 호출
        examService.examList();

        // examRepository.save()가 호출되었는지 검증
        verify(examRepository, times(1)).save(any(ExamEntity.class)); // "기사" 포함된 두 항목만 저장
    }
}
