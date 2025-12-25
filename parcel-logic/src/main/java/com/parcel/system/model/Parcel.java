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
public class Parcel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String parcelNumber;  
    
    private String originPostalIndex;
    private LocalDate date;
    private Integer attachmentCount;
    private Double cost;
    
    @Enumerated(EnumType.STRING)
    private ParcelStatus status = ParcelStatus.DRAFT;
    
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Person sender;
    
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Person receiver;
    
    @ManyToOne
    @JoinColumn(name = "transporter_id")
    private Organization transporter;
}