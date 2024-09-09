package com.shhwang0930.mytg.exam.service;


import com.shhwang0930.mytg.exam.model.dto.ExamDTO;
import com.shhwang0930.mytg.exam.model.entity.ExamEntity;
import com.shhwang0930.mytg.exam.repository.ExamRepository;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


import static org.mockito.Mockito.when;

public class ExamServiceTest {
    @Mock
    private ExamRepository examRepository;  // DB 연동 부분을 Mocking

    @InjectMocks
    private ExamService examService;        // 실제 서비스 클래스를 테스트

    private SimpleDateFormat sdf;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        sdf = new SimpleDateFormat("yyyyMMdd");
    }

    @Test
    @DisplayName("Exam API 목록 가져오기 - 정상 응답")
    void testExamList() throws Exception {
        // given
        // Mock된 XML 응답 데이터 준비
        String mockXmlResponse =
                "<response>" +
                        "  <body>" +
                        "    <items>" +
                        "      <item>" +
                        "        <jmcd>1234</jmcd>" +
                        "        <seriesnm>기사</seriesnm>" +
                        "        <jmfldnm>정보처리기사</jmfldnm>" +
                        "      </item>" +
                        "      <item>" +
                        "        <jmcd>5678</jmcd>" +
                        "        <seriesnm>기능사</seriesnm>" +
                        "        <jmfldnm>정보처리기능사</jmfldnm>" +
                        "      </item>" +
                        "    </items>" +
                        "  </body>" +
                        "</response>";

        // Mock InputStream으로 위의 XML 데이터를 제공
        InputStream mockInputStream = new ByteArrayInputStream(mockXmlResponse.getBytes());

        // XML 파싱 설정
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(mockInputStream));

        // when
        NodeList items = doc.getElementsByTagName("item");

        // for 루프 내에서 각 <item>에 대해 조건 검사 및 DB 저장 로직 테스트
        for (int i = 0; i < items.getLength(); i++) {
            Element element = (Element) items.item(i);
            String seriesnm = element.getElementsByTagName("seriesnm").item(0).getTextContent();
            String jmfldnm = element.getElementsByTagName("jmfldnm").item(0).getTextContent();
            int jmcd = Integer.parseInt(element.getElementsByTagName("jmcd").item(0).getTextContent());

            if (seriesnm.contains("기사")) {
                // DTO를 통해 Entity로 변환
                ExamDTO examDTO = new ExamDTO(jmcd, seriesnm, jmfldnm);
                ExamEntity examEntity = examDTO.toEntity();

                // Mock Repository를 통해 저장
                examRepository.save(examEntity);

                // 저장 확인
                verify(examRepository, times(1)).save(examEntity);
            }
        }

        // then
        // "기사"가 포함된 데이터만 DB에 저장되었는지 검증
        verify(examRepository, times(1)).save(any(ExamEntity.class));
    }

    @Test
    @DisplayName("종목코드 탐색 성공")
    public void testGetExamJmcd_Success() {
        // given
        String jmfldnm = "정보처리기사";
        ExamEntity mockExamEntity = ExamEntity
                .builder()
                .jmcd(1234)
                .jmfldnm(jmfldnm)
                .seriesnm("test")
                .build();

        // when
        when(examRepository.findByJmfldnm(jmfldnm)).thenReturn(mockExamEntity);

        // then
        int jmcd = examService.getExamJmcd(jmfldnm);
        assertEquals(1234, jmcd); // 예상 jmcd 값과 비교
    }

    @Test
    @DisplayName("종목코드 탐색 실패")
    public void testGetExamJmcd_NotFound() {
        // given
        String jmfldnm = "비존재하는기사";

        // when
        when(examRepository.findByJmfldnm(jmfldnm)).thenReturn(null);

        // then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            examService.getExamJmcd(jmfldnm);
        });

        String expectedMessage = "No ExamEntity found with jmfldnm: " + jmfldnm;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("날짜 비교 - 미래")
    public void testFilterDate_FutureDate() throws Exception {
        // given
        String futureDate = sdf.format(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)); // 현재 날짜 + 1일

        // when
        Boolean result = examService.filterDate(futureDate);

        // then
        assertTrue(result); // 미래 날짜는 true
    }

    @Test
    @DisplayName("날짜 비교 - 과거")
    public void testFilterDate_PastDate() throws Exception {
        // given
        String pastDate = sdf.format(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)); // 현재 날짜 - 1일

        // when
        Boolean result = examService.filterDate(pastDate);

        // then
        assertFalse(result); // 과거 날짜는 false
    }

    @Test
    @DisplayName("날짜 비교 - 현재")
    public void testFilterDate_TodayDate() throws Exception {
        // given
        String todayDate = sdf.format(new Date()); // 오늘 날짜

        // when
        Boolean result = examService.filterDate(todayDate);

        // then
        assertFalse(result); // 오늘 날짜는 false
    }
}
