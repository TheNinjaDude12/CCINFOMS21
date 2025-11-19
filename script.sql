-- ============================================
-- GAME MANAGEMENT DATABASE - COMPLETE SCHEMA
-- Database: gamemanagementdatabase
-- ============================================

-- ============================================
-- STEP 1: CREATE DATABASE
-- ============================================

DROP DATABASE IF EXISTS gamemanagementdatabase;
CREATE DATABASE gamemanagementdatabase;
USE gamemanagementdatabase;

-- ============================================
-- STEP 2: CREATE TABLES
-- ============================================

-- Table 1: Publisher Record
CREATE TABLE publisher_record(
    publisher_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    country VARCHAR(100),
    established_date DATE,
    website VARCHAR(100),
    contact_email VARCHAR(100),
    company_size INT DEFAULT 0,
    specialization VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    total_games_published INT DEFAULT 0,
    revenue_share_percentage DECIMAL(5,2),
    publisher_budget_range FLOAT DEFAULT 0.0
);

-- Table 2: Developer Record
CREATE TABLE developer_record(
    developer_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(100) DEFAULT NULL,
    email VARCHAR(300) NOT NULL,
    website VARCHAR(255) DEFAULT NULL,
    games_developed INT DEFAULT 0
);

-- Table 3: Customer Record
CREATE TABLE customer_record(
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    last_name VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    email VARCHAR(300) NOT NULL UNIQUE,
    password VARCHAR(45) NOT NULL,
    registration_date DATE NOT NULL,
    country VARCHAR(100),
    preferred_platform VARCHAR(200) DEFAULT 'None',
    total_spent DECIMAL(10,2) DEFAULT 0.00,
    games_owned INT DEFAULT 0
);

-- Table 4: Game Record
CREATE TABLE game_record(
	game_id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
	title varchar(50) NOT NULL,
	genre varchar(50),
	release_date DATE,
	platform ENUM('Console', 'PC', 'Mobile', 'Switch', 'VR'),
	price float DEFAULT 0.0,
	total_bought int DEFAULT 0,
	status ENUM('Beta', 'Under Development', 'Released'),
	review_average float DEFAULT 0,
	reviews int DEFAULT 0,
	publisher_id int NOT NULL,
	FOREIGN KEY (publisher_id) REFERENCES publisher_record(publisher_id),
	developer_id int,
	FOREIGN KEY (developer_id) REFERENCES developer_record(developer_id)
);

-- Table 5: Transaction Log
CREATE TABLE transaction_log(
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    game_id INT NOT NULL,
    purchase_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    payment_method VARCHAR(50) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) DEFAULT 'Paid',
    FOREIGN KEY (customer_id) REFERENCES customer_record(customer_id) ON DELETE RESTRICT,
    FOREIGN KEY (game_id) REFERENCES game_record(game_id) ON DELETE RESTRICT,
    UNIQUE KEY unique_customer_game (customer_id, game_id)
);

-- Table 6: Review Record
CREATE TABLE review_record(
    review_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    customer_id INT NOT NULL, 
    game_id INT NOT NULL, 
    date_posted DATE,
    rating INT NOT NULL,
    comment VARCHAR(300) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer_record(customer_id),
    FOREIGN KEY (game_id) REFERENCES game_record(game_id)
);

-- ============================================
-- STEP 3: CREATE VIEWS FOR REPORTS
-- ============================================

-- View 1: Customer Engagement Report by Year
CREATE OR REPLACE VIEW customerEngagementReportYear AS
SELECT 
    YEAR(t.purchase_date) AS year,
    c.customer_id,
    c.first_name,
    c.last_name,
    c.email,
    COUNT(t.transaction_id) AS total_purchases,
    SUM(t.amount) AS total_spent
FROM customer_record c
JOIN transaction_log t ON c.customer_id = t.customer_id
WHERE t.status = 'Paid'
GROUP BY YEAR(t.purchase_date), c.customer_id, c.first_name, c.last_name, c.email
ORDER BY year DESC, total_spent DESC;

-- View 2: Customer Engagement Report by Month
CREATE OR REPLACE VIEW customerEngagementReportMonth AS
SELECT 
    YEAR(t.purchase_date) AS year,
    MONTH(t.purchase_date) AS month,
    c.customer_id,
    c.first_name,
    c.last_name,
    c.email,
    COUNT(t.transaction_id) AS total_purchases,
    SUM(t.amount) AS total_spent
