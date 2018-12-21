package by.itechart.turvo_task.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class LinkDto {
    @NotEmpty
    private String source;
    @NotEmpty
    private String target;
    @Min(0)
    @NotNull
    private Long distance;
}
