package com.parcel.system.rest;

import com.parcel.system.dto.*;
import com.parcel.system.model.Parcel;
import com.parcel.system.model.Person;
import com.parcel.system.model.Organization;
import com.parcel.system.service.ParcelService;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/parcels")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ParcelResource {
    
    @Inject
    private ParcelService parcelService;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private ParcelDTO toDTO(Parcel parcel) {
        return new ParcelDTO(
            parcel.getId(),
            parcel.getParcelNumber(),
            parcel.getOriginPostalIndex(),
            parcel.getDate(),
            parcel.getAttachmentCount(),
            parcel.getCost(),
            parcel.getStatus(),
            parcel.getSender() != null ? parcel.getSender().getId() : null,
            parcel.getReceiver() != null ? parcel.getReceiver().getId() : null,
            parcel.getTransporter() != null ? parcel.getTransporter().getId() : null
        );
    }
    
    private Parcel toEntity(CreateParcelRequest request) {
        Parcel parcel = new Parcel();
        parcel.setOriginPostalIndex(request.getOriginPostalIndex());
        parcel.setDate(request.getDate());
        parcel.setAttachmentCount(request.getAttachmentCount());
        parcel.setCost(request.getCost());
        
        if (request.getSenderId() != null) {
            parcel.setSender(entityManager.find(Person.class, request.getSenderId()));
        }
        if (request.getReceiverId() != null) {
            parcel.setReceiver(entityManager.find(Person.class, request.getReceiverId()));
        }
        if (request.getTransporterId() != null) {
            parcel.setTransporter(entityManager.find(Organization.class, request.getTransporterId()));
        }
        
        return parcel;
    }
    
    // 1. Create Parcel (POST)
    @POST
    @Path("/")
    public Response createParcel(CreateParcelRequest request) {
        try {
            Parcel parcel = toEntity(request);
            Parcel created = parcelService.createParcel(parcel);
            return Response.ok(ApiResponse.success("Parcel created successfully", toDTO(created)))
                         .status(Response.Status.CREATED)
                         .build();
        } catch (Exception e) {
            return Response.ok(ApiResponse.error("Failed to create parcel: " + e.getMessage()))
                         .status(Response.Status.BAD_REQUEST)
                         .build();
        }
    }
    
    // 2. Get All Parcels (GET)
    @GET
    @Path("/")
    public Response getAllParcels() {
        try {
            List<ParcelDTO> parcels = parcelService.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
            return Response.ok(ApiResponse.success("Parcels retrieved successfully", parcels)).build();
        } catch (Exception e) {
            return Response.ok(ApiResponse.error("Failed to retrieve parcels: " + e.getMessage()))
                         .status(Response.Status.INTERNAL_SERVER_ERROR)
                         .build();
        }
    }
    
    // 3. Get Parcel by ID (GET)
    @GET
    @Path("/{id}")
    public Response getParcelById(@PathParam("id") Long id) {
        try {
            Parcel parcel = parcelService.findById(id);
            if (parcel == null) {
                return Response.ok(ApiResponse.error("Parcel not found with id: " + id))
                             .status(Response.Status.NOT_FOUND)
                             .build();
            }
            return Response.ok(ApiResponse.success("Parcel retrieved successfully", toDTO(parcel))).build();
        } catch (Exception e) {
            return Response.ok(ApiResponse.error("Failed to retrieve parcel: " + e.getMessage()))
                         .status(Response.Status.INTERNAL_SERVER_ERROR)
                         .build();
        }
    }
    
    // 4. Update Parcel (PUT)
    @PUT
    @Path("/{id}")
    public Response updateParcel(@PathParam("id") Long id, CreateParcelRequest request) {
        try {
            Parcel parcel = toEntity(request);
            Parcel updated = parcelService.updateParcel(id, parcel);
            if (updated == null) {
                return Response.ok(ApiResponse.error("Cannot update parcel. Either not found or not in DRAFT status"))
                             .status(Response.Status.BAD_REQUEST)
                             .build();
            }
            return Response.ok(ApiResponse.success("Parcel updated successfully", toDTO(updated))).build();
        } catch (Exception e) {
            return Response.ok(ApiResponse.error("Failed to update parcel: " + e.getMessage()))
                         .status(Response.Status.BAD_REQUEST)
                         .build();
        }
    }
    
    // 5. Delete Parcel (DELETE)
    @DELETE
    @Path("/{id}")
    public Response deleteParcel(@PathParam("id") Long id) {
        try {
            boolean deleted = parcelService.deleteParcel(id);
            if (deleted) {
                return Response.ok(ApiResponse.success("Parcel deleted successfully", null)).build();
            } else {
                return Response.ok(ApiResponse.error("Cannot delete parcel. Either not found or not in DRAFT status"))
                             .status(Response.Status.BAD_REQUEST)
                             .build();
            }
        } catch (Exception e) {
            return Response.ok(ApiResponse.error("Failed to delete parcel: " + e.getMessage()))
                         .status(Response.Status.INTERNAL_SERVER_ERROR)
                         .build();
        }
    }
    
    // 6. Push Status (Business Process) - POST /parcels/{id}/push
    @POST
    @Path("/{id}/push")
    public Response pushStatus(@PathParam("id") Long id, StatusUpdateRequest request) {
        try {
            Parcel updated = parcelService.pushStatus(id, request.getNewStatus());
            if (updated == null) {
                return Response.ok(ApiResponse.error("Invalid status transition or parcel not found"))
                             .status(Response.Status.BAD_REQUEST)
                             .build();
            }
            return Response.ok(ApiResponse.success("Status updated successfully", toDTO(updated))).build();
        } catch (Exception e) {
            return Response.ok(ApiResponse.error("Failed to update status: " + e.getMessage()))
                         .status(Response.Status.BAD_REQUEST)
                         .build();
        }
    }
}