package de.oncoding.webshop.service

import de.oncoding.webshop.exceptions.IdNotFoundException
import de.oncoding.webshop.model.OrderPositionResponse
import de.oncoding.webshop.model.OrderResponse
import de.oncoding.webshop.model.ShoppingCartResponse
import de.oncoding.webshop.repository.OrderPositionRepository
import de.oncoding.webshop.repository.OrderRepository
import de.oncoding.webshop.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ShoppingCartService(
    val orderRepository: OrderRepository,
    val productRepository: ProductRepository,
    val orderPositionRepository : OrderPositionRepository
    ) {

    fun getShoppingCartForCustomers(customerId: String): ShoppingCartResponse {

        val orders: List <OrderResponse> = orderRepository.findAllByCustomerIdWhereOrderStatusIsNew(customerId)
        val orderIds = orders.map {it.id}

        val orderPositions = orderPositionRepository.findAllByOrderIds(orderIds)
        val deliveryCost = 800L
        val totalAmount = calculateSumForCart(orderPositions, deliveryCost)

        return ShoppingCartResponse(
            customerId = customerId,
            orderPositions = orderPositions,
            deliveryOption = "STANDARD",
            deliveryCostInCent = deliveryCost,
            totalAmountÍnCent = totalAmount
        )

    }

    private fun calculateSumForCart(
        orderPositions: List<OrderPositionResponse>,
        deliveryCost: Long): Long {
        val positionAmount = orderPositions.map {
            val product = productRepository
                .findById(it.productId)
                .orElseThrow { IdNotFoundException("Product with Id ${it.productId} not found") }
            it.quantity * product.priceInCent
        }
        val positionSum = positionAmount.sumBy { it.toInt() }
        val totalAmount = positionSum + deliveryCost
        return totalAmount
    }

}
