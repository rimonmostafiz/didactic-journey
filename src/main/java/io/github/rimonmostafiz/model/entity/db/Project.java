package io.github.rimonmostafiz.model.entity.db;

import io.github.rimonmostafiz.model.entity.common.EntityCommon;
import io.github.rimonmostafiz.model.entity.common.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Rimon Mostafiz
 */
@Entity
@NoArgsConstructor
@Table(name = "PROJECT")
@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = true)
public class Project extends EntityCommon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    @NotBlank(message = "error.project.name.blank}")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "{error.project.status.empty}")
    private Status status;

    @OneToOne
    @JoinColumn(name = "ASSIGNED_USER", referencedColumnName = "ID")
    private User assignedUser;

//    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Task> tasks;
}
