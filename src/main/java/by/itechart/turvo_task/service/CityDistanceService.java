package by.itechart.turvo_task.service;

import by.itechart.turvo_task.dto.LinkDto;
import by.itechart.turvo_task.dto.PathDto;

import java.util.List;

public interface CityDistanceService {
    LinkDto addLink(LinkDto linkDto);
    List<PathDto> findAllPaths(String source, String target);
}
