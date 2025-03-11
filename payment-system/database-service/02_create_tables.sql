-- Connect to the payment database
\c "payDemo"

-- Create the transaction table
CREATE TABLE transdemo (
    id BIGSERIAL PRIMARY KEY,
    tx_date TIMESTAMP WITH TIME ZONE DEFAULT (NOW() AT TIME ZONE 'America/New_York'),
    tx_type transaction_type NOT NULL,
    payment JSONB NOT NULL,
    payer JSONB NOT NULL,
    status transaction_status DEFAULT 'pending'
);

-- Set the starting value for the id sequence to 100000000001
ALTER SEQUENCE transdemo_id_seq RESTART WITH 100000000001;