package by.itechart.city_distance_service.controller;

import by.itechart.city_distance_service.dto.LinkDto;
import by.itechart.city_distance_service.dto.PathDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class CityControllerSystemTest {
    @Autowired
    protected TestRestTemplate restTemplate;

    @Test
    public void iCanAddLinkBetweenCities() {
        LinkDto linkDto = new LinkDto();
        linkDto.setTarget("sdkfjhk34");
        linkDto.setSource("vh09h34");
        linkDto.setDistance(343L);
        ResponseEntity<Object> response = restTemplate.exchange("/api/cities/links", HttpMethod.PUT,
                new HttpEntity<>(linkDto), Object.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verifyResponse(linkDto, response);
    }

    @Test
    public void iCanFindAllPathsBetweenTwoCities() {
        LinkDto linkDto = new LinkDto();
        linkDto.setTarget("sdkfjhk34");
        linkDto.setSource("vh09h34");
        linkDto.setDistance(343L);
        PathDto expectedPath = new PathDto(Arrays.asList(linkDto.getSource(),
                linkDto.getTarget()), linkDto.getDistance());
        restTemplate.exchange("/api/cities/links", HttpMethod.PUT,
                new HttpEntity<>(linkDto), Object.class);

        ResponseEntity<Object> response = restTemplate.exchange(
                String.format("/api/cities/paths?source=%s&target=%s", linkDto.getSource(), linkDto.getTarget()),
                HttpMethod.GET, new HttpEntity<>(linkDto), Object.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verifyResponse(Arrays.asList(expectedPath), response);

    }

    private void verifyResponse(Object data, ResponseEntity responseEntity) {
        Gson gson = new GsonBuilder().create();
        String expected = gson.toJson(data)
                .replace("\"", "").replace(":", "=").replace(",", ", ").replace("\\u0027", "'");
        assertEquals(expected, responseEntity.getBody().toString());
    }
}
