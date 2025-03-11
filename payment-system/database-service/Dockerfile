FROM postgres:16.8

# Set environment variables
ENV POSTGRES_USER=payadmin
ENV POSTGRES_PASSWORD=PaySecure123!
ENV POSTGRES_DB=postgres

# Copy SQL files to docker-entrypoint-initdb.d
# Files in this directory are executed in alphabetical order when the container starts
COPY *.sql /docker-entrypoint-initdb.d/

# Expose PostgreSQL port
EXPOSE 5432