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
public class AuthorshipId implements Serializable {

    private String workId;

    private String authorId;

    private String institutionId;

}
