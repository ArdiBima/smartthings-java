CREATE TABLE vendors (
    id SERIAL PRIMARY KEY,
    brand_name VARCHAR(100) NOT NULL
);

CREATE TABLE devices (
    id SERIAL PRIMARY KEY,
    vendor_id INT REFERENCES vendors(id),
    name VARCHAR(100),
    description TEXT,
    config_json JSONB,
    value INT
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    dob DATE,
    address TEXT,
    country VARCHAR(50)
);

CREATE TABLE user_devices (
    user_id INT REFERENCES users(id),
    device_id INT REFERENCES devices(id),
    PRIMARY KEY (user_id, device_id)
);
