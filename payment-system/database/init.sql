-- Create custom enum types
CREATE TYPE transaction_type AS ENUM ('SALE', 'CREDIT', 'REFUND', 'VOID');
CREATE TYPE transaction_status AS ENUM ('pending', 'successful', 'failed');

-- Create extension for encryption functions
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Set timezone to US Eastern
SET timezone = 'America/New_York';

-- Create the transactions table
CREATE TABLE transdemo (
    id BIGSERIAL PRIMARY KEY CHECK (id >= 100000000001),
    tx_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    tx_type transaction_type NOT NULL,
    payment JSONB NOT NULL,
    payer JSONB NOT NULL,
    status transaction_status NOT NULL DEFAULT 'pending',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create an index on tx_date for better query performance
CREATE INDEX idx_transdemo_tx_date ON transdemo(tx_date);

-- Create an index on status for filtering
CREATE INDEX idx_transdemo_status ON transdemo(status);

-- Create a function to update the updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create a trigger to automatically update the updated_at column
CREATE TRIGGER update_transdemo_updated_at
    BEFORE UPDATE ON transdemo
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();