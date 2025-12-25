package com.parcel.system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@XmlRootElement
public class Person {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String fullName;
    private LocalDate birthDate;
    private String passportNumber;
    private LocalDate passportIssueDate;
    
    @Embedded
    private Address address;
    
    @Embedded
    private Credentials credentials;
}