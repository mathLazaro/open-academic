package com.github.open_academic_ad_hoc.model.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class WorkTopicId implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = -5359066160502052175L;

    @Column(name = "topic_id", nullable = false, length = Integer.MAX_VALUE)
    private String topicId;

    @Column(name = "work_id", nullable = false, length = Integer.MAX_VALUE)
    private String workId;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        WorkTopicId entity = (WorkTopicId) o;
        return Objects.equals(this.topicId, entity.topicId) &&
                Objects.equals(this.workId, entity.workId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(topicId, workId);
    }

}