package com.example.orders.clients;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/products")
@RegisterRestClient(configKey = "product-api")
@Produces(MediaType.APPLICATION_JSON)
public interface ProductClient {

    @POST
    @Path("/{id}/deduct")
    Response deductStock(@PathParam("id") Long id, @QueryParam("quantity") int quantity);

    @GET
    @Path("/{id}")
    ProductDTO getProduct(@PathParam("id") Long id);

    public static class ProductDTO {
        public Long id;
        public String name;
        public String description;
        public double price;
        public int stock;
    }
}
