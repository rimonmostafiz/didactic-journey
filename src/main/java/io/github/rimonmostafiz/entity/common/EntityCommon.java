package io.github.rimonmostafiz.entity.common;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author Rimon Mostafiz
 */
@Data
@MappedSuperclass
public abstract class EntityCommon implements Serializable {
    @Column(name = "CREATED_BY")
    protected String createdBy;

    @Column(name = "EDITED_BY")
    protected String editedBy;

    @Column(name = "CREATE_TIME")
    protected LocalDateTime createTime;

    @Column(name = "EDIT_TIME")
    protected LocalDateTime editTime;

    @Version
    @Column(name = "INTERNAL_VERSION")
    protected Long version;

    public abstract Long getId();

    public abstract void setId(Long id);

    @PrePersist
    public void beforeSave() {
        if (this.createTime == null) {
            this.createTime = LocalDateTime.now(ZoneId.of("BST"));
        }
    }

    @PreUpdate
    public void beforeEdit() {
        this.editTime = LocalDateTime.now(ZoneId.of("BST"));
    }

    @PreRemove
    public void beforeDelete() {
        this.editTime = LocalDateTime.now(ZoneId.of("BST"));
    }
}
