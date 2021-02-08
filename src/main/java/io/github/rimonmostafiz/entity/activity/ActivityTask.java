package io.github.rimonmostafiz.entity.activity;

import io.github.rimonmostafiz.entity.common.ActivityAction;
import io.github.rimonmostafiz.entity.common.ActivityCommon;
import io.github.rimonmostafiz.entity.common.TaskStatus;
import io.github.rimonmostafiz.entity.db.Task;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author Rimon Mostafiz
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "ACTIVITY_TASK")
@EqualsAndHashCode(callSuper = true)
public class ActivityTask extends ActivityCommon {
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    @Column(name = "ACTIVITY_ID")
    private Long activityId;

    @Column(name = "TASK_ID")
    private Long id;

    @Column(name = "DESCRIPTION")
    @NotBlank(message = "{error.task.description.blank}")
    private String description;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    @NotBlank(message = "{error.task.status.blank}")
    private TaskStatus status;

    @Column(name = "PROJECT_ID")
    private Long project;

    private static ActivityTask of(Task entity) {
        ActivityTask activity = new ActivityTask();

        activity.setId(entity.getId());
        activity.setDescription(entity.getDescription());
        activity.setStatus(entity.getStatus());
        activity.setProject(entity.getProject().getId());

        ActivityCommon.mapper(activity, entity);

        return activity;
    }

    public ActivityTask(Task task, String activityUser, ActivityAction activityAction, LocalDateTime activityTime) {
        ActivityTask activity = of(task);
        ActivityCommon.mapper(activity, activityUser, activityAction);
    }
}
