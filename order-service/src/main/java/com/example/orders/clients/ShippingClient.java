package com.example.orders.clients;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/shipping")
@RegisterRestClient(configKey = "shipping-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ShippingClient {

    @POST
    @Path("/calculate")
    ShippingResponse calculate(ShippingRequest request);

    public static class ShippingRequest {
        public Long productId;
        public String destination;

        public ShippingRequest() {
        }

        public ShippingRequest(Long productId, String destination) {
            this.productId = productId;
            this.destination = destination;
        }
    }

    public static class ShippingResponse {
        public double cost;
        public String destination;
    }
}
