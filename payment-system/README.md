# Payment Processing System

This repository contains a comprehensive payment processing system consisting of microservices with a PostgreSQL database.

## Database Microservice

### Database Credentials
- **Username**: payadmin
- **Password**: PaySecure123!
- **Database Name**: payDemo
- **Port**: 5432

### SQL Files
1. `01_create_database.sql`: Creates the payDemo database and defines enum types for transaction types and statuses
2. `02_create_tables.sql`: Creates the transdemo table with the required schema
3. `03_populate_data.sql`: Populates the table with 10 sample transactions

### Database Schema

#### Enum Types
- **transaction_type**: SALE, CREDIT, REFUND, VOID
- **transaction_status**: pending, successful, failed

#### Table: transdemo
- **id**: bigserial (starts from 100000000001)
- **tx_date**: timestamp with timezone (default: current US Eastern Time)
- **tx_type**: transaction_type enum
- **payment**: JSONB containing payment information
  - payment_type
  - masked_payment
  - encrypted_payment
  - cvv (for credit cards)
  - expiration_date (for credit cards)
  - routing_number (for ACH)
- **payer**: JSONB containing payer information
  - firstName
  - lastName
  - address1
  - address2
  - city
  - state
  - zipcode
  - phone
  - email
- **status**: transaction_status enum

## Running the Database

### Using Docker Compose
```bash
cd payment-system
docker-compose up -d
```

### Connecting to the Database
```bash
docker exec -it payment-db psql -U payadmin -d payDemo
```

### Sample Queries
```sql
-- Get all transactions
SELECT * FROM transdemo;

-- Get successful transactions
SELECT * FROM transdemo WHERE status = 'successful';

-- Get transactions by type
SELECT * FROM transdemo WHERE tx_type = 'SALE';
```