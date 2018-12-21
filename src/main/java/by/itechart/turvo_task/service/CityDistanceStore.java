package by.itechart.turvo_task.service;

import by.itechart.turvo_task.dto.PathDto;

import java.util.List;

public interface CityDistanceStore {
    void addEdge(String source, String target, Long distance);
    List<PathDto> findAllPaths(String source, String destination);
}
