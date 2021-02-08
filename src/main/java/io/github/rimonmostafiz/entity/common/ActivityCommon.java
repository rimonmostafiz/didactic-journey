package io.github.rimonmostafiz.entity.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Rimon Mostafiz
 */
@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
@AttributeOverride(name = "version", column = @Column(name = "INTERNAL_VERSION"))
public abstract class ActivityCommon extends EntityCommon implements Serializable {
    @Column(name = "ACTIVITY_USER")
    protected String activityUser;

    @Column(name = "ACTIVITY_ACTION")
    protected int activityAction;

    @Column(name = "ACTIVITY_TIME")
    protected LocalDateTime activityTime;

    @Column(name = "INTERNAL_VERSION")
    protected Long version;

    public static void mapper(ActivityCommon activity, String activityUser,
                              ActivityAction activityAction, LocalDateTime activityTime) {
        activity.setActivityUser(activityUser);
        activity.setActivityAction(activityAction.getAction());
        activity.setActivityTime(activityTime);
    }

    public static void mapper(ActivityCommon activity, EntityCommon entity) {
        activity.setCreatedBy(entity.getCreatedBy());
        activity.setCreateTime(entity.getCreateTime());
        activity.setEditedBy(entity.getEditedBy());
        activity.setEditTime(entity.getEditTime());
        activity.setVersion(entity.getVersion());
    }

    public abstract Long getId();
}
