import React, { Component } from 'react';
import 'semantic-ui-css/semantic.min.css';
import OrdersList from './ConnectedOrdersList';

class App extends Component {
  render() {
    return (
      <div className="App">
        <OrdersList />
      </div>
    );
  }
}

export default App;
