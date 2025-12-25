package com.parcel.system.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.parcel.system.model.ParcelStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class ParcelDTO {
    private Long id;
    private String parcelNumber;
    private String originPostalIndex;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    
    private Integer attachmentCount;
    private Double cost;
    private ParcelStatus status;
    
    private Long senderId;
    private Long receiverId;
    private Long transporterId;
}