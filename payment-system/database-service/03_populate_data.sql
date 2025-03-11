-- Connect to the payment database
\c "payDemo"

-- Insert sample data into the transdemo table
INSERT INTO transdemo (tx_type, payment, payer, status) VALUES
(
    'SALE',
    '{
        "payment_type": "Credit Card",
        "masked_payment": "************1234",
        "encrypted_payment": "AES256_ENCRYPTED_DATA_PLACEHOLDER_1",
        "cvv": "123",
        "expiration_date": "12/2025"
    }',
    '{
        "firstName": "John",
        "lastName": "Doe",
        "address1": "123 Main St",
        "address2": "Apt 4B",
        "city": "New York",
        "state": "NY",
        "zipcode": "10001",
        "phone": "212-555-1234",
        "email": "john.doe@example.com"
    }',
    'successful'
),
(
    'CREDIT',
    '{
        "payment_type": "ACH",
        "masked_payment": "*********5678",
        "encrypted_payment": "AES256_ENCRYPTED_DATA_PLACEHOLDER_2",
        "routing_number": "021000021"
    }',
    '{
        "firstName": "Jane",
        "lastName": "Smith",
        "address1": "456 Park Ave",
        "address2": "",
        "city": "Boston",
        "state": "MA",
        "zipcode": "02108",
        "phone": "617-555-9876",
        "email": "jane.smith@example.com"
    }',
    'pending'
),
(
    'SALE',
    '{
        "payment_type": "Credit Card",
        "masked_payment": "************5432",
        "encrypted_payment": "AES256_ENCRYPTED_DATA_PLACEHOLDER_3",
        "cvv": "321",
        "expiration_date": "03/2024"
    }',
    '{
        "firstName": "Robert",
        "lastName": "Johnson",
        "address1": "789 Broadway",
        "address2": "Suite 10",
        "city": "Chicago",
        "state": "IL",
        "zipcode": "60601",
        "phone": "312-555-4321",
        "email": "robert.johnson@example.com"
    }',
    'successful'
),
(
    'REFUND',
    '{
        "payment_type": "Credit Card",
        "masked_payment": "************9876",
        "encrypted_payment": "AES256_ENCRYPTED_DATA_PLACEHOLDER_4",
        "cvv": "456",
        "expiration_date": "08/2026"
    }',
    '{
        "firstName": "Emily",
        "lastName": "Davis",
        "address1": "321 Oak St",
        "address2": "",
        "city": "San Francisco",
        "state": "CA",
        "zipcode": "94107",
        "phone": "415-555-8765",
        "email": "emily.davis@example.com"
    }',
    'successful'
),
(
    'VOID',
    '{
        "payment_type": "Credit Card",
        "masked_payment": "************2468",
        "encrypted_payment": "AES256_ENCRYPTED_DATA_PLACEHOLDER_5",
        "cvv": "789",
        "expiration_date": "05/2025"
    }',
    '{
        "firstName": "Michael",
        "lastName": "Wilson",
        "address1": "654 Pine St",
        "address2": "Apt 7C",
        "city": "Miami",
        "state": "FL",
        "zipcode": "33101",
        "phone": "305-555-2468",
        "email": "michael.wilson@example.com"
    }',
    'successful'
),
(
    'SALE',
    '{
        "payment_type": "ACH",
        "masked_payment": "*********1357",
        "encrypted_payment": "AES256_ENCRYPTED_DATA_PLACEHOLDER_6",
        "routing_number": "026009593"
    }',
    '{
        "firstName": "Sarah",
        "lastName": "Brown",
        "address1": "987 Maple Ave",
        "address2": "",
        "city": "Dallas",
        "state": "TX",
        "zipcode": "75201",
        "phone": "214-555-1357",
        "email": "sarah.brown@example.com"
    }',
    'failed'
),
(
    'CREDIT',
    '{
        "payment_type": "Credit Card",
        "masked_payment": "************3690",
        "encrypted_payment": "AES256_ENCRYPTED_DATA_PLACEHOLDER_7",
        "cvv": "159",
        "expiration_date": "11/2024"
    }',
    '{
        "firstName": "David",
        "lastName": "Miller",
        "address1": "753 Cedar St",
        "address2": "Unit 12",
        "city": "Seattle",
        "state": "WA",
        "zipcode": "98101",
        "phone": "206-555-3690",
        "email": "david.miller@example.com"
    }',
    'pending'
),
(
    'SALE',
    '{
        "payment_type": "Credit Card",
        "masked_payment": "************8024",
        "encrypted_payment": "AES256_ENCRYPTED_DATA_PLACEHOLDER_8",
        "cvv": "753",
        "expiration_date": "04/2026"
    }',
    '{
        "firstName": "Jennifer",
        "lastName": "Taylor",
        "address1": "246 Elm St",
        "address2": "",
        "city": "Denver",
        "state": "CO",
        "zipcode": "80202",
        "phone": "303-555-8024",
        "email": "jennifer.taylor@example.com"
    }',
    'successful'
),
(
    'REFUND',
    '{
        "payment_type": "ACH",
        "masked_payment": "*********9513",
        "encrypted_payment": "AES256_ENCRYPTED_DATA_PLACEHOLDER_9",
        "routing_number": "121000358"
    }',
    '{
        "firstName": "Christopher",
        "lastName": "Anderson",
        "address1": "135 Walnut St",
        "address2": "Apt 22B",
        "city": "Atlanta",
        "state": "GA",
        "zipcode": "30303",
        "phone": "404-555-9513",
        "email": "christopher.anderson@example.com"
    }',
    'successful'
),
(
    'SALE',
    '{
        "payment_type": "Credit Card",
        "masked_payment": "************7531",
        "encrypted_payment": "AES256_ENCRYPTED_DATA_PLACEHOLDER_10",
        "cvv": "951",
        "expiration_date": "09/2025"
    }',
    '{
        "firstName": "Amanda",
        "lastName": "Thomas",
        "address1": "864 Spruce St",
        "address2": "",
        "city": "Philadelphia",
        "state": "PA",
        "zipcode": "19103",
        "phone": "215-555-7531",
        "email": "amanda.thomas@example.com"
    }',
    'pending'
);