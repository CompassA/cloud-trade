-- -------------------------------------------------
-- order model --
CREATE TABLE IF NOT EXISTS `order_master` (
    `order_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `shop_id` BIGINT NOT NULL,
    `telephone` BIGINT NOT NULL,
    `amount` DECIMAL(14, 2) NOT NULL,
    `order_status` TINYINT NOT NULL DEFAULT 0,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`order_id`),
    INDEX `idx_user_id`(`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- sharding --
CREATE TABLE IF NOT EXISTS `order_master_0000` (
    `order_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `shop_id` BIGINT NOT NULL,
    `telephone` BIGINT NOT NULL,
    `amount` DECIMAL(14, 2) NOT NULL,
    `order_status` TINYINT NOT NULL DEFAULT 0,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`order_id`),
    INDEX `idx_user_id`(`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `order_master_0001` (
    `order_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `shop_id` BIGINT NOT NULL,
    `telephone` BIGINT NOT NULL,
    `amount` DECIMAL(14, 2) NOT NULL,
    `order_status` TINYINT NOT NULL DEFAULT 0,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`order_id`),
    INDEX `idx_user_id`(`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `order_master_0002` (
    `order_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `shop_id` BIGINT NOT NULL,
    `telephone` BIGINT NOT NULL,
    `amount` DECIMAL(14, 2) NOT NULL,
    `order_status` TINYINT NOT NULL DEFAULT 0,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`order_id`),
    INDEX `idx_user_id`(`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `order_master_0003` (
    `order_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `shop_id` BIGINT NOT NULL,
    `telephone` BIGINT NOT NULL,
    `amount` DECIMAL(14, 2) NOT NULL,
    `order_status` TINYINT NOT NULL DEFAULT 0,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`order_id`),
    INDEX `idx_user_id`(`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `order_commodity` (
    `commodity_id` BIGINT NOT NULL,
    `order_id` BIGINT NOT NULL,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- -----------------------------------------------
-- commodity model --
CREATE TABLE IF NOT EXISTS `commodity` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `shop_id` BIGINT NOT NULL,
    `name` VARCHAR(128) NOT NULL,
    `price` DECIMAL(14,2) NOT NULL,
    `version` BIGINT NOT NULL,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`id`),
    INDEX `idx_shop_id`(`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `commodity_snapshot` (
    `id` BIGINT NOT NULL,
    `version` BIGINT NOT NULL,
    `shop_id` BIGINT NOT NULL,
    `name` VARCHAR(128) NOT NULL,
    `price` DECIMAL(14,2) NOT NULL,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`id`, `version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- -----------------------------------------------
-- user model --
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `nick` VARCHAR(32) NOT NULL,
    `gender` TINYINT NOT NULL DEFAULT 0,
    `telephone` INT,
    `password` VARCHAR(64) NOT NULL,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`id`),
    UNIQUE KEY `uni_idx_telephone`(`telephone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
