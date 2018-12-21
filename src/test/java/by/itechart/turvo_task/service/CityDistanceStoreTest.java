package by.itechart.turvo_task.service;

import by.itechart.turvo_task.dto.PathDto;
import by.itechart.turvo_task.model.WeightedEdge;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CityDistanceStoreTest {
    private CityDistanceStore cityDistanceStore;
    private Graph<String, WeightedEdge> graph;

    @Before
    public void init() {
        graph = new DefaultUndirectedWeightedGraph<>(WeightedEdge.class);
        cityDistanceStore = new CityDistanceStoreImpl(graph);
    }

    @Test
    public void addEdgeWhenVertexesExist() {
        String vertex1 = "8ewyuf9823";
        String vertex2 = "38ty8g908345";
        Long distance = 25L;
        graph.addVertex(vertex1);
        graph.addVertex(vertex2);

        cityDistanceStore.addEdge(vertex1, vertex2, distance);
        assertTrue(graph.containsEdge(vertex1, vertex2));
        assertEquals(distance, graph.getEdge(vertex1, vertex2).getDistance());
    }

    @Test
    public void addEdgeWhenVertexesDoNotExist() {
        String vertex1 = "8ewyuf9823";
        String vertex2 = "38ty8g908345";
        Long distance = 25L;

        cityDistanceStore.addEdge(vertex1, vertex2, distance);
        assertTrue(graph.containsEdge(vertex1, vertex2));
        assertEquals(distance, graph.getEdge(vertex1, vertex2).getDistance());
    }

    @Test
    public void addEdgeUpdateDistanceOfExistingEdge() {
        String vertex1 = "8ewyuf9823";
        String vertex2 = "38ty8g908345";
        Long distance = 25L;
        graph.addVertex(vertex1);
        graph.addVertex(vertex2);
        graph.addEdge(vertex1, vertex2).setDistance(distance - 10L);

        cityDistanceStore.addEdge(vertex1, vertex2, distance);
        assertTrue(graph.containsEdge(vertex1, vertex2));
        assertEquals(distance, graph.getEdge(vertex1, vertex2).getDistance());
    }

    @Test
    public void findAllPathsInSimpleGraph() {
        String vertex1 = "8ewyuf9823";
        String vertex2 = "38ty8g908345";
        String vertex3 = "hdfoih340";
        Long distance1 = 25L;
        Long distance2 = 15L;
        graph.addVertex(vertex1);
        graph.addVertex(vertex2);
        graph.addVertex(vertex3);
        graph.addEdge(vertex1, vertex2).setDistance(distance1);
        graph.addEdge(vertex2, vertex3).setDistance(distance2);

        List<PathDto> paths = cityDistanceStore.findAllPaths(vertex1, vertex3);
        assertEquals(1, paths.size());
        assertEquals(Arrays.asList(vertex1, vertex2, vertex3), paths.get(0).getNodes());
        assertEquals(distance1 + distance2, paths.get(0).getDistance().longValue());
    }

    @Test
    public void findAllPathsInLoopedGraph() {
        String vertex1 = "8ewyuf9823";
        String vertex2 = "38ty8g908345";
        String vertex3 = "hdfoih340";
        Long distance1 = 25L;
        Long distance2 = 15L;
        Long distance3 = 55L;
        graph.addVertex(vertex1);
        graph.addVertex(vertex2);
        graph.addVertex(vertex3);
        graph.addEdge(vertex1, vertex2).setDistance(distance1);
        graph.addEdge(vertex2, vertex3).setDistance(distance2);
        graph.addEdge(vertex1, vertex3).setDistance(distance3);

        List<PathDto> paths = cityDistanceStore.findAllPaths(vertex3, vertex1);
        assertEquals(2, paths.size());
        assertTrue(paths.stream().anyMatch(pathDto -> pathDto.getDistance().equals(distance1 + distance2)));
        assertTrue(paths.stream().anyMatch(pathDto -> pathDto.getDistance().equals(distance3)));
    }

    @Test
    public void findAllPathsWhenPointsAreTheSame() {
        String vertex1 = "8ewyuf9823";
        String vertex2 = "38ty8g908345";
        Long distance1 = 25L;
        graph.addVertex(vertex1);
        graph.addVertex(vertex2);
        graph.addEdge(vertex1, vertex2).setDistance(distance1);

        List<PathDto> paths = cityDistanceStore.findAllPaths(vertex1, vertex1);
        assertEquals(1, paths.size());
        assertEquals(Arrays.asList(vertex1), paths.get(0).getNodes());
        assertEquals(0L, paths.get(0).getDistance().longValue());
    }

    @Test
    public void findAllPathsShouldIgnoreCyclicInTheMiddle() {
        String vertex1 = "8ewyuf9823";
        String vertex2 = "38ty8g908345";
        String vertex3 = "tr8gh9083489dgfh";
        String vertex4 = "345trew";
        String vertex5 = "ghj64324gdrf";
        Long distance = 25L;
        graph.addVertex(vertex1);
        graph.addVertex(vertex2);
        graph.addVertex(vertex3);
        graph.addVertex(vertex4);
        graph.addVertex(vertex5);
        graph.addEdge(vertex1, vertex2).setDistance(distance);
        graph.addEdge(vertex2, vertex3).setDistance(distance);
        graph.addEdge(vertex2, vertex4).setDistance(distance);
        graph.addEdge(vertex2, vertex5).setDistance(distance);
        graph.addEdge(vertex4, vertex5).setDistance(distance);

        List<PathDto> paths = cityDistanceStore.findAllPaths(vertex1, vertex3);
        assertEquals(1, paths.size());
        assertEquals(Arrays.asList(vertex1, vertex2, vertex3), paths.get(0).getNodes());
        assertEquals(distance * 2, paths.get(0).getDistance().longValue());
    }
}
