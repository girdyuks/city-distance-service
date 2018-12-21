package by.itechart.turvo_task.service;

import by.itechart.turvo_task.dto.PathDto;
import by.itechart.turvo_task.exception.CityNotFoundException;
import by.itechart.turvo_task.model.WeightedEdge;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class CityDistanceStoreImpl implements CityDistanceStore {
    private static final String CITY_NOT_FOUND_MESSAGE = "Cannot find city with name %s";

    private final Graph<String, WeightedEdge> graph;
    private final ReadWriteLock readWriteLock;

    public CityDistanceStoreImpl() {
        this(new DefaultUndirectedWeightedGraph<>(WeightedEdge.class));
    }

    public CityDistanceStoreImpl(final Graph<String, WeightedEdge> graph) {
        this.graph = graph;
        this.readWriteLock = new ReentrantReadWriteLock();
    }

    @Override
    public void addEdge(final String source, final String target, final Long distance) {
        readWriteLock.writeLock().lock();
        try {
            graph.addVertex(source);
            graph.addVertex(target);

            graph.addEdge(source, target);
            graph.getEdge(source, target).setDistance(distance);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public List<PathDto> findAllPaths(final String source, final String destination) {
        validateExistence(source, destination);
        readWriteLock.readLock().lock();
        try {
            List<PathDto> paths = new ArrayList<>();
            recursiveSearch(source, destination, paths, new LinkedList<>(), 0L);
            return paths;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    private void recursiveSearch(final String current, final String destination, final List<PathDto> paths,
            final Collection<String> path, final Long totalDistance) {
        path.add(current);
        if (current.equals(destination)) {
            paths.add(new PathDto(new ArrayList<>(path), totalDistance));
            path.remove(current);
            return;
        }
        final Set<String> edges  = graph.edgesOf(current).stream()
                .map(weightedEdge -> weightedEdge.getOppositeVertex(current)).collect(Collectors.toSet());
        for (String linkedVertex : edges) {
            if (!path.contains(linkedVertex)) {
                Long edgeDistance = graph.getEdge(current, linkedVertex).getDistance();
                recursiveSearch(linkedVertex, destination, paths, path, totalDistance + edgeDistance);
            }
        }
        path.remove(current);
    }

    private void validateExistence(final String source, final String target) {
        if (!graph.containsVertex(source)) {
            throw new CityNotFoundException(String.format(CITY_NOT_FOUND_MESSAGE, source));
        }
        if (!graph.containsVertex(target)) {
            throw new CityNotFoundException(String.format(CITY_NOT_FOUND_MESSAGE, target));
        }
    }
}