FROM customer_record c
JOIN transaction_log t ON c.customer_id = t.customer_id
WHERE t.status = 'Paid'
GROUP BY YEAR(t.purchase_date), MONTH(t.purchase_date), c.customer_id, c.first_name, c.last_name, c.email
ORDER BY year DESC, month DESC, total_spent DESC;

-- ============================================
-- STEP 4: INSERT PUBLISHERS
-- ============================================

INSERT INTO publisher_record (
    publisher_id, name, country, established_date, website, contact_email, 
    company_size, specialization, is_active, total_games_published, 
    revenue_share_percentage, publisher_budget_range
) VALUES
(1, 'Solarbyte Studios', 'United States', '2010-03-15', 'https://www.solarbyte.com', 'contact@solarbyte.com', 320, 'RPG', TRUE, 0, 24.50, 6.3),
(2, 'BlueNova Interactive', 'Japan', '2014-07-22', 'https://www.bluenova.jp', 'info@bluenova.jp', 210, 'Action', TRUE, 0, 30.00, 2.8),
(3, 'IronPulse Games', 'United States', '2006-01-10', 'https://www.ironpulse.ca', 'support@ironpulse.ca', 980, 'FPS', TRUE, 0, 22.25, 9.7),
(4, 'Stormveil Entertainment', 'Japan', '1998-06-05', 'https://www.stormveil.de', 'hello@stormveil.de', 720, 'Strategy', TRUE, 0, 19.80, 14.1),
(5, 'DreamArcade Studio', 'Philippines', '2011-10-01', 'https://www.dreamarcade.ph', 'contact@dreamarcade.ph', 290, 'Adventure', TRUE, 0, 20.75, 3.9),
(6, 'TitanEdge Publishing', 'United States', '2002-02-27', 'https://www.titanedge.com', 'info@titanedge.com', 1200, 'Sports', TRUE, 0, 28.40, 11.6),
(7, 'QuantumLeaf Games', 'Japan', '2016-09-12', 'https://www.quantumleaf.jp', 'team@quantumleaf.jp', 150, 'Puzzle', TRUE, 0, 18.60, 2.1),
(8, 'PixelForge Studios', 'Philippines', '2018-05-20', 'https://www.pixelforge.ph', 'support@pixelforge.ph', 95, 'Platformer', TRUE, 0, 16.40, 1.5),
(9, 'RedHorizon Labs', 'United States', '2004-11-08', 'https://www.redhorizon.us', 'info@redhorizon.us', 670, 'Simulation', TRUE, 0, 27.00, 8.2),
(10, 'SakuraSky Digital', 'Japan', '2009-04-18', 'https://www.sakurasky.jp', 'contact@sakurasky.jp', 430, 'Horror', TRUE, 0, 23.10, 5.4);

-- ============================================
-- STEP 5: INSERT DEVELOPERS
-- ============================================



-- ============================================
-- STEP 6: RESET GAME/CUSTOMER/TRANSACTION DATA
-- ============================================

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE review_record;
TRUNCATE TABLE transaction_log;
TRUNCATE TABLE customer_record;
TRUNCATE TABLE game_record;

ALTER TABLE customer_record AUTO_INCREMENT = 1;
ALTER TABLE transaction_log AUTO_INCREMENT = 1;
ALTER TABLE game_record AUTO_INCREMENT = 1;
ALTER TABLE review_record AUTO_INCREMENT = 1;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- STEP 7: INSERT GAMES
-- ============================================



-- ============================================
-- STEP 8: INSERT CUSTOMERS
-- ============================================

