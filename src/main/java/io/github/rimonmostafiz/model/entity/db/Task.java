package io.github.rimonmostafiz.model.entity.db;

import io.github.rimonmostafiz.model.entity.common.EntityCommon;
import io.github.rimonmostafiz.model.entity.common.TaskStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author Rimon Mostafiz
 */

@Entity
@ToString
@NoArgsConstructor
@Table(name = "TASK")
@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = true)
public class Task extends EntityCommon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DESCRIPTION")
    @NotBlank(message = "{error.task.description.blank}")
    private String description;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "{error.task.status.blank}")
    private TaskStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID", nullable = false)
    @NotNull(message = "{error.task.project.null")
    private Project project;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ASSIGNED_USER", referencedColumnName = "ID")
    private User assignedUser;

    @Column(name = "DUE_DATE")
    private LocalDate dueDate;
}
