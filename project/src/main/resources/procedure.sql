INSERT INTO `point_wallet`(user_id, amount) VALUES('user1',0);
INSERT INTO `point_wallet`(user_id, amount) VALUES('user2',0);

DELIMITER //
CREATE PROCEDURE bulkInsert()
BEGIN
DECLARE i INT DEFAULT 1;
DECLARE random_amount bigint DEFAULT 1;
WHILE(i <= 3000) DO
SET random_amount = FLOOR(1000 + (RAND() * 9000));
INSERT INTO `point_reservation`(point_wallet_id, amount, earned_date, available_days, is_executed)
VALUES(1, random_amount, '2022-03-20', 10, 0);
SET i = i+1;
END WHILE;
END;
//
DELIMITER ;
drop procedure if exists bulkInsert;