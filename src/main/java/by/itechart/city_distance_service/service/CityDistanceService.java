package by.itechart.city_distance_service.service;

import by.itechart.city_distance_service.dto.LinkDto;
import by.itechart.city_distance_service.dto.PathDto;

import java.util.List;

public interface CityDistanceService {
    LinkDto addLink(LinkDto linkDto);
    List<PathDto> findAllPaths(String source, String target);
}
