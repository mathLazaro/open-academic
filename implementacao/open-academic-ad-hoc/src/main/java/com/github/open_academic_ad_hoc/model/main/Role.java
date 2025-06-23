package com.github.open_academic_ad_hoc.model.main;

import com.github.open_academic_ad_hoc.model.pk.RoleId;
import com.github.open_academic_ad_hoc.model.type.OrganizationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_roles")
public class Role {

    @EmbeddedId
    private RoleId id;

    @ManyToOne
    @MapsId("organizationId")
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private OrganizationType role;

    private Integer worksCount;

}
