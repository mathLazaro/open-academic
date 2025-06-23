package com.github.open_academic_ad_hoc.model.relation;

import com.github.open_academic_ad_hoc.model.main.Organization;
import com.github.open_academic_ad_hoc.model.main.Work;
import com.github.open_academic_ad_hoc.model.pk.WorkOrganizationId;
import com.github.open_academic_ad_hoc.model.type.OrganizationWorkRole;
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
@Table(name = "tb_work_organizations")
public class WorkOrganization {

    @EmbeddedId
    private WorkOrganizationId id;

    @ManyToOne
    @MapsId("workId")
    private Work work;

    @ManyToOne
    @MapsId("organizationId")
    private Organization organization;

    @Enumerated(EnumType.STRING)
    private OrganizationWorkRole roleType;

}
