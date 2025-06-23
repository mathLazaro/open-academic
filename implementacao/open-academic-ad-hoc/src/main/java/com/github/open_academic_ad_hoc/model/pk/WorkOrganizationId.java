package com.github.open_academic_ad_hoc.model.pk;

import com.github.open_academic_ad_hoc.model.type.OrganizationWorkRole;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class WorkOrganizationId implements Serializable {

    private String workId;

    private String organizationId;

    private OrganizationWorkRole roleType;

}
