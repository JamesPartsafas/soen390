package com.soen.synapsis.integration;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserRepository;
import com.soen.synapsis.appuser.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
                Role.CANDIDATE);
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

    @Test
    void itShouldFindUsersByNameIfUserNameExists() {
        String name = "oh";
        AppUser appUser1 = new AppUser(1L,
                "John User 1",
                "1234",
                "joeuser1test@mail.com",
                Role.CANDIDATE);

        AppUser appUser2 = new AppUser(2L,
                "John User 2",
                "1234",
                "joeuser2test@mail.com",
                Role.CANDIDATE);

        underTest.saveAll(Arrays.asList(appUser1, appUser2));
        List<AppUser> users = underTest.findByNameContainingIgnoreCaseAndIdNot(name, 2L);
        assertTrue(users.size() == 1);
    }

    @Test
    void itShouldNotFindUsersByNameIfUserNameDoesNotExist() {
        String name = "oh";
        List<AppUser> users = underTest.findByNameContainingIgnoreCaseAndIdNot(name, 2L);
        assertTrue(users.size() == 0);
    }
}