CREATE TABLE IF NOT EXISTS `commodity` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `shop_id` BIGINT NOT NULL,
    `name` VARCHAR(128) NOT NULL,
    `price` DECIMAL(14,2) NOT NULL,
    `version` BIGINT NOT NULL,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`id`),
    INDEX idx_shop_id(`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `commodity_snapshot` (
    `id` BIGINT NOT NULL,
    `version` BIGINT NOT NULL,
    `shop_id` BIGINT NOT NULL,
    `name` VARCHAR(128) NOT NULL,
    `price` DECIMAL(14,2) NOT NULL,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(`id`, `version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;