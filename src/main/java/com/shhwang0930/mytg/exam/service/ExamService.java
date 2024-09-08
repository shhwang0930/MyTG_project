package com.shhwang0930.mytg.exam.service;

import com.shhwang0930.mytg.exam.model.dto.DateDTO;
import com.shhwang0930.mytg.exam.model.dto.ExamDTO;
import com.shhwang0930.mytg.exam.model.entity.ExamEntity;
import com.shhwang0930.mytg.exam.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamRepository examRepository;
    public void examList() throws Exception {
        //API URL 설정
        StringBuilder urlBuilder = new StringBuilder("http://openapi.q-net.or.kr/api/service/rest/InquiryListNationalQualifcationSVC/getList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=wpdKAjZpGnV4rz8EaDL%2BZf9KVondeWTErqtKPrVGUJ1iIo1MGM%2BddyEw3%2BUKWsRItuMjq6y0CX5O3zbNzmHzdA%3D%3D"); /*Service Key*/

        //API 연결 설정
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/xml");
        System.out.println("Response code: " + conn.getResponseCode());

        //API 응답 받기
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        // XML 파싱
        List<String> jmfldnmList = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(sb.toString())));

        // 모든 <item> 요소를 찾음
        NodeList items = doc.getElementsByTagName("item");

        // 각 <item> 요소에 대해 <seriesnm>과 <jmfldnm> 값을 추출하고 조건 검사
        for (int i = 0; i < items.getLength(); i++) {
            Node item = items.item(i);

            if (item.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) item;

                int jmcd = Integer.parseInt(element.getElementsByTagName("jmcd").item(0).getTextContent());
                String seriesnm = element.getElementsByTagName("seriesnm").item(0).getTextContent();
                String jmfldnm = element.getElementsByTagName("jmfldnm").item(0).getTextContent();

                // <seriesnm>에 "기사"가 포함된 경우에만 DB에 추가
                if (seriesnm.contains("기사")) {
                    ExamDTO examDTO = new ExamDTO(jmcd, seriesnm, jmfldnm);
                    ExamEntity exam = examDTO.toEntity();
                    examRepository.save(exam);
                }
            }
        }
    }
    public List<DateDTO> examSchedule(String jmfldnm) throws Exception {
        //API URL 설정
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B490007/qualExamSchd/getQualExamSchdList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=wpdKAjZpGnV4rz8EaDL%2BZf9KVondeWTErqtKPrVGUJ1iIo1MGM%2BddyEw3%2BUKWsRItuMjq6y0CX5O3zbNzmHzdA%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("dataFormat","UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8")); /*응답 데이터 표준 형식 - xml / json (대소문자 구분 없음)*/
        urlBuilder.append("&" + URLEncoder.encode("implYy","UTF-8") + "=" + URLEncoder.encode("2024", "UTF-8")); /*시행년도*/
        urlBuilder.append("&" + URLEncoder.encode("qualgbCd","UTF-8") + "=" + URLEncoder.encode("T", "UTF-8")); /*자격구분코드 - T : 국가기술자격 - C : 과정평가형자격 - W : 일학습병행자격 - S : 국가전문자격*/
        urlBuilder.append("&" + URLEncoder.encode("jmCd","UTF-8") + "=" + URLEncoder.encode(String.valueOf(getExamJmcd(jmfldnm)), "UTF-8")); /*종목코드 값 (예) 7910 : 한식조리 기능사(검정형)*/

        //API 연결 설정
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/xml");
        System.out.println("Response code: " + conn.getResponseCode());

        //API 응답 받기
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        // XML 파싱
        List<String> scheduleList = new ArrayList<>();
        List<DateDTO> dateList = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(sb.toString())));

        // 모든 <item> 요소를 찾음
        NodeList items = doc.getElementsByTagName("item");

        // 각 <item> 요소에 대해 <seriesnm>과 <jmfldnm> 값을 추출하고 조건 검사
        for (int i = 0; i < items.getLength(); i++) {
            Node item = items.item(i);

            if (item.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) item;

                String docRegStartDt = element.getElementsByTagName("docRegStartDt").item(0).getTextContent();
                String docRegEndDt = element.getElementsByTagName("docRegEndDt").item(0).getTextContent();
                if(filterDate(docRegStartDt)){
                    DateDTO date = new DateDTO(docRegStartDt, docRegEndDt);
                    dateList.add(date);
                }
            }
        }
        return dateList;
    }

    public int getExamJmcd(String jmfldnm){
        ExamEntity examEntity = examRepository.findByJmfldnm(jmfldnm);
        if (examEntity != null) {
            return examEntity.getJmcd();
        } else {
            throw new RuntimeException("No ExamEntity found with jmfldnm: " + jmfldnm);
        }
    }

    private Boolean filterDate(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date currentDate = new Date();

        Date docRegStartDate = sdf.parse(date);
        if (docRegStartDate.compareTo(currentDate) >= 0) {
            return true;
        }
        return false;
    }
}
