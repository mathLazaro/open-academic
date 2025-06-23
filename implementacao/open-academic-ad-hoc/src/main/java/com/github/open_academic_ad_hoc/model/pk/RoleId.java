package com.github.open_academic_ad_hoc.model.pk;

import com.github.open_academic_ad_hoc.model.type.OrganizationType;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class RoleId implements Serializable {

    private String organizationId;

    private OrganizationType role;

}
