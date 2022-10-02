package ru.netology;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static final String REMOTE_SERVICE_URI =
            "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setUserAgent("Facts about cats")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build()) {
            // создание объекта запроса
            HttpGet request = new HttpGet(REMOTE_SERVICE_URI);
            request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
            // отправка запроса
            CloseableHttpResponse response = httpClient.execute(request);
            List<CatsFacts> catsFactsList = mapper.readValue(response.getEntity().getContent(), new TypeReference<>() {});
            List<CatsFacts> cats = catsFactsList.stream().filter(cat -> cat.getUpvotes() != 0).collect(Collectors.toList());
            System.out.println(cats);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