INSERT INTO customer_record (customer_id, last_name, first_name, email, password, registration_date, country, preferred_platform, total_spent, games_owned) VALUES
(1, 'Smith', 'Amber', 'amber.smith@email.com', '1234', '2024-02-01', 'United States', 'PC', 0, 0),
(2, 'Garcia', 'Maria', 'maria.garcia@email.com', '1234', '2025-01-28', 'Spain', 'PlayStation', 0, 0),
(3, 'Johnson', 'Liam', 'liam.johnson@email.com', '1234', '2024-03-10', 'Canada', 'Xbox', 0, 0),
(4, 'Kim', 'Min-jun', 'minjun.kim@email.com', '1234', '2024-03-25', 'South Korea', 'PC', 0, 0),
(5, 'Silva', 'Pedro', 'pedro.silva@email.com', '1234', '2024-04-05', 'Brazil', 'Xbox', 0, 0),
(6, 'Johnson', 'Emma', 'emma.johnson@email.com', '1234', '2024-05-12', 'United Kingdom', 'PC', 0, 0),
(7, 'Lopez', 'Carlos', 'carlos.lopez@email.com', '1234', '2024-06-18', 'Mexico', 'PlayStation', 0, 0),
(8, 'Wang', 'Wei', 'wei.wang@email.com', '1234', '2024-07-22', 'China', 'Mobile', 0, 0),
(9, 'Müller', 'Anna', 'anna.muller@email.com', '1234', '2024-08-30', 'Germany', 'PC', 0, 0),
(10, 'Patel', 'Raj', 'raj.patel@email.com', '1234', '2024-09-15', 'India', 'Mobile', 0, 0);

-- ============================================
-- STEP 9: INSERT TRANSACTIONS

-- ============================================
INSERT INTO developer_record (developer_id, name, country, email, website, games_developed) VALUES
(1, 'CD Projekt Red', 'Poland', 'contact@cdprojektred.com', 'https://www.cdprojektred.com/', 2),
(2, 'Nintendo EPD', 'Japan', 'contact@nintendo.co.jp', 'https://www.nintendo.com/', 3),
(3, 'Rockstar Games', 'USA', 'info@rockstargames.com', 'https://www.rockstargames.com/', 2),
(4, 'FromSoftware', 'Japan', 'support@fromsoftware.jp', 'https://www.fromsoftware.jp/', 1),
(5, 'Larian Studios', 'Belgium', 'info@larian.com', 'https://larian.com/', 1),
(6, 'nom', 'nom', 'nom@gmail.com', 'www.nomnom.com.ph', 0),
(7, 'Ubisoft Montreal', 'Canada', 'support@ubisoft.com', 'https://www.ubisoft.com/', 4),
(8, 'Square Enix', 'Japan', 'support@square-enix.com', 'https://www.square-enix-games.com/', 3),
(9, 'Valve Corporation', 'USA', 'contact@valvesoftware.com', 'https://www.valvesoftware.com/', 3),
(10, 'Game Freak', 'Japan', 'contact@gamefreak.co.jp', 'https://www.gamefreak.co.jp/', 2);

INSERT INTO game_record(title, genre, release_date, platform, price, total_bought, status, review_average, reviews, publisher_id, developer_id)
VALUES
("Persona 3 Reload", "Role-Playing Game", "2024-02-02", "Console", 2399.00, 200, 'Released', 5.0, 100, 1, 1),
("Metaphor: reFantazio", "Role-Playing Game", "2024-10-11", "Console", 2895.00, 200, 'Released', 4.5, 100, 1, 2),
("Tekken 8", "Fighting Game", "2024-01-26", "Console", 2499.00, 300, 'Released', 3.0, 50, 2, 3),
("Hearts of Iron IV", "Strategy Game", "2016-06-06", "PC", 1999.00, 150, 'Released', 4.5, 200, 3, 3),
("Grand Theft Auto VI", "Action-Adventure", "2026-05-26", "Console", 0, 0, 'Under Development', 0, 0, 4, 4),
("Animal Crossing: New Horizons", "Social Simulation", "2020-03-20", 'Switch', 2250.00, 300, 'Released', 4, 150, 5, 5),
("Arknights: Endfield", "Action Role-Playing Game", null, 'Mobile', 0, 0, 'Beta', 0, 0, 6, 6),
("Crusader Kings III", "Strategy Game", "2020-09-01", 'PC', 1999.00, 350, 'Released', 4.5, 200, 3, 7),
("Limbus Company", "Turn-based RPG", "2023-02-26", 'Mobile', 0, 100, 'Released', 4, 50, 7, 8),
("Hollow Knight", "Metroidvania", "2017-02-24", 'PC', 485.00, 200, 'Released', 5.0, 150, 8, 9);


