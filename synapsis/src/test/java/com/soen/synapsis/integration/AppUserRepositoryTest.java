package com.soen.synapsis.integration;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserRepository;
import com.soen.synapsis.appuser.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindByEmail() {
        String email = "joeusertest@mail.com";
        AppUser appUser = new AppUser(1L,
                "Joe User",
                "1234",
                email,
                Role.USER);
        underTest.save(appUser);

        AppUser foundAppUser = underTest.findByEmail(email);

        assertThat(foundAppUser).isNotNull();
    }

    @Test
    void itShouldNotFindByEmailIfUserDoesNotExist() {
        String email = "joeusertest@mail.com";

        AppUser foundAppUser = underTest.findByEmail(email);

        assertThat(foundAppUser).isNull();
    }
}