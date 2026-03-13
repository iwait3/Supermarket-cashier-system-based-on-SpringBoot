CREATE DATABASE IF NOT EXISTS supermarket;
USE supermarket;
-- 创建管理员表
CREATE TABLE IF NOT EXISTS admin (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建商品表
CREATE TABLE IF NOT EXISTS product (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  price DOUBLE NOT NULL,
  stock INT NOT NULL,
  description TEXT NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建购物车表
CREATE TABLE IF NOT EXISTS cart (
  id BIGINT NOT NULL AUTO_INCREMENT,
  product_id BIGINT NOT NULL,
  quantity INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建订单表
CREATE TABLE IF NOT EXISTS orders (
  id BIGINT NOT NULL AUTO_INCREMENT,
  order_number VARCHAR(255) NOT NULL UNIQUE,
  order_date DATETIME NOT NULL,
  total_price DOUBLE NOT NULL,
  status VARCHAR(50) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建订单项表
CREATE TABLE IF NOT EXISTS order_item (
  id BIGINT NOT NULL AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity INT NOT NULL,
  price DOUBLE NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
  FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 插入默认管理员数据
INSERT INTO admin (username, password) VALUES ('admin', 'admin123') ON DUPLICATE KEY UPDATE password = 'admin123';

-- 插入默认商品数据
INSERT INTO product (name, price, stock, description) VALUES
('苹果', 5.99, 100, '新鲜苹果，口感甜美'),
('香蕉', 3.99, 150, '新鲜香蕉，软糯香甜'),
('橙子', 4.99, 80, '新鲜橙子，酸甜可口'),
('草莓', 12.99, 50, '新鲜草莓，香甜多汁')
ON DUPLICATE KEY UPDATE price = VALUES(price), stock = VALUES(stock), description = VALUES(description);