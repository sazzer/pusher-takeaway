package com.pusher.takeawaybackend

import org.springframework.stereotype.Component

@Component
class MenuItemDao {
    private val menuItems = listOf(
            MenuItem(id = "cheese_tomato_pizza", name = "Cheese & Tomato Pizza"),
            MenuItem(id = "hot_spicy_pizza", name = "Hot & Spicy Pizza"),
            MenuItem(id = "vegetarian_pizza", name = "Vegetarian Supreme Pizza"),
            MenuItem(id = "garlic_bread", name = "Garlic Pizza Bread"),
            MenuItem(id = "donner_kebab", name = "Donner Kebab"),
            MenuItem(id = "chicken_tikka_kebab", name = "Chicken Tikka Kebab"),
            MenuItem(id = "donner_chicken_tikka_kebab", name = "Donner & Chicken Tikka Mixed Kebab"),
            MenuItem(id = "chicken_strips", name = "Chicken Strips (7)"),
            MenuItem(id = "beef_burger", name = "Beef Burger"),
            MenuItem(id = "cheeseburger", name = "Cheeseburger")
    )

    fun listMenuItems() = menuItems
}
