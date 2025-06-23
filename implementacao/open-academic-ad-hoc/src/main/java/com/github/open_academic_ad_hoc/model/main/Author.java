package com.github.open_academic_ad_hoc.model.main;

import com.github.open_academic_ad_hoc.model.relation.Authorship;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "tb_authors")
public class Author {

    @Id
    private String id;

    private String name;

    private Integer worksCount;

    private Integer citedByCount;

    @OneToMany(mappedBy = "author")
    private List<Authorship> authorships;

}
