package by.itechart.city_distance_service.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jgrapht.graph.DefaultWeightedEdge;

@Data
@EqualsAndHashCode(callSuper = true)
public class WeightedEdge extends DefaultWeightedEdge {
    private Long distance;

    public String getOppositeVertex(final String source) {
        return source.equals(this.getSource()) ? this.getTarget().toString() : this.getSource().toString();
    }
}
