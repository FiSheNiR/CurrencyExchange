CREATE SCHEMA IF NOT EXISTS currency_storage;

CREATE TABLE IF NOT EXISTS currency_storage.currencies(
                                                          id SERIAL PRIMARY KEY,
                                                          code VARCHAR(3) UNIQUE NOT NULL,
                                                          full_name VARCHAR (128),
                                                          sign VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS currency_storage.exchange_rates(
                                                              id SERIAL PRIMARY KEY,
                                                              base_currency_id INT REFERENCES currency_storage.currencies(id) ON DELETE CASCADE,
                                                              target_currency_id INT REFERENCES currency_storage.currencies(id) ON DELETE CASCADE,
                                                              rate DECIMAL,
                                                              UNIQUE (base_currency_id, target_currency_id),
                                                              CHECK (base_currency_id <> target_currency_id)
);

INSERT INTO currency_storage.currencies(code, full_name, sign)
VALUES ('USD', 'United States Dollar', '$'),
       ('EUR', 'Euro', '€'),
       ('AUD', 'Australian dollar', 'A$');

INSERT INTO currency_storage.exchange_rates(base_currency_id, target_currency_id, rate)
VALUES (1,2,0.85),
       (1,3,10)