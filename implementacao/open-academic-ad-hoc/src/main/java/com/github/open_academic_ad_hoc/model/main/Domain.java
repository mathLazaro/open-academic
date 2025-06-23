package com.github.open_academic_ad_hoc.model.main;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_domains")
public class Domain {

    @Id
    private String id;

    private String title;

    private String description;

    private Integer worksCount;

    @OneToMany(mappedBy = "domain")
    private List<Field> fields = new ArrayList<>();

    @ManyToMany(mappedBy = "domains")
    private List<Organization> organizations = new ArrayList<>();

}
