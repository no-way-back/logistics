\c product_db;

CREATE SCHEMA IF NOT EXISTS product_service AUTHORIZATION "user";
SET search_path TO product_service;

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- =========================
-- p_products
-- =========================
CREATE TABLE IF NOT EXISTS p_products (
                                          product_id   UUID PRIMARY KEY,
                                          supplier_id  UUID NOT NULL,
                                          hub_id       UUID NOT NULL,
                                          name         VARCHAR(255) NOT NULL,
    price        INT NOT NULL,

    created_at   TIMESTAMP NOT NULL,
    updated_at   TIMESTAMP NOT NULL,
    deleted_at   TIMESTAMP,
    deleted_by   UUID
    );

-- =========================
-- p_stocks
-- =========================
CREATE TABLE IF NOT EXISTS p_stocks (
                                        stock_id     UUID PRIMARY KEY,
                                        product_id   UUID NOT NULL,
                                        quantity     INT NOT NULL,

                                        created_at   TIMESTAMP NOT NULL,
                                        updated_at   TIMESTAMP NOT NULL,
                                        deleted_at   TIMESTAMP,
                                        deleted_by   UUID,

                                        CONSTRAINT fk_stocks_product
                                        FOREIGN KEY (product_id)
    REFERENCES p_products(product_id)
    );

-- =========================


-- =========================
-- Product 1 : 초코바 24입
-- =========================
INSERT INTO p_products (
    product_id,
    supplier_id,
    hub_id,
    name,
    price,
    created_at,
    updated_at
) VALUES (
             '00000000-0000-0000-0000-000000000000',
             '00000000-0000-0000-0000-000000000001', -- 공급업체 A
             '11111111-1111-1111-1111-111111111111', -- 임의 Hub
             '초코바 24입',
             12900,
             now(),
             now()
         );

INSERT INTO p_stocks (
    stock_id,
    product_id,
    quantity,
    created_at,
    updated_at
) VALUES (
             gen_random_uuid(),
             '00000000-0000-0000-0000-000000000000',
             100, -- 충분한 재고
             now(),
             now()
         );

-- =========================
-- Product 2 : 사과 주스 1L
-- =========================
INSERT INTO p_products (
    product_id,
    supplier_id,
    hub_id,
    name,
    price,
    created_at,
    updated_at
) VALUES (
             '00000000-0000-0000-0000-000000000001',
             '00000000-0000-0000-0000-000000000001', -- 같은 공급업체
             '11111111-1111-1111-1111-111111111111',
             '사과 주스 1L',
             3200,
             now(),
             now()
         );

INSERT INTO p_stocks (
    stock_id,
    product_id,
    quantity,
    created_at,
    updated_at
) VALUES (
             gen_random_uuid(),
             '00000000-0000-0000-0000-000000000001',
             200,
             now(),
             now()
         );
