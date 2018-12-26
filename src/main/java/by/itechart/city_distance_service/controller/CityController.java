package by.itechart.city_distance_service.controller;

import by.itechart.city_distance_service.dto.LinkDto;
import by.itechart.city_distance_service.dto.PathDto;
import by.itechart.city_distance_service.service.CityDistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {
    private final CityDistanceService cityDistanceService;

    @Autowired
    public CityController(final CityDistanceService cityDistanceService) {
        this.cityDistanceService = cityDistanceService;
    }

    @PutMapping(value = "/links")
    public ResponseEntity<LinkDto> createOrUpdateLink(@RequestBody @Valid final LinkDto linkDto) {
        LinkDto result = cityDistanceService.addLink(linkDto);

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/paths")
    public List<PathDto> findAllPaths(@RequestParam("source") @NotEmpty final String source,
            @RequestParam("target") @NotEmpty final String target) {
        return cityDistanceService.findAllPaths(source, target);
    }
}
