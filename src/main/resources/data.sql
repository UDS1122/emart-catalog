DROP TABLE IF EXISTS Cart;
DROP TABLE IF EXISTS Product;

CREATE TABLE Cart (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  productid INT NOT NULL,
  userid VARCHAR(250) NOT NULL,
  quantity INT NOT NULL,
  price decimal NOT NULL
);

CREATE TABLE Product (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  type VARCHAR(250) NOT NULL,
  make VARCHAR(100) NOT NULL,
  model VARCHAR(250) NOT NULL,
  price decimal NOT NULL,
  available boolean NULL
);

INSERT INTO Product (name, type, make, model, price, available) VALUES ('iPhone 8', 'Mobile', 'Apple', 	'Version 8', 650.38,  true);
INSERT INTO Product (name, type, make, model, price, available) VALUES ('Mac PRO' , 'Laptop', 'Apple', 	'2021 v2'  , 1238.99, true);
INSERT INTO Product (name, type, make, model, price, available) VALUES ('Note 8'  , 'Mobile', 'Samsung', 	'8 v2'     , 850.78,  true);