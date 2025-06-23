package com.github.open_academic_ad_hoc.model.pk;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class WorkTopicId implements Serializable {

    private String workId;

    private String topicId;

}
