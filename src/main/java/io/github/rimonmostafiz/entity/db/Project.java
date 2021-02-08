package io.github.rimonmostafiz.entity.db;

import io.github.rimonmostafiz.entity.common.EntityCommon;
import io.github.rimonmostafiz.entity.common.Status;
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
@Table(name = "PROJECT")
@Data(staticConstructor = "of")
@EqualsAndHashCode(callSuper = true)
public class Project extends EntityCommon {
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    @NotBlank(message = "{error.project.name.blank}")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    @NotBlank(message = "{error.project.status.empty}")
    private Status status;

//    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Task> tasks;
}
