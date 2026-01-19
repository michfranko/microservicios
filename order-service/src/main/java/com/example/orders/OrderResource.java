package com.example.orders;

import com.example.orders.clients.ProductClient;
import com.example.orders.clients.ShippingClient;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import java.util.List;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    @RestClient
    ProductClient productClient;

    @Inject
    @RestClient
    ShippingClient shippingClient;

    @GET
    public List<Order> getAllOrders() {
        return Order.listAll();
    }

    @POST
    @Transactional
    public Response createOrder(OrderRequest request) {
        System.out.println("[DEBUG] Processing order (Injection Mode). Product: " + request.productId);

        // 1. Get Product Details
        ProductClient.ProductDTO product;
        try {
            product = productClient.getProduct(request.productId);
            if (product == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Producto " + request.productId + " no encontrado\"}")
                        .build();
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to call product_service: " + e.getMessage());
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity("{\"error\": \"Error contactando productos: " + e.getMessage() + "\"}")
                    .build();
        }

        // 2. Deduct Stock
        try {
            Response deductionResponse = productClient.deductStock(request.productId, request.quantity);
            if (deductionResponse.getStatus() != 200) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"error\": \"Stock insuficiente\"}")
                        .build();
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to deduct stock: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error en stock: " + e.getMessage() + "\"}")
                    .build();
        }

        // 3. Calculate Shipping
        double shippingCost = 0.0;
        try {
            ShippingClient.ShippingResponse shippingResponse = shippingClient.calculate(
                    new ShippingClient.ShippingRequest(request.productId, request.destination));
            shippingCost = shippingResponse.cost;
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to calculate shipping: " + e.getMessage());
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity("{\"error\": \"Error en envio: " + e.getMessage() + "\"}")
                    .build();
        }

        // 4. Calculate Total
        double total = (product.price * request.quantity) + shippingCost;

        // 5. Save Order
        Order order = new Order(request.productId, request.destination, request.quantity, shippingCost, total);
        order.persist();

        return Response.status(Response.Status.CREATED).entity(order).build();
    }

    public static class OrderRequest {
        public Long productId;
        public String destination;
        public int quantity;
    }
}
