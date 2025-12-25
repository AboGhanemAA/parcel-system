package com.parcel.system.service;

import com.parcel.system.model.Parcel;
import com.parcel.system.model.ParcelStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class ParcelService {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public Parcel createParcel(Parcel parcel) {
        entityManager.persist(parcel);
        parcel.setParcelNumber("PRCL-" + parcel.getId());
        return entityManager.merge(parcel);
    }
    
    public Parcel updateParcel(Long id, Parcel updatedParcel) {
        Parcel existing = entityManager.find(Parcel.class, id);
        if (existing != null) {

            if (existing.getStatus() == ParcelStatus.DRAFT) {
                existing.setOriginPostalIndex(updatedParcel.getOriginPostalIndex());
                existing.setDate(updatedParcel.getDate());
                existing.setAttachmentCount(updatedParcel.getAttachmentCount());
                existing.setCost(updatedParcel.getCost());
                existing.setSender(updatedParcel.getSender());
                existing.setReceiver(updatedParcel.getReceiver());
                existing.setTransporter(updatedParcel.getTransporter());
                return entityManager.merge(existing);
            }
        }
        return null;
    }
    
    public boolean deleteParcel(Long id) {
        Parcel parcel = entityManager.find(Parcel.class, id);
        if (parcel != null && parcel.getStatus() == ParcelStatus.DRAFT) {
            entityManager.remove(parcel);
            return true;
        }
        return false;
    }
    
    public Parcel pushStatus(Long id, ParcelStatus newStatus) {
        Parcel parcel = entityManager.find(Parcel.class, id);
        if (parcel != null && isStatusTransitionValid(parcel.getStatus(), newStatus)) {
            parcel.setStatus(newStatus);
            return entityManager.merge(parcel);
        }
        return null;
    }
    
    private boolean isStatusTransitionValid(ParcelStatus current, ParcelStatus next) {
        return switch (current) {
            case DRAFT -> next == ParcelStatus.VIEW_BY_TRANSPORTER;
            case VIEW_BY_TRANSPORTER -> next == ParcelStatus.SENT || 
                                         next == ParcelStatus.REJECTED_BY_TRANSPORTER;
            case SENT -> next == ParcelStatus.VIEW_BY_RECEIVER;
            case VIEW_BY_RECEIVER -> next == ParcelStatus.COMPLETED || 
                                     next == ParcelStatus.REJECTED_BY_RECEIVER;
            default -> false;
        };
    }
    
    public Parcel findById(Long id) {
        return entityManager.find(Parcel.class, id);
    }
    
    public List<Parcel> findAll() {
        return entityManager.createQuery("SELECT p FROM Parcel p", Parcel.class)
                           .getResultList();
    }
}