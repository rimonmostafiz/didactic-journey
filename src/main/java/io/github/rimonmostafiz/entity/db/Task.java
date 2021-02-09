package io.github.rimonmostafiz.entity.db;

import io.github.rimonmostafiz.entity.common.EntityCommon;
import io.github.rimonmostafiz.entity.common.TaskStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * @author Rimon Mostafiz
 */

@Entity
@NoArgsConstructor
@Table(name = "TASK")
@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = true)
public class Task extends EntityCommon {
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    @Column(name = "ID")
    private Long id;

    @Column(name = "DESCRIPTION")
    @NotBlank(message = "{error.task.description.blank}")
    private String description;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    @NotBlank(message = "{error.task.status.blank}")
    private TaskStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID", nullable = false)
    private Project project;
}