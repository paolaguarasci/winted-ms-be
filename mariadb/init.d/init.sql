CREATE
DATABASE `winted-address-service`;
CREATE
USER 'winted-address-service'@'%' IDENTIFIED BY 'winted-address-service';
GRANT ALL PRIVILEGES ON `winted-address-service`.* To
'winted-address-service'@'%' IDENTIFIED BY 'winted-address-service';

CREATE
DATABASE `winted-order-service`;
CREATE
USER 'winted-order-service'@'%' IDENTIFIED BY 'winted-order-service';
GRANT ALL PRIVILEGES ON `winted-order-service`.* To
'winted-order-service'@'%' IDENTIFIED BY 'winted-order-service';

CREATE
DATABASE `winted-payment-service`;
CREATE
USER 'winted-payment-service'@'%' IDENTIFIED BY 'winted-payment-service';
GRANT ALL PRIVILEGES ON `winted-payment-service`.* To
'winted-payment-service'@'%' IDENTIFIED BY 'winted-payment-service';

CREATE
DATABASE `winted-resource-service`;
CREATE
USER 'winted-resource-service'@'%' IDENTIFIED BY 'winted-resource-service';
GRANT ALL PRIVILEGES ON `winted-resource-service`.* To
'winted-resource-service'@'%' IDENTIFIED BY 'winted-resource-service';

GRANT ALL PRIVILEGES ON *.* TO 'winted-resource-service'@'%' WITH GRANT OPTION;

FLUSH
PRIVILEGES;
