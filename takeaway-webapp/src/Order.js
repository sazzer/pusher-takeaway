import React from 'react';
import { Segment, Table, Button } from 'semantic-ui-react'
import axios from 'axios';

function updateOrderItem(order, item, newStatus) {
    axios.put(`http://localhost:8080/orders/${order.id}/items/${item.id}/status`,
        newStatus,
        {
            transformRequest: (data) => `"${data}"`,
            headers: {
                'Content-Type': 'application/json'
            }
        });
}

function updateOrder(order, newStatus) {
    axios.put(`http://localhost:8080/orders/${order.id}/status`,
        newStatus,
        {
            transformRequest: (data) => `"${data}"`,
            headers: {
                'Content-Type': 'application/json'
            }
        });
}

function OrderItemButton({ order, item }) {
    if (item.status === 'PENDING') {
        return <Button onClick={() => updateOrderItem(order, item, 'STARTED')}>Start Work</Button>;
    } else if (item.status === 'STARTED') {
        return <Button onClick={() => updateOrderItem(order, item, 'FINISHED')}>Finish Work</Button>;
    } else {
        return <div>Finished</div>;
    }
}

function OrderButton({ order }) {
    if (order.status === 'COOKED') {
        return <Button onClick={() => updateOrder(order, 'OUT_FOR_DELIVERY')}>Out for Delivery</Button>;
    } else if (order.status === 'OUT_FOR_DELIVERY') {
        return <Button onClick={() => updateOrder(order, 'DELIVERED')}>Delivered</Button>;
    } else {
        return null;
    }
}

export default function Order({ order }) {
    const items = order.items.map((item) => (
        <Table.Row key={item.id}>
            <Table.Cell>
                {item.name}
            </Table.Cell>
            <Table.Cell>
                <OrderItemButton order={order} item={item} />
            </Table.Cell>
        </Table.Row>
    ));
    return (
        <Segment vertical>
            <Table striped>
                <Table.Body>
                    {items}
                </Table.Body>
            </Table>
            <OrderButton order={order} />
        </Segment>
    );
}
