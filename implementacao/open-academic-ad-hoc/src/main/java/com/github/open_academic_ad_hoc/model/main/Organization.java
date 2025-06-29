package com.github.open_academic_ad_hoc.model.main;

import com.github.open_academic_ad_hoc.model.Selectable;
import com.github.open_academic_ad_hoc.model.relation.Authorship;
import com.github.open_academic_ad_hoc.model.relation.WorkOrganization;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_organizations")
public class Organization implements Selectable {

    @Id
    @Column(name = "id", nullable = false, length = Integer.MAX_VALUE)
    private String id;

    @Column(name = "name", length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "city", length = Integer.MAX_VALUE)
    private String city;

    @Column(name = "country", length = Integer.MAX_VALUE)
    private String country;

    @Column(name = "country_code", length = Integer.MAX_VALUE)
    private String countryCode;

    @ColumnDefault("0")
    @Column(name = "works_count")
    private Integer worksCount;

    @ColumnDefault("0")
    @Column(name = "cited_by_count")
    private Integer citedByCount;

    @OneToMany(mappedBy = "organization")
    private Set<Authorship> authorship = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(
            name = "tb_organization_domains",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "domain_id")
    )
    private Set<Domain> domain;

    @OneToMany(mappedBy = "organization")
    private Set<Role> role = new LinkedHashSet<>();

    @OneToMany(mappedBy = "organization")
    private Set<WorkOrganization> workOrganization = new LinkedHashSet<>();

}