package io.github.rimonmostafiz.entity.activity;

import io.github.rimonmostafiz.entity.common.ActivityAction;
import io.github.rimonmostafiz.entity.common.ActivityCommon;
import io.github.rimonmostafiz.entity.common.Status;
import io.github.rimonmostafiz.entity.db.User;
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
@NoArgsConstructor
@Table(name = "ACTIVITY_USER")
@EqualsAndHashCode(callSuper = true)
public class ActivityUser extends ActivityCommon {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    @Column(name = "ACTIVITY_ID")
    private Long activityId;

    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;

    private static ActivityUser of(User entity) {
        ActivityUser activity = new ActivityUser();

        activity.setId(entity.getId());
        activity.setUsername(entity.getUsername());
        activity.setEmail(entity.getEmail());
        activity.setPassword(entity.getPassword());
        activity.setFirstName(entity.getFirstName());
        activity.setLastName(entity.getLastName());
        activity.setStatus(entity.getStatus());

        ActivityCommon.mapper(activity, entity);
        return activity;
    }

    public ActivityUser(User user, String activityUser, ActivityAction activityAction, LocalDateTime activityTime) {
        ActivityUser activity = of(user);
        ActivityCommon.mapper(activity, activityUser, activityAction);
    }

}
