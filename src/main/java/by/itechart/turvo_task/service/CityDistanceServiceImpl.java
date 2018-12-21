package by.itechart.turvo_task.service;

import by.itechart.turvo_task.dto.LinkDto;
import by.itechart.turvo_task.dto.PathDto;
import by.itechart.turvo_task.exception.PathNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityDistanceServiceImpl implements CityDistanceService {
    private final CityDistanceStore cityDistanceStore;

    @Autowired
    public CityDistanceServiceImpl(final CityDistanceStore cityDistanceStore) {
        this.cityDistanceStore = cityDistanceStore;
    }

    @Override
    public LinkDto addLink(final LinkDto linkDto) {
        validateNotSame(linkDto.getSource(), linkDto.getTarget());
        cityDistanceStore.addEdge(linkDto.getSource(), linkDto.getTarget(), linkDto.getDistance());

        return linkDto;
    }

    @Override
    public List<PathDto> findAllPaths(final String source, final String target) {
        List<PathDto> paths = cityDistanceStore.findAllPaths(source, target);
        if (paths.isEmpty()) {
            throw new PathNotFoundException(String.format("Cannot find path between %s and %s", source, target));
        }

        return paths;
    }

    private void validateNotSame(final String source, final String target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("Source cannot be the same as target");
        }
    }
}
