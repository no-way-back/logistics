INSERT INTO product_service.p_products (product_id, supplier_id, hub_id, name, price, created_at, updated_at)
VALUES ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000001', '11111111-1111-1111-1111-111111111111', '초코바 24입', 12900, now(), now())
    ON CONFLICT (product_id) DO NOTHING;

INSERT INTO product_service.p_stocks (stock_id, product_id, quantity, created_at, updated_at)
VALUES ('00000000-0000-0000-0000-000000000100', '00000000-0000-0000-0000-000000000000', 100, now(), now());

INSERT INTO product_service.p_products (product_id, supplier_id, hub_id, name, price, created_at, updated_at)
VALUES ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', '11111111-1111-1111-1111-111111111111', '사과 주스 1L', 3200, now(), now())
    ON CONFLICT (product_id) DO NOTHING;

INSERT INTO product_service.p_stocks (stock_id, product_id, quantity, created_at, updated_at)
VALUES ('00000000-0000-0000-0000-000000000101', '00000000-0000-0000-0000-000000000001', 200, now(), now());
