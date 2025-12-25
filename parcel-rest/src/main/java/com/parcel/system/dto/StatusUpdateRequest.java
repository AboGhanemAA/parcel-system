package com.parcel.system.dto;

import com.parcel.system.model.ParcelStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class StatusUpdateRequest {
    private ParcelStatus newStatus;
}