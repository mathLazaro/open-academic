package com.github.open_academic_ad_hoc.model.main;

import com.github.open_academic_ad_hoc.model.pk.RoleId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_roles")
public class Role {

    @EmbeddedId
    private RoleId id;

    @MapsId("organizationId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ColumnDefault("0")
    @Column(name = "works_count")
    private Integer worksCount;

}