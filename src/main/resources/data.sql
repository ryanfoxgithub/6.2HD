-- Create Users Table (For SQL Injection Vulnerability)
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Insert some test data
INSERT INTO users (username, password) VALUES ('admin', 'admin');
INSERT INTO users (username, password) VALUES ('user', 'user');

-- Create Customers Table
CREATE TABLE customers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

-- Insert test customer data
INSERT INTO customers (first_name, last_name, email) VALUES ('John', 'Doe', 'john.doe@example.com');
INSERT INTO customers (first_name, last_name, email) VALUES ('Jane', 'Smith', 'jane.smith@example.com');
INSERT INTO customers (first_name, last_name, email) VALUES ('Alice', 'Johnson', 'alice.johnson@example.com');
INSERT INTO customers (first_name, last_name, email) VALUES ('Bob', 'Brown', 'bob.brown@example.com');

-- Create Purchases Table
CREATE TABLE purchases (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    item VARCHAR(255),
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- Insert test purchase history data
INSERT INTO purchases (customer_id, item) VALUES (1, 'Laptop');
INSERT INTO purchases (customer_id, item) VALUES (1, 'Phone');
INSERT INTO purchases (customer_id, item) VALUES (2, 'Tablet');
INSERT INTO purchases (customer_id, item) VALUES (2, 'Camera');
INSERT INTO purchases (customer_id, item) VALUES (3, 'Headphones');
INSERT INTO purchases (customer_id, item) VALUES (4, 'Smartwatch');
