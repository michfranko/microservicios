package com.example.orders;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "product-api")
@Path("/products")
public interface ProductServiceClient {

    @GET
    @Path("/{id}")
    Product getProduct(@PathParam("id") Long id);

    @POST
    @Path("/{id}/deduct")
    Response deductStock(
            @PathParam("id") Long id,
            @QueryParam("quantity") Integer quantity);
}
