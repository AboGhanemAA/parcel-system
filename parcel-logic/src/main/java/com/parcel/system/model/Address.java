package com.parcel.system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@XmlRootElement
public class Address {
    private String city;
    private String district;
    private String street;
    private String house;
    private String apartment;
    private String postalCode;
}