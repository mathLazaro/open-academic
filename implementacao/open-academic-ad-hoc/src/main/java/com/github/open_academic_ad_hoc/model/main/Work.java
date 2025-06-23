package com.github.open_academic_ad_hoc.model.main;

import com.github.open_academic_ad_hoc.model.relation.Authorship;
import com.github.open_academic_ad_hoc.model.relation.WorkOrganization;
import com.github.open_academic_ad_hoc.model.relation.WorkTopic;
import com.github.open_academic_ad_hoc.model.type.WorkType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_works")
public class Work {

    @Id
    private String id;

    private String title;

    private Boolean isOpen;

    private Integer referencedWorksCount;

    private Integer citedByCount;

    private Double fwci;

    private LocalDate publishDate;

    @Enumerated(EnumType.STRING)
    private WorkType type;

    @OneToMany(mappedBy = "work")
    private List<Authorship> authorships;

    @OneToMany(mappedBy = "work")
    private List<WorkOrganization> organizations;

    @OneToMany(mappedBy = "work")
    private List<WorkTopic> topics;


}




