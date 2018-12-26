package by.itechart.city_distance_service.service;

import by.itechart.city_distance_service.dto.LinkDto;
import by.itechart.city_distance_service.dto.PathDto;
import by.itechart.city_distance_service.exception.PathNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CityDistanceServiceTest {
    @InjectMocks
    private CityDistanceServiceImpl cityDistanceService;
    @Mock
    private CityDistanceStore cityDistanceStore;

    @Test
    public void addPathSucceedWithValidInput() {
        LinkDto linkDto = new LinkDto();
        linkDto.setSource("1");
        linkDto.setTarget("2");
        linkDto.setDistance(35L);

        cityDistanceService.addLink(linkDto);
        verify(cityDistanceStore).addEdge(linkDto.getSource(), linkDto.getTarget(), linkDto.getDistance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPathFailWhenPointsAreTheSame() {
        LinkDto linkDto = new LinkDto();
        linkDto.setSource("1");
        linkDto.setTarget("1");
        linkDto.setDistance(35L);

        cityDistanceService.addLink(linkDto);
    }

    @Test
    public void getAllPathsSucceedWhenAtLeastOnePathExists() {
        String testSource = "fgy456tresg34";
        String testTarget = "34u09egh0934";
        List<PathDto> expected = Arrays.asList(new PathDto());
        when(cityDistanceStore.findAllPaths(testSource, testTarget)).thenReturn(expected);

        List<PathDto> result = cityDistanceService.findAllPaths(testSource, testTarget);
        assertEquals(expected, result);
    }

    @Test(expected = PathNotFoundException.class)
    public void getAllPathsFailWhenPathDoesntExist() {
        String testSource = "fgy456tresg34";
        String testTarget = "34u09egh0934";
        when(cityDistanceStore.findAllPaths(testSource, testTarget)).thenReturn(new ArrayList<>());

        cityDistanceService.findAllPaths(testSource, testTarget);
    }
}
