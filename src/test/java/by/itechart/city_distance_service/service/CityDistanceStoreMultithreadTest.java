package by.itechart.city_distance_service.service;

import by.itechart.city_distance_service.model.WeightedEdge;
import by.itechart.city_distance_service.util.Waiter;
import org.jgrapht.Graph;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CityDistanceStoreMultithreadTest {
    private CityDistanceStore cityDistanceStore;
    private Graph<String, WeightedEdge> graph;

    @Before
    public void init() {
        graph = mock(Graph.class);
        cityDistanceStore = new CityDistanceStoreImpl(graph);
    }

    @Test
    public void verifyWriteBlocksWrite() throws TimeoutException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<String> checkOrder = new ArrayList<>();
        String firstThreadId = UUID.randomUUID().toString();
        String secondThreadId = UUID.randomUUID().toString();
        String vertex1 = "03894u00ghre";
        String vertex2 = "dfg34tdfg";
        String vertex3 = "fgh68ured";
        String vertex4 = "asdc2341";
        when(graph.getEdge(any(), any())).thenReturn(new WeightedEdge());
        when(graph.addVertex(vertex1)).thenAnswer(invocationOnMock -> {
            Thread.sleep(2000);
            checkOrder.add(firstThreadId);

            return null;
        });
        when(graph.addVertex(vertex3)).thenAnswer(invocationOnMock -> {
            checkOrder.add(secondThreadId);

            return null;
        });

        executorService.execute(() -> cityDistanceStore.addEdge(vertex1, vertex2, 25L));
        Thread.sleep(50);
        executorService.execute(() -> cityDistanceStore.addEdge(vertex3, vertex4, 15L));
        waitAndCompareResults(Arrays.asList(firstThreadId, secondThreadId), checkOrder);
    }

    @Test
    public void verifyReadDoesNotBlockRead() throws TimeoutException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        List<String> checkOrder = new ArrayList<>();
        String firstThreadId = UUID.randomUUID().toString();
        String secondThreadId = UUID.randomUUID().toString();
        String thirdThreadId = UUID.randomUUID().toString();
        String vertex1 = "03894u00ghre";
        String vertex2 = "dfg34tdfg";
        String vertex3 = "fgh68ured";
        String vertex4 = "asdc2341";

        when(graph.containsVertex(any())).thenReturn(true);
        when(graph.getEdge(any(), any())).thenReturn(new WeightedEdge());
        when(graph.edgesOf(vertex1)).thenAnswer(invocationOnMock -> {
            Thread.sleep(4000);
            checkOrder.add(firstThreadId);

            return new HashSet<>();
        });
        when(graph.edgesOf(vertex2)).thenAnswer(invocationOnMock -> {
            Thread.sleep(2000);
            checkOrder.add(secondThreadId);

            return new HashSet<>();
        });

        when(graph.edgesOf(vertex3)).thenAnswer(invocationOnMock -> {
            checkOrder.add(thirdThreadId);

            return new HashSet<>();
        });

        executorService.execute(() -> cityDistanceStore.findAllPaths(vertex1, vertex4));
        Thread.sleep(50);
        executorService.execute(() -> cityDistanceStore.findAllPaths(vertex2, vertex4));
        Thread.sleep(50);
        executorService.execute(() -> cityDistanceStore.findAllPaths(vertex3, vertex4));
        waitAndCompareResults(Arrays.asList(thirdThreadId, secondThreadId, firstThreadId), checkOrder);
    }

    @Test
    public void verifyReadBlockWrite() throws TimeoutException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<String> checkOrder = new ArrayList<>();
        String firstThreadId = UUID.randomUUID().toString();
        String secondThreadId = UUID.randomUUID().toString();
        String vertex1 = "03894u00ghre";
        String vertex2 = "dfg34tdfg";
        String vertex4 = "asdc2341";

        when(graph.containsVertex(any())).thenReturn(true);
        when(graph.getEdge(any(), any())).thenReturn(new WeightedEdge());
        when(graph.edgesOf(vertex1)).thenAnswer(invocationOnMock -> {
            Thread.sleep(2000);
            checkOrder.add(firstThreadId);

            return new HashSet<>();
        });
        when(graph.addVertex(vertex2)).thenAnswer(invocationOnMock -> {
            checkOrder.add(secondThreadId);

            return true;
        });

        executorService.execute(() -> cityDistanceStore.findAllPaths(vertex1, vertex4));
        Thread.sleep(50);
        executorService.execute(() -> cityDistanceStore.addEdge(vertex2, vertex4, 15L));
        waitAndCompareResults(Arrays.asList(firstThreadId, secondThreadId), checkOrder);
    }

    @Test
    public void verifyWriteBlockRead() throws TimeoutException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<String> checkOrder = new ArrayList<>();
        String firstThreadId = UUID.randomUUID().toString();
        String secondThreadId = UUID.randomUUID().toString();
        String vertex1 = "03894u00ghre";
        String vertex2 = "dfg34tdfg";
        String vertex4 = "asdc2341";

        when(graph.containsVertex(any())).thenReturn(true);
        when(graph.getEdge(any(), any())).thenReturn(new WeightedEdge());
        when(graph.edgesOf(vertex1)).thenAnswer(invocationOnMock -> {
            checkOrder.add(firstThreadId);

            return new HashSet<>();
        });
        when(graph.addVertex(vertex2)).thenAnswer(invocationOnMock -> {
            Thread.sleep(2000);
            checkOrder.add(secondThreadId);

            return true;
        });

        executorService.execute(() -> cityDistanceStore.addEdge(vertex2, vertex4, 15L));
        Thread.sleep(50);
        executorService.execute(() -> cityDistanceStore.findAllPaths(vertex1, vertex4));
        waitAndCompareResults(Arrays.asList(secondThreadId, firstThreadId), checkOrder);
    }

    private void waitAndCompareResults(List<String> expected, List<String> result)
            throws TimeoutException, InterruptedException {
        Waiter.wait(() -> {
            if (result.size() != expected.size()) {
                return false;
            }
            assertEquals(expected, result);

            return true;
        });
    }
}
