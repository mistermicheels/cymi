package com.mistermicheels.cymi.component.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mistermicheels.cymi.component.user.entity.User;

interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
