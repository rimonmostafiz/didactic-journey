package io.github.rimonmostafiz.entity.activity;

import io.github.rimonmostafiz.entity.common.ActivityAction;
import io.github.rimonmostafiz.entity.common.ActivityCommon;
import io.github.rimonmostafiz.entity.common.Status;
import io.github.rimonmostafiz.entity.db.Project;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @GeneratedValue(generator = "ID_GENERATOR")
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

    private static ActivityProject of(Project entity) {
        ActivityProject activity = new ActivityProject();

        activity.setId(entity.getId());
        activity.setName(entity.getName());
        activity.setDescription(entity.getDescription());
        activity.setStatus(entity.getStatus());

        ActivityCommon.mapper(activity, entity);

        return activity;
    }

    public ActivityProject(Project project, String activityUse,
                           ActivityAction activityAction, LocalDateTime activityTime) {
        ActivityProject activity = of(project);
        ActivityCommon.mapper(activity, activityUse, activityAction, activityTime);
    }
}
