package com.github.open_academic_ad_hoc.model.relation;

import com.github.open_academic_ad_hoc.model.main.Topic;
import com.github.open_academic_ad_hoc.model.main.Work;
import com.github.open_academic_ad_hoc.model.pk.WorkTopicId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_topics")
public class WorkTopic {

    @EmbeddedId
    private WorkTopicId id;

    @ManyToOne
    @MapsId("workId")
    private Work work;

    @ManyToOne
    @MapsId("topicId")
    private Topic topic;

    private Double score;

}
