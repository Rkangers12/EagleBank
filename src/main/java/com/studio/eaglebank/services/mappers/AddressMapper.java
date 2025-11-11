package com.studio.eaglebank.services.mappers;

import com.studio.eaglebank.domain.entities.AddressEntity;
import com.studio.eaglebank.domain.models.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressEntity mapAddressToAddressEntity(Address userAddress) {
        return AddressEntity.builder()
                .line1(userAddress.line1())
                .line2(userAddress.line2())
                .line3(userAddress.line3())
                .town(userAddress.town())
                .county(userAddress.county())
                .postcode(userAddress.postcode())
                .build();
    }

    public Address mapAddressEntityToAddress(AddressEntity addressEntity) {
        return new Address(
                addressEntity.getLine1(),
                addressEntity.getLine2(),
                addressEntity.getLine3(),
                addressEntity.getTown(),
                addressEntity.getCounty(),
                addressEntity.getPostcode());
    }
}
