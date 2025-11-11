package com.studio.eaglebank.services.mappers;

import com.studio.eaglebank.domain.entities.AddressEntity;
import com.studio.eaglebank.domain.models.Address;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AddressMapperTest {

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

    public static Address getAddress() {
        return new Address(
                "23 Maple Crescent",
                "Flat 4B",
                "",
                "Nottingham",
                "Nottinghamshire",
                "NG1 3EZ"
        );
    }

    public static AddressEntity getAddressEntity() {
        return AddressEntity.builder()
                .line1("23 Maple Crescent")
                .line2("Flat 4B")
                .line3("")
                .town("Nottingham")
                .county("Nottinghamshire")
                .postcode("NG1 3EZ")
                .build();
    }
}