INSERT INTO transaction_log (customer_id, game_id, purchase_date, payment_method, amount, status) VALUES
(1, 1, '2024-02-15 14:23:00', 'Credit Card', 2399.00, 'Paid'),
(1, 4, '2024-03-20 10:15:00', 'Credit Card', 1999.00, 'Paid'),
(1, 10, '2024-05-10 16:45:00', 'PayPal', 485.00, 'Paid'),
(2, 2, '2025-02-01 12:00:00', 'PayPal', 2895.00, 'Paid'),
(2, 3, '2025-03-15 15:30:00', 'Credit Card', 2499.00, 'Paid'),
(2, 6, '2025-05-20 11:45:00', 'Debit Card', 2250.00, 'Paid'),
(3, 4, '2024-04-10 13:20:00', 'Credit Card', 1999.00, 'Paid'),
(3, 8, '2024-04-11 14:10:00', 'Credit Card', 1999.00, 'Paid'),
(3, 10, '2024-05-20 10:05:00', 'PayPal', 485.00, 'Paid'),
(4, 1, '2024-04-01 08:00:00', 'Credit Card', 2399.00, 'Paid'),
(4, 2, '2024-04-05 09:15:00', 'Credit Card', 2895.00, 'Paid'),
(4, 3, '2024-05-10 17:30:00', 'PayPal', 2499.00, 'Paid'),
(4, 4, '2024-05-15 12:45:00', 'Credit Card', 1999.00, 'Paid'),
(4, 6, '2024-06-01 14:20:00', 'Credit Card', 2250.00, 'Paid'),
(4, 8, '2024-06-20 16:10:00', 'PayPal', 1999.00, 'Paid'),
(4, 9, '2024-07-10 11:25:00', 'Credit Card', 0.00, 'Paid'),
(4, 10, '2024-08-05 15:50:00', 'Credit Card', 485.00, 'Paid'),
(5, 1, '2024-04-20 09:30:00', 'Credit Card', 2399.00, 'Paid'),
(5, 4, '2024-05-15 14:15:00', 'PayPal', 1999.00, 'Paid'),
(5, 6, '2024-06-30 16:45:00', 'Credit Card', 2250.00, 'Paid'),
(5, 10, '2024-08-12 11:20:00', 'Debit Card', 485.00, 'Paid'),
(6, 2, '2024-05-20 10:00:00', 'PayPal', 2895.00, 'Paid'),
(6, 3, '2024-05-21 10:30:00', 'PayPal', 2499.00, 'Paid'),
(6, 8, '2024-06-01 15:20:00', 'Credit Card', 1999.00, 'Paid'),
(6, 9, '2024-07-10 12:40:00', 'Debit Card', 0.00, 'Paid'),
(6, 10, '2024-08-15 14:55:00', 'Credit Card', 485.00, 'Paid'),
(8, 1, '2024-08-01 13:20:00', 'Gcash', 2399.00, 'Paid'),
(8, 9, '2024-09-10 15:40:00', 'Gcash', 0.00, 'Paid'),
(8, 10, '2024-10-20 10:25:00', 'Credit Card', 485.00, 'Paid'),
(9, 1, '2024-09-05 09:15:00', 'PayPal', 2399.00, 'Paid'),
(9, 2, '2024-09-10 11:30:00', 'PayPal', 2895.00, 'Paid'),
(9, 4, '2024-09-20 13:45:00', 'Credit Card', 1999.00, 'Paid'),
(9, 6, '2024-10-01 15:20:00', 'PayPal', 2250.00, 'Paid'),
(9, 8, '2024-10-15 10:50:00', 'Debit Card', 1999.00, 'Paid'),
(10, 4, '2024-10-01 16:25:00', 'Gcash', 1999.00, 'Paid'),
(10, 9, '2024-10-10 11:40:00', 'Gcash', 0.00, 'Paid'),
(10, 10, '2024-11-01 13:55:00', 'Credit Card', 485.00, 'Paid');

-- ============================================
-- STEP 10: INSERT REVIEWS
-- ============================================

