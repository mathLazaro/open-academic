package com.github.open_academic_ad_hoc.repository;

import com.github.open_academic_ad_hoc.model.main.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, String> {

}
