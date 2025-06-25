package com.github.open_academic_ad_hoc.model.main;

import com.github.open_academic_ad_hoc.model.Selectable;
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
@Table(name = "tb_domains")
public class Domain implements Selectable {

    @Id
    @Column(name = "id", nullable = false, length = Integer.MAX_VALUE)
    private String id;

    @Column(name = "title", nullable = false, length = Integer.MAX_VALUE)
    private String title;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ColumnDefault("0")
    @Column(name = "works_count")
    private Integer worksCount;

    @OneToMany(mappedBy = "domain")
    private Set<Field> field = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "domain")
    private Set<Organization> organization = new LinkedHashSet<>();

}