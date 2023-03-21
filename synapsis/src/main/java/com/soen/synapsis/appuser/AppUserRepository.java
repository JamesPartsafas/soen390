package com.soen.synapsis.appuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository layer for interfacing with AppUser database table.
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    /**
     * Finds user by their email.
     * @param email Used to search for user.
     * @return User with the requested email.
     */
    AppUser findByEmail(String email);

    /**
     * Finds user by verifying if their name contains some string,
     * and excludes any user who has the passed id or the passed role.
     * @param name The name to search for.
     * @param id The user IDs to exclude.
     * @param role The user roles to exclude.
     * @return A list of AppUsers who meet search requirements.
     */
    List<AppUser> findByNameContainingIgnoreCaseAndIdNotAndRoleNot(String name, Long id, Role role);
}
