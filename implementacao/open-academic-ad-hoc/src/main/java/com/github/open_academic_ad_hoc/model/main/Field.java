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
@Table(name = "tb_fields")
public class Field {

    @Id
    private String id;

    private String title;

    private String description;

    private Integer worksCount;

    @ManyToOne
    @JoinColumn(name = "domain_id")
    private Domain domain;

    @OneToMany(mappedBy = "field")
    private List<Topic> topics = new ArrayList<>();

}
