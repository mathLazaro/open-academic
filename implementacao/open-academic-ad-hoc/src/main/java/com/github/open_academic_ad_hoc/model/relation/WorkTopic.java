package com.github.open_academic_ad_hoc.model.relation;

import com.github.open_academic_ad_hoc.model.Selectable;
import com.github.open_academic_ad_hoc.model.main.Topic;
import com.github.open_academic_ad_hoc.model.main.Work;
import com.github.open_academic_ad_hoc.model.pk.WorkTopicId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_work_topics")
public class WorkTopic implements Selectable {

    @EmbeddedId
    private WorkTopicId id;

    @MapsId("topicId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @MapsId("workId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "work_id", nullable = false)
    private Work work;

    @ColumnDefault("0")
    @Column(name = "score")
    private Double score;

}