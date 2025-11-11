package com.studio.eaglebank.domain.repositories;

import com.studio.eaglebank.domain.entities.AddressEntity;
import com.studio.eaglebank.domain.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.studio.eaglebank.testdata.UserTestDataHelper.getAddressEntity;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getUserEntity;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        AddressEntity addressEntity = getAddressEntity();
        UserEntity userEntity = getUserEntity(addressEntity);

        userRepository.save(userEntity);
    }

    @Test
    public void shouldFindUser() {

        // When
        Optional<UserEntity> optionalUser = userRepository.findAll()
                .stream()
                .findFirst();

        // Then
        assertThat(optionalUser.isPresent());
        assertThat(optionalUser.get().getPublicId()).isEqualTo("usr-abc123ef");
    }

    @Test
    public void findByPublicId() {

        String userPublicId = "usr-abc123ef";

        // When
        Optional<UserEntity> optionalUser = userRepository.findByPublicId(userPublicId);

        // Then
        assertThat(optionalUser.isPresent());
        assertThat(optionalUser.get().getPublicId()).isEqualTo(userPublicId);
    }
}