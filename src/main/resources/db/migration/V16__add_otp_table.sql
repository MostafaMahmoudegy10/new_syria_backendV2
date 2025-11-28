CREATE TABLE otp (
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     code VARCHAR(10) NOT NULL,
     email VARCHAR(255) NOT NULL,
     expires_at TIMESTAMP NOT NULL,
     is_used BOOLEAN NOT NULL DEFAULT FALSE
);