package io.github.rimonmostafiz.model.entity.activity;

import io.github.rimonmostafiz.model.entity.common.ActivityAction;
import io.github.rimonmostafiz.model.entity.common.ActivityCommon;
import io.github.rimonmostafiz.model.entity.common.Status;
import io.github.rimonmostafiz.model.entity.db.Project;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Rimon Mostafiz
 */
@Data
@Entity
@Table(name = "ACTIVITY_PROJECT")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ActivityProject extends ActivityCommon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACTIVITY_ID")
    private Long activityId;

    @Column(name = "PROJECT_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "ASSIGNED_USER")
    private Long assignedUser;

    private static ActivityProject of(Project entity) {
        ActivityProject activity = new ActivityProject();

        activity.setId(entity.getId());
        activity.setName(entity.getName());
        activity.setDescription(entity.getDescription());
        activity.setStatus(entity.getStatus());
        activity.setAssignedUser(entity.getAssignedUser().getId());

        ActivityCommon.mapper(activity, entity);

        return activity;
    }

    public static ActivityProject of(Project project, String activityUse, ActivityAction activityAction) {
        ActivityProject activity = of(project);
        ActivityCommon.mapper(activity, activityUse, activityAction);
        return activity;
    }
}
