package com.github.open_academic_ad_hoc.model.main;

import com.github.open_academic_ad_hoc.model.relation.Authorship;
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
@Table(name = "tb_authors")
public class Author {

    @Id
    @Column(name = "id", nullable = false, length = Integer.MAX_VALUE)
    private String id;

    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @ColumnDefault("0")
    @Column(name = "works_count")
    private Integer worksCount;

    @ColumnDefault("0")
    @Column(name = "cited_by_count")
    private Integer citedByCount;

    @OneToMany(mappedBy = "author")
    private Set<Authorship> authorships = new LinkedHashSet<>();

}