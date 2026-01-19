package com.example.orders;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ShippingRequest(
    @JsonProperty("productId") int productId,
    String destination
) {}
