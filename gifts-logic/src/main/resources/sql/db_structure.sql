CREATE SCHEMA gifts;

CREATE TABLE tags
(
    id   bigint NOT NULL AUTO_INCREMENT,
    name varchar(50),
    PRIMARY KEY (id)
);

CREATE TABLE gift_certificates
(
    id               bigint         NOT NULL AUTO_INCREMENT,
    name             varchar(100)   NOT NULL UNIQUE,
    description      varchar(500)   NOT NULL,
    price            decimal(10, 2) NOT NULL,
    duration         smallint       NOT NULL,
    create_date      datetime       NOT NULL,
    last_update_date datetime       NOT NULL,
    is_deleted       tinyint        NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE gift_tags
(
    certificate_id bigint NOT NULL,
    tag_id         bigint NOT NULL,
    CONSTRAINT gift_tags_gift_certificate FOREIGN KEY (certificate_id) REFERENCES gift_certificates (id),
    CONSTRAINT gift_tags_tag FOREIGN KEY (tag_id) REFERENCES tags (id)
);

CREATE TABLE orders (
  id bigint NOT NULL AUTO_INCREMENT,
  certificate_id bigint NOT NULL,
  price decimal(10,2) NOT NULL,
  purchase_time datetime NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT orders_certificates FOREIGN KEY (certificate_id) REFERENCES gift_certificates (id)
);

CREATE TABLE users (
  id bigint NOT NULL AUTO_INCREMENT,
  login varchar(100) NOT NULL,
  balance decimal(10,2) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE user_orders (
  user_id bigint NOT NULL,
  order_id bigint NOT NULL,
  CONSTRAINT user_orders_users FOREIGN KEY (user_id) REFERENCES users (id),
  CONSTRAINT user_orders_orders FOREIGN KEY (order_id) REFERENCES orders (id)
);