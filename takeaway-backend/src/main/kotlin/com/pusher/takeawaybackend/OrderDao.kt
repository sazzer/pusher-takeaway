package com.pusher.takeawaybackend

import org.springframework.stereotype.Component
import java.util.*

@Component
class OrderDao {
    private val orders = mutableListOf<Order>()

    fun createNewOrder(items: List<String>) : Order {
        val orderId = UUID.randomUUID().toString()
        val orderItems = items.map { menuItem ->
            val orderItemId = UUID.randomUUID().toString()
            OrderItem(id = orderItemId, menuItem = menuItem, status = OrderItemStatus.PENDING)
        }

        val order = Order(id = orderId, items = orderItems, status = OrderStatus.PENDING)
        orders.add(order)

        return order
    }

    fun removeOrder(orderId: String) {
        orders.removeIf { order -> order.id == orderId }
    }

    fun listOrders(): List<Order> = orders

    fun getOrderById(id: String) = orders.first { order -> order.id == id }
}
