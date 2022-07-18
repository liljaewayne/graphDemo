package com.ljw.graph.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@Component
public class DataHelper {

    private static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public static <T> T convert(Object object, Class<T> classOfT) {
        try {
            String str = mapper.writeValueAsString(object);
            if (classOfT.equals(String.class)) return (T) str;
            return mapper.readValue(str, classOfT);
        } catch (IOException e) {
            log.error("Object数据转换错误", e);
            throw e;
        }
    }

    @SneakyThrows
    public static <T> T convert(Object object, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(mapper.writeValueAsString(object), typeReference);
        } catch (IOException e) {
            log.error("Object数据转换错误", e);
            throw e;
        }
    }


    @SneakyThrows
    public static <T> String toJson(T t) {
        try {
            return mapper.writeValueAsString(t);
        } catch (IOException e) {
            log.error("转换成 Json 字符串错误", e);
            throw e;
        }
    }


    @SneakyThrows
    public static <T> T fromJson(String jsonStr, Class<T> classOfT) {
        try {
            return mapper.readValue(jsonStr, classOfT);
        } catch (IOException e) {
            log.error("从 Json 字符串转对象错误，" + jsonStr, e);
            throw e;
        }
    }


    @SneakyThrows
    public static <T> T fromJson(String jsonStr, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(jsonStr, typeReference);
        } catch (IOException e) {
            log.error("从 Json 字符串转对象错误，" + jsonStr, e);
            throw e;
        }
    }


    @SneakyThrows
    public static <T> T loadFromJsonFile(String filePath, Class<T> classOfT) {

        try {
            return mapper.readValue(new ClassPathResource(filePath).getURL(), classOfT);
        } catch (IOException e) {
            log.error("json 文件加载错误, " + filePath, e);
            throw e;
        }
    }


    @SneakyThrows
    public static <T> String toXml(T obj) {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
            StringWriter writer = new StringWriter();
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(obj, writer);
            return writer.toString();
        } catch (JAXBException e) {
            log.error("toXml error", e);
            throw e;
        }
    }


    @SneakyThrows
    public static <T> T fromXml(String xmlStr, Class<T> classOfT) {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(classOfT);
            StringReader reader = new StringReader(xmlStr);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (T) jaxbUnmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            log.error("fromXml error", e);
            throw e;
        }
    }


    @SneakyThrows
    public static Map<String, String> xmlToMap(String xml) {

        try {
            Map<String, String> data = new HashMap<>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            stream.close();
            return data;
        } catch (Exception e) {
            log.error("xmlToMap error", e);
            throw e;
        }
    }


    @SneakyThrows
    public static Map<String, Object> toMap(Object obj, boolean toUnderscore) {

        Map<String, Object> mp = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                String name = toUnderscore ?
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName()) : field.getName();
                field.setAccessible(true);
                mp.put(name, field.get(obj));
            }
        } catch (IllegalAccessException e) {
            log.error("generateSignature error", e);
            throw e;
        }
        return mp;
    }
}
