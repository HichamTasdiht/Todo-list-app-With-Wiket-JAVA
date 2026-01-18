-- 1. Create the Database
CREATE DATABASE IF NOT EXISTS todo_db;
USE todo_db;

-- 2. Create the Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50)
) ENGINE=InnoDB;

-- 3. Create the Tasks Table
CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    status ENUM('PENDING', 'DONE') DEFAULT 'PENDING',
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id) 
        REFERENCES users(id) 
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- 4. Optional: Insert a test user (password is 'password' - you should hash this in a real app)
-- INSERT INTO users (username, password, first_name, last_name) 
-- VALUES ('admin', 'password', 'Admin', 'User');