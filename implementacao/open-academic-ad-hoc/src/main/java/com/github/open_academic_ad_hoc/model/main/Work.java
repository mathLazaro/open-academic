package com.github.open_academic_ad_hoc.model.main;

import com.github.open_academic_ad_hoc.model.Selectable;
import com.github.open_academic_ad_hoc.model.relation.Authorship;
import com.github.open_academic_ad_hoc.model.relation.WorkOrganization;
import com.github.open_academic_ad_hoc.model.relation.WorkTopic;
import com.github.open_academic_ad_hoc.model.type.WorkType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_works")
public class Work implements Selectable {

    @Id
    @Column(name = "id", nullable = false, length = Integer.MAX_VALUE)
    private String id;

    @Column(name = "title", nullable = false, length = Integer.MAX_VALUE)
    private String title;

    @ColumnDefault("false")
    @Column(name = "is_open")
    private Boolean isOpen;

    @ColumnDefault("0")
    @Column(name = "referenced_works_count")
    private Integer referencedWorksCount;

    @ColumnDefault("0")
    @Column(name = "cited_by_count")
    private Integer citedByCount;

    @ColumnDefault("0")
    @Column(name = "fwci")
    private Double fwci;

    @Column(name = "publish_date", nullable = false)
    private LocalDate publishDate;

    @OneToMany(mappedBy = "work")
    private Set<Authorship> authorship = new LinkedHashSet<>();

    @OneToMany(mappedBy = "work")
    private Set<WorkOrganization> workOrganization = new LinkedHashSet<>();

    @OneToMany(mappedBy = "work")
    private Set<WorkTopic> workTopic = new LinkedHashSet<>();

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private WorkType type;

}