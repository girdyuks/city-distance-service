package by.itechart.city_distance_service.service;

import by.itechart.city_distance_service.dto.PathDto;

import java.util.List;

public interface CityDistanceStore {
    void addEdge(String source, String target, Long distance);
    List<PathDto> findAllPaths(String source, String destination);
}
