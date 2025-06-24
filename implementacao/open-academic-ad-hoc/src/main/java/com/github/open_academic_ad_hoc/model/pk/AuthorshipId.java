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
public class AuthorshipId implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = -4640541595313707297L;

    @Column(name = "work_id", nullable = false, length = Integer.MAX_VALUE)
    private String workId;

    @Column(name = "author_id", nullable = false, length = Integer.MAX_VALUE)
    private String authorId;

    @Column(name = "institution_id", nullable = false, length = Integer.MAX_VALUE)
    private String institutionId;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AuthorshipId entity = (AuthorshipId) o;
        return Objects.equals(this.institutionId, entity.institutionId) &&
                Objects.equals(this.authorId, entity.authorId) &&
                Objects.equals(this.workId, entity.workId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(institutionId, authorId, workId);
    }

}