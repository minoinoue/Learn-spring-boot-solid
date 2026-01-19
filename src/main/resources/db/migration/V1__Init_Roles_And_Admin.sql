-- roles
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    role VARCHAR(20) NOT NULL
);

-- users
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- user_roles
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- students
CREATE TABLE students (
    student_id VARCHAR(255) PRIMARY KEY, 
    full_name VARCHAR(255),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    id BIGINT,
    CONSTRAINT fk_student_user FOREIGN KEY (id) REFERENCES users(id) ON DELETE SET NULL
);

--refreshtoken
CREATE TABLE refreshtoken (
    id BIGSERIAL PRIMARY KEY, 
    token VARCHAR(255) NOT NULL UNIQUE, 
    expiry_date TIMESTAMP NOT NULL,
    user_id BIGINT,
    CONSTRAINT fk_refresh_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO roles (role)
SELECT 'ROLE_ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE role = 'ROLE_ADMIN'
);

INSERT INTO roles (role)
SELECT 'ROLE_STUDENT'
WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE role = 'ROLE_STUDENT'
);


INSERT INTO users (user_name, password)
SELECT 'admin', '$2a$10$RGsvBCwAy6wOFtH5GvDMMe327U7oCut0Xjz0EE0bb7TcpGbgYaaSK' -- password: admin@123 --
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE user_name = 'admin'
);


INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.user_name = 'admin' AND r.role = 'ROLE_ADMIN'
AND NOT EXISTS (
    SELECT 1 FROM user_roles ur 
    WHERE ur.user_id = (SELECT id FROM users WHERE user_name = 'admin') 
      AND ur.role_id = (SELECT id FROM roles WHERE role = 'ROLE_ADMIN')
);