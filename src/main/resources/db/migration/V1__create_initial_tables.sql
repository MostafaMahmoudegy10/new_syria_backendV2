-- ==========================
-- Table: country
-- ==========================
CREATE TABLE country (
    id BIGSERIAL PRIMARY KEY,
    country_code CHAR(3) NOT NULL UNIQUE,
    country_name VARCHAR(255) NOT NULL UNIQUE
);

-- ==========================
-- Table: image
-- ==========================
CREATE TABLE image (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    file_name VARCHAR(255),
    file_type VARCHAR(50),
    image_data bigint
);

-- ==========================
-- Table: users
-- ==========================
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_name VARCHAR(50) NOT NULL ,

    password VARCHAR(255) NOT NULL,

    email VARCHAR(255) NOT NULL UNIQUE,

    role VARCHAR(20) NOT NULL DEFAULT 'USER',

    enabled BOOLEAN NOT NULL DEFAULT FALSE,

    is_completed_profile BOOLEAN NOT NULL DEFAULT FALSE,

    cover_image_id UUID NULL,
    country_name VARCHAR(255) NULL,
    phone_number VARCHAR(15) UNIQUE,

    CONSTRAINT fk_cover_image FOREIGN KEY (cover_image_id) REFERENCES image(id),
    CONSTRAINT fk_country FOREIGN KEY (country_name) REFERENCES country(country_name)
);
