package com.github.open_academic_ad_hoc.model.pk;

import com.github.open_academic_ad_hoc.model.type.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RoleId implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = -6052275635792649688L;

    @Column(name = "organization_id", nullable = false, length = Integer.MAX_VALUE)
    private String organizationId;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RoleId entity = (RoleId) o;
        return Objects.equals(this.organizationId, entity.organizationId) &&
                Objects.equals(this.role, entity.role);
    }

    @Override
    public int hashCode() {

        return Objects.hash(organizationId, role);
    }

}