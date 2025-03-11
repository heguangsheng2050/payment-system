FROM postgres:16.8

# Set environment variables
ENV POSTGRES_DB=payDemo
ENV POSTGRES_USER=payadmin
ENV POSTGRES_PASSWORD=PayStr0ng!2024

# Copy initialization scripts
COPY ./init.sql /docker-entrypoint-initdb.d/01-init.sql
COPY ./seed.sql /docker-entrypoint-initdb.d/02-seed.sql

# Install additional tools
RUN apt-get update && apt-get install -y \
    postgresql-client \
    && rm -rf /var/lib/apt/lists/*

# Expose PostgreSQL port
EXPOSE 5432