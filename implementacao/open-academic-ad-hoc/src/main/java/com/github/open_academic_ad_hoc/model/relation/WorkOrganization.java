package com.github.open_academic_ad_hoc.model.relation;

import com.github.open_academic_ad_hoc.model.main.Organization;
import com.github.open_academic_ad_hoc.model.main.Work;
import com.github.open_academic_ad_hoc.model.pk.WorkOrganizationId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_work_organizations")
public class WorkOrganization {

    @EmbeddedId
    private WorkOrganizationId id;

    @MapsId("workId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "work_id", nullable = false)
    private Work work;

    @MapsId("organizationId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

}