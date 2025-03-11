-- Create the payment processing database
CREATE DATABASE "payDemo";

-- Connect to the newly created database
\c "payDemo"

-- Create enum types for transaction types
CREATE TYPE transaction_type AS ENUM ('SALE', 'CREDIT', 'REFUND', 'VOID');

-- Create enum types for transaction status
CREATE TYPE transaction_status AS ENUM ('pending', 'successful', 'failed');

-- Set timezone to US Eastern Time
SET timezone = 'America/New_York';