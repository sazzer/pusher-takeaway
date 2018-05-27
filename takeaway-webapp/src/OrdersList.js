import React from 'react';
import Order from './Order';

export default ({ orders }) => {
    const orderElements = orders.map((order) => <Order order={order} key={order.id} />);

    return (
        <div>
            {orderElements}
        </div>
    );
};
