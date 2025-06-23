package com.github.open_academic_ad_hoc.model.main;

import com.github.open_academic_ad_hoc.model.relation.WorkTopic;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_topics")
public class Topic {

    @Id
    private String id;

    private String title;

    private String description;

    private Integer worksCount;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;

    @OneToMany(mappedBy = "topic")
    private List<WorkTopic> workTopics;

    @ElementCollection
    @CollectionTable(name = "tb_topic_keywords", joinColumns = @JoinColumn(name = "topic_id"))
    @Column(name = "word")
    private Set<String> keywords;

}
