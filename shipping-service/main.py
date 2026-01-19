from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import logging

logging.basicConfig(level=logging.INFO)

app = FastAPI(title="Shipping Calculator Service")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class ShippingRequest(BaseModel):
    productId: int
    destination: str

@app.get("/health")
async def health_check():
    return {"status": "healthy"}

@app.post("/shipping/calculate")
async def calculate_shipping(data: ShippingRequest):
    logging.info(f"Parsed request: productId={data.productId}, destination={data.destination}")

    # All destinations cost 12
    cost = 12.0
    return {"cost": cost}
