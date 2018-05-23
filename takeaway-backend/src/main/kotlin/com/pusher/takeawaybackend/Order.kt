package com.pusher.takeawaybackend

enum class OrderItemStatus {
    PENDING,
    STARTED,
    FINISHED
}

enum class OrderStatus {
    PENDING,
    STARTED,
    COOKED,
    OUT_FOR_DELIVERY,
    DELIVERED
}

data class OrderItem(
        val id: String,
        val menuItem: String,
        var status: OrderItemStatus
)

data class Order(
        val id: String,
        var status: OrderStatus,
        val items: List<OrderItem>
)
