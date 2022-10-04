package com.mistermicheels.cymi.component.group;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mistermicheels.cymi.component.group.entity.Group;

interface GroupRepository extends JpaRepository<Group, Long> {

}
