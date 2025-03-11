import React, { useState } from 'react';
import { Card, Tab, Tabs, Form, Button, Spinner } from 'react-bootstrap';
import { Formik } from 'formik';
import * as Yup from 'yup';
import { toast } from 'react-toastify';
import axios from 'axios';

const PaymentForm = () => {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [apiDebug, setApiDebug] = useState({
    request: null,
    response: null,
    error: null
  });

  const creditCardSchema = Yup.object().shape({
    firstName: Yup.string().required('First name is required'),
    lastName: Yup.string().required('Last name is required'),
    cardNumber: Yup.string()
      .matches(/^[0-9]{16}$/, 'Card number must be 16 digits')
      .required('Card number is required'),
    expirationDate: Yup.string()
      .matches(/^(0[1-9]|1[0-2])\/([0-9]{2})$/, 'Invalid expiration date (MM/YY)')
      .required('Expiration date is required'),
    cvv: Yup.string()
      .matches(/^[0-9]{3,4}$/, 'CVV must be 3 or 4 digits')
      .required('CVV is required'),
    address1: Yup.string().required('Address is required'),
    city: Yup.string().required('City is required'),
    state: Yup.string().required('State is required'),
    zipcode: Yup.string()
      .matches(/^[0-9]{5}$/, 'Invalid ZIP code')
      .required('ZIP code is required'),
    amount: Yup.number()
      .min(0.01, 'Amount must be greater than 0')
      .required('Amount is required')
  });

  const achSchema = Yup.object().shape({
    firstName: Yup.string().required('First name is required'),
    lastName: Yup.string().required('Last name is required'),
    routingNumber: Yup.string()
      .matches(/^[0-9]{9}$/, 'Routing number must be 9 digits')
      .required('Routing number is required'),
    accountNumber: Yup.string()
      .matches(/^[0-9]{4,17}$/, 'Account number must be between 4 and 17 digits')
      .required('Account number is required'),
    address1: Yup.string().required('Address is required'),
    city: Yup.string().required('City is required'),
    state: Yup.string().required('State is required'),
    zipcode: Yup.string()
      .matches(/^[0-9]{5}$/, 'Invalid ZIP code')
      .required('ZIP code is required'),
    amount: Yup.number()
      .min(0.01, 'Amount must be greater than 0')
      .required('Amount is required')
  });

  const handleSubmit = async (values, { resetForm }) => {
    setIsSubmitting(true);
    try {
      const paymentData = {
        txType: 'SALE',
        payment: {
          paymentType: values.routingNumber ? 'ACH' : 'Credit Card',
          accountNumber: values.routingNumber ? values.accountNumber : values.cardNumber,
          routingNumber: values.routingNumber,
          expirationDate: values.expirationDate,
          cvv: values.cvv,
          amount: values.amount
        },
        payer: {
          firstName: values.firstName,
          lastName: values.lastName,
          address1: values.address1,
          city: values.city,
          state: values.state,
          zipcode: values.zipcode
        }
      };

      // Update API debug info - request
      setApiDebug(prev => ({
        ...prev,
        request: {
          url: 'http://localhost:8082/payproc/api/v1/payments/process',
          method: 'POST',
          headers: {
            'Authorization': 'Basic ' + btoa('payproc:ProcPass123!'),
            'Content-Type': 'application/json'
          },
          data: paymentData
        }
      }));

      const response = await axios.post('http://localhost:8082/payproc/api/v1/payments/process', paymentData, {
        auth: {
          username: 'payproc',
          password: 'ProcPass123!'
        }
      });

      // Update API debug info - response
      setApiDebug(prev => ({
        ...prev,
        response: {
          status: response.status,
          statusText: response.statusText,
          data: response.data
        },
        error: null
      }));

      if (response.data.status === 'successful') {
        toast.success('Payment processed successfully!');
        resetForm();
      } else {
        toast.error('Payment failed: ' + response.data.message);
      }
    } catch (error) {
      // Update API debug info - error
      setApiDebug(prev => ({
        ...prev,
        error: {
          message: error.message,
          response: error.response ? {
            status: error.response.status,
            statusText: error.response.statusText,
            data: error.response.data
          } : null
        }
      }));
      toast.error('Error processing payment: ' + (error.response?.data?.message || error.message));
    } finally {
      setIsSubmitting(false);
    }
  };

  const renderDebugInfo = () => (
    <div className="mt-4 border-top pt-4">
      <h5>API Debug Information</h5>
      <div className="row">
        <div className="col-md-6">
          <h6>Request</h6>
          <pre className="bg-light p-3 rounded" style={{ maxHeight: '300px', overflow: 'auto' }}>
            {apiDebug.request ? JSON.stringify(apiDebug.request, null, 2) : 'No request made yet'}
          </pre>
        </div>
        <div className="col-md-6">
          <h6>Response</h6>
          <pre className="bg-light p-3 rounded" style={{ maxHeight: '300px', overflow: 'auto' }}>
            {apiDebug.response ? JSON.stringify(apiDebug.response, null, 2) : 
             apiDebug.error ? JSON.stringify(apiDebug.error, null, 2) : 'No response yet'}
          </pre>
        </div>
      </div>
    </div>
  );

  const renderForm = ({ handleSubmit, handleChange, values, touched, errors, isValid }) => (
    <Form noValidate onSubmit={handleSubmit}>
      <Form.Group className="mb-3">
        <Form.Label>First Name</Form.Label>
        <Form.Control
          type="text"
          name="firstName"
          value={values.firstName}
          onChange={handleChange}
          isInvalid={touched.firstName && errors.firstName}
        />
        <Form.Control.Feedback type="invalid">{errors.firstName}</Form.Control.Feedback>
      </Form.Group>

      <Form.Group className="mb-3">
        <Form.Label>Last Name</Form.Label>
        <Form.Control
          type="text"
          name="lastName"
          value={values.lastName}
          onChange={handleChange}
          isInvalid={touched.lastName && errors.lastName}
        />
        <Form.Control.Feedback type="invalid">{errors.lastName}</Form.Control.Feedback>
      </Form.Group>

      {values.routingNumber !== undefined ? (
        <>
          <Form.Group className="mb-3">
            <Form.Label>Routing Number</Form.Label>
            <Form.Control
              type="text"
              name="routingNumber"
              value={values.routingNumber}
              onChange={handleChange}
              isInvalid={touched.routingNumber && errors.routingNumber}
            />
            <Form.Control.Feedback type="invalid">{errors.routingNumber}</Form.Control.Feedback>
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Account Number</Form.Label>
            <Form.Control
              type="text"
              name="accountNumber"
              value={values.accountNumber}
              onChange={handleChange}
              isInvalid={touched.accountNumber && errors.accountNumber}
            />
            <Form.Control.Feedback type="invalid">{errors.accountNumber}</Form.Control.Feedback>
          </Form.Group>
        </>
      ) : (
        <>
          <Form.Group className="mb-3">
            <Form.Label>Card Number</Form.Label>
            <Form.Control
              type="text"
              name="cardNumber"
              value={values.cardNumber}
              onChange={handleChange}
              isInvalid={touched.cardNumber && errors.cardNumber}
            />
            <Form.Control.Feedback type="invalid">{errors.cardNumber}</Form.Control.Feedback>
          </Form.Group>

          <div className="row">
            <div className="col">
              <Form.Group className="mb-3">
                <Form.Label>Expiration Date (MM/YY)</Form.Label>
                <Form.Control
                  type="text"
                  name="expirationDate"
                  value={values.expirationDate}
                  onChange={handleChange}
                  isInvalid={touched.expirationDate && errors.expirationDate}
                />
                <Form.Control.Feedback type="invalid">{errors.expirationDate}</Form.Control.Feedback>
              </Form.Group>
            </div>
            <div className="col">
              <Form.Group className="mb-3">
                <Form.Label>CVV</Form.Label>
                <Form.Control
                  type="text"
                  name="cvv"
                  value={values.cvv}
                  onChange={handleChange}
                  isInvalid={touched.cvv && errors.cvv}
                />
                <Form.Control.Feedback type="invalid">{errors.cvv}</Form.Control.Feedback>
              </Form.Group>
            </div>
          </div>
        </>
      )}

      <Form.Group className="mb-3">
        <Form.Label>Address</Form.Label>
        <Form.Control
          type="text"
          name="address1"
          value={values.address1}
          onChange={handleChange}
          isInvalid={touched.address1 && errors.address1}
        />
        <Form.Control.Feedback type="invalid">{errors.address1}</Form.Control.Feedback>
      </Form.Group>

      <div className="row">
        <div className="col">
          <Form.Group className="mb-3">
            <Form.Label>City</Form.Label>
            <Form.Control
              type="text"
              name="city"
              value={values.city}
              onChange={handleChange}
              isInvalid={touched.city && errors.city}
            />
            <Form.Control.Feedback type="invalid">{errors.city}</Form.Control.Feedback>
          </Form.Group>
        </div>
        <div className="col">
          <Form.Group className="mb-3">
            <Form.Label>State</Form.Label>
            <Form.Control
              type="text"
              name="state"
              value={values.state}
              onChange={handleChange}
              isInvalid={touched.state && errors.state}
            />
            <Form.Control.Feedback type="invalid">{errors.state}</Form.Control.Feedback>
          </Form.Group>
        </div>
        <div className="col">
          <Form.Group className="mb-3">
            <Form.Label>ZIP Code</Form.Label>
            <Form.Control
              type="text"
              name="zipcode"
              value={values.zipcode}
              onChange={handleChange}
              isInvalid={touched.zipcode && errors.zipcode}
            />
            <Form.Control.Feedback type="invalid">{errors.zipcode}</Form.Control.Feedback>
          </Form.Group>
        </div>
      </div>

      <Form.Group className="mb-3">
        <Form.Label>Amount ($)</Form.Label>
        <Form.Control
          type="number"
          step="0.01"
          name="amount"
          value={values.amount}
          onChange={handleChange}
          isInvalid={touched.amount && errors.amount}
        />
        <Form.Control.Feedback type="invalid">{errors.amount}</Form.Control.Feedback>
      </Form.Group>

      <Button type="submit" disabled={!isValid || isSubmitting}>
        {isSubmitting ? (
          <>
            <Spinner animation="border" size="sm" className="me-2" />
            Processing...
          </>
        ) : (
          'Submit Payment'
        )}
      </Button>
    </Form>
  );

  return (
    <Card>
      <Card.Body>
        <Tabs defaultActiveKey="credit-card" className="mb-4">
          <Tab eventKey="credit-card" title="Credit Card">
            <Formik
              initialValues={{
                firstName: '',
                lastName: '',
                cardNumber: '',
                expirationDate: '',
                cvv: '',
                address1: '',
                city: '',
                state: '',
                zipcode: '',
                amount: ''
              }}
              validationSchema={creditCardSchema}
              onSubmit={handleSubmit}
            >
              {renderForm}
            </Formik>
          </Tab>
          <Tab eventKey="ach" title="ACH">
            <Formik
              initialValues={{
                firstName: '',
                lastName: '',
                routingNumber: '',
                accountNumber: '',
                address1: '',
                city: '',
                state: '',
                zipcode: '',
                amount: ''
              }}
              validationSchema={achSchema}
              onSubmit={handleSubmit}
            >
              {renderForm}
            </Formik>
          </Tab>
        </Tabs>
        {renderDebugInfo()}
      </Card.Body>
    </Card>
  );
};

export default PaymentForm;