-- Function to generate masked credit card number
CREATE OR REPLACE FUNCTION mask_credit_card(card_number TEXT)
RETURNS TEXT AS $$
BEGIN
    RETURN REPEAT('*', LENGTH(card_number) - 4) || RIGHT(card_number, 4);
END;
$$ LANGUAGE plpgsql;

-- Insert sample data
INSERT INTO transdemo (tx_type, payment, payer, status)
VALUES
    ('SALE', 
    '{
        "payment_type": "Credit Card",
        "masked_payment": "************1234",
        "encrypted_payment": "' || encode(pgp_sym_encrypt('4532123412341234', 'AES256Key'), 'base64') || '",
        "cvv": "123",
        "expiration_date": "12/25"
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
        "email": "john.doe@email.com"
    }',
    'successful'),

    ('CREDIT',
    '{
        "payment_type": "ACH",
        "masked_payment": "*********9876",
        "encrypted_payment": "' || encode(pgp_sym_encrypt('021000021123456789', 'AES256Key'), 'base64') || '",
        "routing_number": "021000021"
    }',
    '{
        "firstName": "Jane",
        "lastName": "Smith",
        "address1": "456 Park Ave",
        "city": "Boston",
        "state": "MA",
        "zipcode": "02108",
        "phone": "617-555-5678",
        "email": "jane.smith@email.com"
    }',
    'successful'),

    ('SALE',
    '{
        "payment_type": "Credit Card",
        "masked_payment": "************5678",
        "encrypted_payment": "' || encode(pgp_sym_encrypt('4532567856785678', 'AES256Key'), 'base64') || '",
        "cvv": "456",
        "expiration_date": "03/26"
    }',
    '{
        "firstName": "Robert",
        "lastName": "Johnson",
        "address1": "789 Oak Rd",
        "city": "Chicago",
        "state": "IL",
        "zipcode": "60601",
        "phone": "312-555-9012",
        "email": "robert.j@email.com"
    }',
    'pending'),

    ('REFUND',
    '{
        "payment_type": "Credit Card",
        "masked_payment": "************9012",
        "encrypted_payment": "' || encode(pgp_sym_encrypt('4532901290129012', 'AES256Key'), 'base64') || '",
        "cvv": "789",
        "expiration_date": "09/25"
    }',
    '{
        "firstName": "Mary",
        "lastName": "Williams",
        "address1": "321 Pine St",
        "city": "San Francisco",
        "state": "CA",
        "zipcode": "94101",
        "phone": "415-555-3456",
        "email": "mary.w@email.com"
    }',
    'successful'),

    ('VOID',
    '{
        "payment_type": "ACH",
        "masked_payment": "*********5432",
        "encrypted_payment": "' || encode(pgp_sym_encrypt('031000021543219876', 'AES256Key'), 'base64') || '",
        "routing_number": "031000021"
    }',
    '{
        "firstName": "Michael",
        "lastName": "Brown",
        "address1": "654 Elm St",
        "city": "Miami",
        "state": "FL",
        "zipcode": "33101",
        "phone": "305-555-7890",
        "email": "michael.b@email.com"
    }',
    'failed'),

    ('SALE',
    '{
        "payment_type": "Credit Card",
        "masked_payment": "************3456",
        "encrypted_payment": "' || encode(pgp_sym_encrypt('4532345634563456', 'AES256Key'), 'base64') || '",
        "cvv": "321",
        "expiration_date": "06/26"
    }',
    '{
        "firstName": "Sarah",
        "lastName": "Davis",
        "address1": "987 Maple Ave",
        "city": "Seattle",
        "state": "WA",
        "zipcode": "98101",
        "phone": "206-555-2345",
        "email": "sarah.d@email.com"
    }',
    'successful'),

    ('CREDIT',
    '{
        "payment_type": "ACH",
        "masked_payment": "*********1111",
        "encrypted_payment": "' || encode(pgp_sym_encrypt('021000021111111111', 'AES256Key'), 'base64') || '",
        "routing_number": "021000021"
    }',
    '{
        "firstName": "David",
        "lastName": "Miller",
        "address1": "741 Cedar Ln",
        "city": "Denver",
        "state": "CO",
        "zipcode": "80201",
        "phone": "303-555-6789",
        "email": "david.m@email.com"
    }',
    'pending'),

    ('SALE',
    '{
        "payment_type": "Credit Card",
        "masked_payment": "************7890",
        "encrypted_payment": "' || encode(pgp_sym_encrypt('4532789078907890', 'AES256Key'), 'base64') || '",
        "cvv": "654",
        "expiration_date": "11/25"
    }',
    '{
        "firstName": "Lisa",
        "lastName": "Anderson",
        "address1": "852 Birch St",
        "city": "Austin",
        "state": "TX",
        "zipcode": "78701",
        "phone": "512-555-0123",
        "email": "lisa.a@email.com"
    }',
    'successful'),

    ('REFUND',
    '{
        "payment_type": "Credit Card",
        "masked_payment": "************2468",
        "encrypted_payment": "' || encode(pgp_sym_encrypt('4532246824682468', 'AES256Key'), 'base64') || '",
        "cvv": "987",
        "expiration_date": "04/26"
    }',
    '{
        "firstName": "James",
        "lastName": "Wilson",
        "address1": "963 Spruce Rd",
        "city": "Portland",
        "state": "OR",
        "zipcode": "97201",
        "phone": "503-555-4567",
        "email": "james.w@email.com"
    }',
    'successful'),

    ('VOID',
    '{
        "payment_type": "ACH",
        "masked_payment": "*********3333",
        "encrypted_payment": "' || encode(pgp_sym_encrypt('021000021333333333', 'AES256Key'), 'base64') || '",
        "routing_number": "021000021"
    }',
    '{
        "firstName": "Emily",
        "lastName": "Taylor",
        "address1": "159 Walnut Ave",
        "city": "Las Vegas",
        "state": "NV",
        "zipcode": "89101",
        "phone": "702-555-8901",
        "email": "emily.t@email.com"
    }',
    'failed');