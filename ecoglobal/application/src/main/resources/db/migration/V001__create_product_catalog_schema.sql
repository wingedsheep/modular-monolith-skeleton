CREATE TABLE products (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    sustainability_rating VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    packaging VARCHAR(255) NOT NULL
);

CREATE TABLE product_certifications (
    product_id UUID NOT NULL,
    certification VARCHAR(255) NOT NULL,
    PRIMARY KEY (product_id, certification),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE product_country_availability (
    product_id UUID NOT NULL,
    country_code VARCHAR(2) NOT NULL,
    PRIMARY KEY (product_id, country_code),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE INDEX idx_product_category ON products(category);
CREATE INDEX idx_product_sustainability_rating ON products(sustainability_rating);
