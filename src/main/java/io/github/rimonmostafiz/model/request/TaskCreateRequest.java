package io.github.rimonmostafiz.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author Rimon Mostafiz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskCreateRequest {
    @NotBlank(message = "error.task.description.blank")
    private String description;

    @NotBlank(message = "error.task.status.blank")
    private String status;

    @NotNull(message = "error.task.project.id.null")
    private Long projectId;

    private LocalDate dueDate;
}