INSERT INTO review_record(review_id, customer_id, game_id, date_posted, rating, comment) VALUES 
(1, 1, 1, '2024-02-22', 4, 'A fun and polished experience—easy to recommend.'), 
(2, 1, 5, '2024-05-15', 3, 'Solid core gameplay, but it could use more content.'), 
(3, 1, 4, '2024-09-12', 5, 'Great gameplay, smooth controls, and tons of charm.'), 
(4, 2, 4, '2025-05-20', 4, 'Surprisingly addictive. I kept wanting one more round.'), 
(5, 3, 5, '2024-04-12', 2, 'Looks promising, but the gameplay gets repetitive fast.'), 
(6, 3, 9, '2024-07-18', 4, 'Enjoyable, but the difficulty curve is a bit rough.'), 
(7, 3, 3, '2024-05-20', 3, 'Fun in short bursts, though the pacing feels uneven.'), 
(8, 4, 1, '2024-04-01', 4, 'Delivers exactly what it promises and more.'), 
(9, 4, 2, '2024-04-05', 5, 'Beautiful visuals and a soundtrack that fits perfectly.'), 
(10, 4, 3, '2024-06-10', 3, 'Theres fun to be had, but it takes time to get going.'), 
(11, 4, 4, '2024-11-15', 5, 'A standout title that nails both style and substance.'), 
(12, 4, 5, '2024-06-01', 1, 'Unbalanced mechanics make it more frustrating than fun.'), 
(13, 4, 6, '2024-12-20', 2, 'Technical issues pull you out of the experience.'), 
(14, 4, 7, '2025-07-10', 3, 'Great presentation, but the gameplay doesnt always match it.'), 
(15, 4, 8, '2024-08-15', 4, 'Moments of brilliance mixed with moments of frustration.'), 
(16, 4, 9, '2024-09-05', 5, 'Engaging from start to finish, really well-crafted.'), 
(17, 4, 10, '2024-10-15', 1, 'Lack of depth makes it hard to stay engaged.'), 
(18, 5, 10, '2024-08-12', 2, 'Fails to capture the excitement it aims for.'), 
(19, 5, 1, '2024-10-05', 5, 'Clever design choices make this game feel fresh.'), 
(20, 6, 2, '2024-05-20', 5, 'Smooth performance and satisfying gameplay loops.'), 
(21, 8, 7, '2024-09-10', 2, 'Fails to capture the excitement it aims for.'), 
(22, 8, 3, '2024-11-05', 4, 'Its good, but it feels like it needed a bit more polish.'), 
(23, 9, 9, '2024-10-01', 3, 'Enjoyable overall, even if some parts fall flat.'), 
(24, 10, 7, '2024-10-01', 1, 'The mechanics feel clunky and unrefined.'), 
(25, 10, 8, '2024-10-10', 2, 'Story and gameplay never really come together.'), 
(26, 10, 1, '2024-11-01', 3, 'A forgettable experience with little to keep you invested.'), 
(27, 10, 2, '2024-11-05', 1, 'Buggy, unbalanced, and tough to recommend in its current state.');

-- ============================================
-- STEP 11: UPDATE AGGREGATED TOTALS
-- ============================================

-- Update customer totals
UPDATE customer_record c
SET 
    total_spent = (
        SELECT IFNULL(SUM(amount), 0) 
        FROM transaction_log 
        WHERE customer_id = c.customer_id AND status = 'Paid'
    ),
    games_owned = (
        SELECT COUNT(*) 
        FROM transaction_log 
        WHERE customer_id = c.customer_id AND status = 'Paid'
    );

-- Update game totals
UPDATE game_record g
SET total_bought = (
    SELECT COUNT(*) 
    FROM transaction_log 
    WHERE game_id = g.game_id AND status = 'Paid'
);

-- Update publisher totals
UPDATE publisher_record p
SET total_games_published = (
    SELECT COUNT(*) 
    FROM game_record 
    WHERE publisher_id = p.publisher_id
);

-- Update game review statistics from review_record
UPDATE game_record g
SET 
    reviews = (
        SELECT COUNT(*) 
        FROM review_record 
        WHERE game_id = g.game_id
    ),
    review_average = (
        SELECT IFNULL(AVG(rating), 0) 
        FROM review_record 
        WHERE game_id = g.game_id
    );

-- ============================================
-- VERIFICATION QUERIES
-- ============================================

SELECT '✓ DATABASE CREATED SUCCESSFULLY!' as message,
       (SELECT COUNT(*) FROM publisher_record) as publishers,
       (SELECT COUNT(*) FROM developer_record) as developers,
       (SELECT COUNT(*) FROM customer_record) as customers,
       (SELECT COUNT(*) FROM game_record) as games,
       (SELECT COUNT(*) FROM transaction_log) as transactions,
       (SELECT COUNT(*) FROM review_record) as reviews,
       (SELECT SUM(total_spent) FROM customer_record) as total_revenue;

-- Show game review statistics
SELECT 
    g.game_id,
    g.title,
    g.reviews as review_count,
    ROUND(g.review_average, 2) as avg_rating
FROM game_record g
ORDER BY g.game_id;