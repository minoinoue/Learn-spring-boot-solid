INSERT INTO users (user_name, password)
SELECT 'admin', '$2a$10$RGsvBCwAy6wOFtH5GvDMMe327U7oCut0Xjz0EE0bb7TcpGbgYaaSK'
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