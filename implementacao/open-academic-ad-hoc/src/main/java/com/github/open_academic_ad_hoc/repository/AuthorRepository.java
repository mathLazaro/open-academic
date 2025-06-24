package com.github.open_academic_ad_hoc.repository;

import com.github.open_academic_ad_hoc.model.main.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AuthorRepository extends JpaRepository<Author, String> {

}
