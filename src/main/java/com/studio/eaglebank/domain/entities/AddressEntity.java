package com.studio.eaglebank.domain.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AddressEntity {

    private String line1;
    private String line2;
    private String line3;
    private String town;
    private String county;
    private String postcode;
}