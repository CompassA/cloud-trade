#!/bin/zsh

#创建sequence
sequence_db_name="sequence_db"
sequence_table_name="sequence"
mysql -uroot -proot -e "
    CREATE DATABASE IF NOT EXISTS $sequence_db_name;
    USE $sequence_db_name;
    CREATE TABLE IF NOT EXISTS $sequence_table_name (
        id INT NOT NULL,
        name VARCHAR(64) NOT NULL,
        value BIGINT NOT NULL DEFAULT 0,
        step BIGINT NOT NULL DEFAULT 100,
        create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        PRIMARY KEY(id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
    INSERT INTO $sequence_table_name(id, name) VALUES (1, 'commodity_sequence');
"

# 创建2个商品库 各8张表
for i in {0..1}; do
    data_base_name="product_db_$(printf '%02d' $((i)))"
    mysql -uroot -proot -e "CREATE DATABASE IF NOT EXISTS $data_base_name; "
    for j in {0..7}; do
        commodity_table_name="commodity_$(printf '%04d' $((j)))"
        commodity_snapshot_name="commodity_snapshot_$(printf '%04d' $((j)))"
        mysql -uroot -proot -e "
            USE $data_base_name;
            CREATE TABLE IF NOT EXISTS $commodity_table_name (
                id BIGINT NOT NULL,
                shop_id BIGINT NOT NULL,
                name VARCHAR(128) NOT NULL,
                price DECIMAL(14,2) NOT NULL,
                version BIGINT NOT NULL,
                create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                PRIMARY KEY(id),
                INDEX idx_shop_id(shop_id)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
            CREATE TABLE IF NOT EXISTS $commodity_snapshot_name (
                id BIGINT NOT NULL,
                version BIGINT NOT NULL,
                shop_id BIGINT NOT NULL,
                name VARCHAR(128) NOT NULL,
                price DECIMAL(14,2) NOT NULL,
                create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                PRIMARY KEY(id, version)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
    done
done