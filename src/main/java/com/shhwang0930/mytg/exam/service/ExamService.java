package com.shhwang0930.mytg.exam.service;

import com.shhwang0930.mytg.exam.model.ExamDTO;
import com.shhwang0930.mytg.exam.model.ExamEntity;
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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamRepository examRepository;
    public void examList() throws Exception
    {
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
}
