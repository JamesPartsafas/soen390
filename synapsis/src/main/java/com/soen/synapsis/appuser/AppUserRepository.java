package com.soen.synapsis.appuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByEmail(String email);

    List<AppUser> findByNameContainingIgnoreCaseAndIdNot(String name, Long id);
}
