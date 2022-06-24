package de.oncoding.webshop.model

data class ShoppingCartResponse(
    val customerId: String,
    val orderPositions: List<OrderPositionResponse>,
    val totalAmountÍnCent: Long,
    val deliveryCostInCent: Long,
    val deliveryOption: String
) {

}
