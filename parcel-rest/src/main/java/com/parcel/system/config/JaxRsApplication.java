package com.parcel.system.config;

import com.parcel.system.rest.ParcelResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class JaxRsApplication extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        
        // Register REST resources
        classes.add(ParcelResource.class);
        
        // Register JSON providers (if needed)
        // classes.add(JacksonJsonProvider.class);
        
        return classes;
    }
}