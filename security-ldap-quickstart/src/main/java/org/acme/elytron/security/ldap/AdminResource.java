package org.acme.elytron.security.ldap;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api/admin")
public class AdminResource {

    @GET
    @RolesAllowed("adminRole")
    @Produces(MediaType.TEXT_PLAIN)
    public String adminResource() {
         return "admin";
    }
}