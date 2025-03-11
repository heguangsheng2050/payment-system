#!/bin/bash

echo "Testing connection to PostgreSQL database..."
PGPASSWORD=PaySecure123! psql -h localhost -U payadmin -d payDemo -c "SELECT id, tx_type, status FROM transdemo LIMIT 5;"

if [ $? -eq 0 ]; then
    echo "Connection successful!"
else
    echo "Connection failed. Make sure the database container is running."
fi