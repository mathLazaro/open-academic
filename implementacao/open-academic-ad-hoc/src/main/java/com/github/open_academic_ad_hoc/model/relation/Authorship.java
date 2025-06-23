package com.github.open_academic_ad_hoc.model.relation;

import com.github.open_academic_ad_hoc.model.main.Author;
import com.github.open_academic_ad_hoc.model.main.Organization;
import com.github.open_academic_ad_hoc.model.main.Work;
import com.github.open_academic_ad_hoc.model.pk.AuthorshipId;
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
@Table(name = "tb_authorships")
public class Authorship {

    @EmbeddedId
    private AuthorshipId id;

    @ManyToOne
    @MapsId("workId")
    @JoinColumn(name = "work_id")
    private Work work;

    @ManyToOne
    @MapsId("authorId")
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne
    @JoinColumn(name = "institution_id")
    private Organization institution;

}
