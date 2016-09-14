
-- INI Feature data
INSERT INTO Feature (id, createDate, lastUpdateDate, application, description, displayName, featureName)
VALUES
(1, now(), now(), 'pagoda', 'Allow login to pagoda', 'allowLoginPagoda', 'allowLoginPagoda');

-- INIT Role data
INSERT INTO Role(id, createDate, lastUpdateDate, description, displayName, `name`, `enable`)
VALUES
(1, now(), now(), 'System Admin', 'System Admin', 'admin', 'Y'),
(2, now(), now(), 'System Manager', 'System Manager', 'pagoda', 'Y'),
(3, now(), now(), 'User', 'User', 'user', 'Y');

-- INIT RoleFeature data
INSERT INTO RoleFeature(id, createDate, lastUpdateDate, featureId, roleId)
VALUES
(1, now(), now(), 1, 1),
(2, now(), now(), 1, 2),
(3, now(), now(), 1, 3);

-- INIT User data
INSERT INTO `User` (id, createDate, lastUpdateDate, firstName, lastName, username, password, email, enable, expired, locked, disabled, credentialsExpired, changePassword)
VALUES
(1, now(), now(), 'System', 'Amdin', 'admin', '$2a$08$BfGIccyHocY7oBitGAIhE.NBhM78.k1fK6fnzP96Ccid5ynnsM/Tm', 'admin@pagodao.com', 'Y', 'N', 'N','N', 'N', 'N'),
(2, now(), now(), 'System', 'Pagoda', 'pagoda', '$2a$08$BfGIccyHocY7oBitGAIhE.NBhM78.k1fK6fnzP96Ccid5ynnsM/Tm', 'pagoda@pagodao.com', 'Y', 'N', 'N','N', 'N', 'N'),
(3, now(), now(), 'Pagoda', 'Tester', 'pagodatester', '$2a$08$BfGIccyHocY7oBitGAIhE.NBhM78.k1fK6fnzP96Ccid5ynnsM/Tm', 'pagodaTester@pagodao.com', 'Y', 'N', 'N','N', 'N', 'N');

-- INIT UserRole data
INSERT INTO UserRole(userId, roleId)
VALUES
(1,1),
(2,1),
(3,1);

-- INIT AppType data
INSERT INTO AppType (createDate, lastUpdateDate, active, appName, description)
VALUES 
(now(), now(), 'Y', 'JD_PC', '京东PC端'),
(now(), now(), 'Y', 'JD_M', '京东M端'),
(now(), now(), 'Y', 'JD_WeiXin', '京东微信端'),
(now(), now(), 'Y', 'YHD_PC', '一号店PC端'),
(now(), now(), 'Y', 'YHD_M', '一号店M端');
