import React from 'react';
import { Container } from 'react-bootstrap';
import PaymentForm from './components/PaymentForm';
import { ToastContainer } from 'react-toastify';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'react-toastify/dist/ReactToastify.css';
import './App.css';

function App() {
  return (
    <div className="App">
      <Container className="py-5">
        <h1 className="text-center mb-4">Payment Terminal</h1>
        <PaymentForm />
        <ToastContainer position="top-right" autoClose={5000} />
      </Container>
    </div>
  );
}

export default App;
