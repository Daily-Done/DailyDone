package com.example.Dailydone.Service;

import com.example.Dailydone.DTO.GeoResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GeoLatLong {
    @Autowired
    private RestTemplate restTemplate;

    private static final String API_KEY = "pk.03929cdb2b9342b25838fb4e7215d3ac";
    private static final String BASE_URL = "https://us1.locationiq.com/v1/search.php";

    public Optional<GeoResponse> getCoordinates(String address) {
        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            String url = BASE_URL + "?key=" + API_KEY + "&q=" + encodedAddress + "&format=json&limit=1";

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            if (root.isArray() && !root.isEmpty()) {
                JsonNode location = root.get(0);

                GeoResponse geo = new GeoResponse();
                geo.setLatitude(location.get("lat").asDouble());
                geo.setLongitude(location.get("lon").asDouble());

                return Optional.of(geo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

}
