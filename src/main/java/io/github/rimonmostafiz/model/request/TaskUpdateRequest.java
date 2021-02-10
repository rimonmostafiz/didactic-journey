package io.github.rimonmostafiz.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.rimonmostafiz.entity.common.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * @author Rimon Mostafiz
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskUpdateRequest {
    @NotBlank(message = "{error.task.description.blank")
    private String description;

    @NotBlank(message = "{error.task.status.blank}")
    private TaskStatus status;

    @NotBlank(message = "{error.task.project.id.blank")
    private Long projectId;

    private Long assignedUser;

    private LocalDate dueDate;
}
