package com.github.open_academic_ad_hoc.repository;

import com.github.open_academic_ad_hoc.model.main.Role;
import com.github.open_academic_ad_hoc.model.pk.RoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, RoleId> {

}
