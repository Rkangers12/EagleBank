package com.studio.eaglebank.services.mappers;

import com.studio.eaglebank.domain.entities.AddressEntity;
import com.studio.eaglebank.domain.models.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.studio.eaglebank.testdata.UserTestDataHelper.getAddress;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getAddressEntity;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AddressMapperTest {

    private AddressMapper unit;

    @BeforeEach
    public void setUp() {
        unit = new AddressMapper();
    }

    @Test
    void mapAddressToAddressEntity() {

        // Given
        Address address = getAddress();
        AddressEntity expected = getAddressEntity();

        // When
        AddressEntity actual = unit.mapAddressToAddressEntity(address);

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapAddressEntityToAddress() {

        // Given
        Address expected = getAddress();
        AddressEntity addressEntity = getAddressEntity();

        // When
        Address actual = unit.mapAddressEntityToAddress(addressEntity);

        // Then
        assertThat(actual).isEqualTo(expected);
    }
}