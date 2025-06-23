package com.github.open_academic_ad_hoc.model.main;

import com.github.open_academic_ad_hoc.model.relation.WorkOrganization;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_organizations")
public class Organization {

    @Id
    private String id;

    private String name;

    private String city;

    private String country;

    private String countryCode;

    private Integer worksCount;

    private Integer citedByCount;

    @OneToMany(mappedBy = "organization")
    private List<Role> roles;

    @OneToMany(mappedBy = "organization")
    private List<WorkOrganization> workOrganizations;

    @ManyToMany
    @JoinTable(
        name = "tb_organization_domains",
        joinColumns = @JoinColumn(name = "organization_id"),
        inverseJoinColumns = @JoinColumn(name = "domain_id")
    )
    private List<Domain> domains;

}
