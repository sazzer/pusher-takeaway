import React from 'react';
import axios from 'axios';
import Pusher from 'pusher-js';
import OrdersList from './OrdersList';

const socket = new Pusher('<CHANNELS_KEY>', {
    cluster: '<CHANNELS_CLUSTER>',
});

export default class ConnectedOrdersList extends React.Component {
    state = {
        orders: []
    };

    render() {
        return (
            <div className="ui container">
                <OrdersList orders={this.state.orders} />
            </div>
        );
    }

    componentDidMount() {
        this._fetchOrders();
        socket.subscribe('orders')
            .bind('order-update', () => this._fetchOrders());
    }

    _fetchOrders() {
        const ordersPromise = axios.get('http://localhost:8080/orders')
        const menuItemsPromise = axios.get('http://localhost:8080/menu-items');

        Promise.all([ordersPromise, menuItemsPromise])
            .then((values) => {
                const menuItems = {};

                values[1].data.forEach((entry) => {
                    menuItems[entry.id] = entry.name;
                });

                const orders = values[0].data.map((order) => {
                    return {
                        id: order.id,
                        status: order.status,
                        items: order.items.map((item) => {
                            return {
                                id: item.id,
                                menuItem: item.menuItem,
                                status: item.status,
                                name: menuItems[item.menuItem]
                            };
                        })
                    };
                });

                this.setState({
                    orders: orders
                });
            });
    }
}
