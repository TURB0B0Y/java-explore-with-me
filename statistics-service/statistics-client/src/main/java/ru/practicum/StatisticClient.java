package ru.practicum;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.CreateHitDTO;
import ru.practicum.dto.StatisticDTO;
import ru.practicum.envirnoment.Envirnoments;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class StatisticClient {

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern(Envirnoments.DATE_FORMAT);
    private final RestTemplate rest;

    public StatisticClient(String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ResponseEntity<List<StatisticDTO>> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        Map<String, Object> params = new HashMap<>();
        params.put("start", DF.format(start));
        params.put("end", DF.format(end));
        params.put("uris", uris);
        params.put("unique", unique);
        return makeAndSendRequest(
                HttpMethod.GET,
                "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                null,
                params,
                new ParameterizedTypeReference<List<StatisticDTO>>() {}
        );
    }

    public void addHit(CreateHitDTO hitDto) {
        makeAndSendRequest(
                HttpMethod.POST,
                "/hit",
                hitDto,
                null,
                new ParameterizedTypeReference<Void>() {}
        );
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private <T> ResponseEntity<T> makeAndSendRequest(HttpMethod method, String path, @Nullable Object body, Map<String, Object> params, ParameterizedTypeReference<T> typeReference) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<T> response;
        try {
            if (Objects.isNull(params)) {
                response = rest.exchange(path, method, requestEntity, typeReference);
            } else {
                response = rest.exchange(path, method, requestEntity, typeReference, params);
            }
        } catch (Exception e) {
            throw new ClientException(e);
        }
        return prepareGatewayResponse(response);
    }

    private static <T> ResponseEntity<T> prepareGatewayResponse(ResponseEntity<T> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }

}
