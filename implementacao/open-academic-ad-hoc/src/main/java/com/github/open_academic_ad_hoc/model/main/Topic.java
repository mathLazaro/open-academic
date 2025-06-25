package com.github.open_academic_ad_hoc.model.main;

import com.github.open_academic_ad_hoc.model.Selectable;
import com.github.open_academic_ad_hoc.model.relation.WorkTopic;
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
@Table(name = "tb_topics")
public class Topic implements Selectable {

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;

    @ElementCollection
    @CollectionTable(name = "tb_topic_keywords", joinColumns = @JoinColumn(name = "topic_id"))
    @Column(name = "word")
    private Set<String> keyword;

    @OneToMany(mappedBy = "topic")
    private Set<WorkTopic> workTopic = new LinkedHashSet<>();

}