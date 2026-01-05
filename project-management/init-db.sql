-- Database initialization script
-- This script runs automatically when PostgreSQL container starts for the first time

-- Create database (already created by POSTGRES_DB env var, but keeping for reference)
-- CREATE DATABASE project_management_db;

-- Connect to the database
\c project_management_db;

-- Create extensions if needed
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Optional: Create initial admin user (password: admin123 - BCrypt encoded)
-- Note: This will be created by the application on first run if using a data loader
-- Uncomment if you want to pre-create an admin user

-- INSERT INTO users (id, username, email, password, role, active, created_at)
-- VALUES (
--     1,
--     'admin',
--     'admin@projectmanagement.com',
--     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- password: admin123
--     'ADMIN',
--     true,
--     NOW()
-- );

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE project_management_db TO postgres;

-- Success message
\echo 'Database initialized successfully!'
