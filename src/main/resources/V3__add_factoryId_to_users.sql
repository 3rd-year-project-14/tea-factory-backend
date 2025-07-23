ALTER TABLE users
ADD COLUMN factory_id BIGINT;

ALTER TABLE users
ADD CONSTRAINT fk_users_factory
FOREIGN KEY (factory_id)
REFERENCES factories(factory_id);
