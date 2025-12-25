package com.parcel.system.rest;

import com.parcel.system.dto.ApiResponse;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParcelResourceTest {
    
    @Test
    public void testApiResponse() {
        
        ApiResponse success = ApiResponse.success("Test", "Data");
        assertTrue(success.isSuccess());
        assertEquals("Test", success.getMessage());
        assertEquals("Data", success.getData());
        
        ApiResponse error = ApiResponse.error("Error");
        assertFalse(error.isSuccess());
        assertEquals("Error", error.getMessage());
        assertNull(error.getData());
    }
}