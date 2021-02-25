package io.github.rimonmostafiz.model.entity.activity;

import io.github.rimonmostafiz.model.entity.common.ActivityAction;
import io.github.rimonmostafiz.model.entity.common.ActivityCommon;
import io.github.rimonmostafiz.model.entity.common.TaskStatus;
import io.github.rimonmostafiz.model.entity.db.Task;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACTIVITY_ID")
    private Long activityId;

    @Column(name = "TASK_ID")
    private Long id;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(name = "PROJECT_ID")
    private Long project;

    @Column(name = "ASSIGNED_USER")
    private Long assignedUser;

    @Column(name = "DUE_DATE")
    private LocalDate dueDate;

    private static ActivityTask of(Task entity) {
        ActivityTask activity = new ActivityTask();

        activity.setId(entity.getId());
        activity.setDescription(entity.getDescription());
        activity.setStatus(entity.getStatus());
        activity.setProject(entity.getProject().getId());
        if (entity.getAssignedUser() != null) {
            activity.setAssignedUser(entity.getAssignedUser().getId());
        }
        activity.setDueDate(entity.getDueDate());

        ActivityCommon.mapper(activity, entity);

        return activity;
    }

    public static ActivityTask of(Task task, String activityUser, ActivityAction activityAction) {
        ActivityTask activity = of(task);
        ActivityCommon.mapper(activity, activityUser, activityAction);
        return activity;
    }
}
