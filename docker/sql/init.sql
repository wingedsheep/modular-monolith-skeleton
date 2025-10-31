-- In a real application, each bounded context would have its own database or schema.
-- For simplicity, we'll create schemas within a single database.

CREATE SCHEMA IF NOT EXISTS "product-catalog";
CREATE SCHEMA IF NOT EXISTS "inventory";
CREATE SCHEMA IF NOT EXISTS "order-fulfillment";
CREATE SCHEMA IF NOT EXISTS "logistics";
CREATE SCHEMA IF NOT EXISTS "carbon-accounting";
CREATE SCHEMA IF NOT EXISTS "tax-compliance";
CREATE SCHEMA IF NOT EXISTS "payment";
