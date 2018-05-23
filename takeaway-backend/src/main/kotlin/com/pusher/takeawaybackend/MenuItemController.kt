package com.pusher.takeawaybackend

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
class MenuItemController(private val dao: MenuItemDao) {
    @RequestMapping("/menu-items")
    fun getMenuItems() = dao.listMenuItems()
}
