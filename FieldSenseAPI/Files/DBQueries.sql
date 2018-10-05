
/* 08 may 2013 */
CREATE TABLE `appointments_possition` (
  `id` int(7) NOT NULL AUTO_INCREMENT,
  `appointment_id_fk` int(7) NOT NULL,
  `user_id_fk` int(7) NOT NULL,
  `appointment_possition` int(7) NOT NULL,
  `cretaed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modified_on` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
)

/* 13 May 2014 */

ALTER TABLE `attendances` ADD COLUMN `status` TINYINT(2) NOT NULL DEFAULT 1  AFTER `punch_out_langitude` ;

ALTER TABLE `attendances` CHANGE COLUMN `status` `status` TINYINT(2) NOT NULL DEFAULT '1' COMMENT '0 - offline\n1 - online '  ;

/* 14 May 2014 */

ALTER TABLE `customers` ADD COLUMN `modified_on` DATETIME NULL  AFTER `created_on` ;

/* 16 May 2014 */

ALTER TABLE `attendances` CHANGE COLUMN `punch_out_location` `punch_out_location` VARCHAR(1000) NULL DEFAULT ''  , CHANGE COLUMN `punch_out_latitude` `punch_out_latitude` VARCHAR(50) NULL DEFAULT ''  , CHANGE COLUMN `punch_out_langitude` `punch_out_langitude` VARCHAR(50) NULL DEFAULT ''  ;


/* 23 May 2014 */

CREATE  TABLE `activity_outcomes` (
  `id` INT(7) NOT NULL AUTO_INCREMENT ,
  `outcome` VARCHAR(100) NOT NULL ,
  `is_positive` TINYINT(2) NULL COMMENT 'possitive - 1, Negative - 0 ' ,
  `is_active` TINYINT(2) NOT NULL COMMENT '0 - Active , 1- Inactive ' ,
  `created_on` TIMESTAMP NULL ,
  `created_by_id_fk` INT(7) NOT NULL ,
  PRIMARY KEY (`id`) );

/* 27 May 2014 */

CREATE TABLE `appointments` (
  `id` int(7) NOT NULL,
  `appointment_title` varchar(100) NOT NULL,
  `customer_id_fk` int(7) NOT NULL,
  `customer_contact_id_fk` int(7) NOT NULL,
  `user_id_fk` int(7) NOT NULL,
  `assigned_id_fk` int(7) NOT NULL,
  `appointment_time` datetime NOT NULL,
  `appointment_description` varchar(100) NOT NULL,
  `appointment_type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '0-meeting ,1-call',
  `purpose_id_fk` int(7) NOT NULL,
  `outcome` tinyint(2) NOT NULL DEFAULT '0' COMMENT '0 – unknown, 1 - failure; 2 – success		\n',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '0-pending , 1-In progress,2-completed,3-cancelled  ',
  `check_in_lat` double DEFAULT NULL,
  `check_in_long` double DEFAULT NULL,
  `check_in_location` varchar(1000) DEFAULT NULL,
  `check_in_time` datetime DEFAULT NULL,
  `check_out_lat` double DEFAULT NULL,
  `check_out_long` double DEFAULT NULL,
  `check_out_location` varchar(1000) DEFAULT NULL,
  `check_out_time` datetime DEFAULT NULL,
  `next_appointment_id` int(7) DEFAULT NULL,
  `created_on` datetime DEFAULT NULL,
  `created_by_id_fk` int(7) NOT NULL,
  PRIMARY KEY (`id`)
);

ALTER TABLE `appointments` ADD COLUMN `appointment_end_time` DATETIME NOT NULL  AFTER `appointment_time` ;

ALTER TABLE `expenses` ADD COLUMN `expense_time` DATETIME NULL DEFAULT '1111-11-11 11:11:11'  AFTER `status` , ADD COLUMN `rejected_reason` VARCHAR(200) NULL  AFTER `approved_rejected_by_id_fk` , CHANGE COLUMN `status` `status` TINYINT(2) NULL DEFAULT '0' COMMENT '0 – pending, 1 - approved, 2 – rejected\n'  , CHANGE COLUMN `approved_on` `approved__rejected_on` DATETIME NULL DEFAULT '1111-11-11 11:11:11'  , CHANGE COLUMN `approved_by_id_fk` `approved_rejected_by_id_fk` INT(7) NULL DEFAULT '0'  ;

/* 28 May 2014 */

ALTER TABLE `appointments` CHANGE COLUMN `id` `id` INT(7) NOT NULL AUTO_INCREMENT  ;

ALTER TABLE `appointments` CHANGE COLUMN `customer_contact_id_fk` `customer_contact_id_fk` INT(7) NULL  ;

/* 29 May 2014 */

ALTER TABLE `expenses` CHANGE COLUMN `rejected_reason` `rejected_reason` VARCHAR(200) NULL DEFAULT ''  ;

/* 09 june 2014 */

ALTER TABLE customers
ADD CONSTRAINT UK_customer_name_location_identifier UNIQUE (customer_name,customer_location_identifier);

CREATE  TABLE `register_account` (
  `id` INT(7) NOT NULL ,
  `sql_query` TEXT NOT NULL ,
  `version` VARCHAR(10) NOT NULL ,
  `created_on` DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00.0' ,
  PRIMARY KEY (`id`) );

/* 27 june 2014 */

ALTER TABLE `users` ADD COLUMN `isfirstlogin` TINYINT(2) NOT NULL DEFAULT 0  AFTER `last_known_langitude` ;

/* 29 june 2014 */

ALTER TABLE `expenses` CHANGE COLUMN `approved__rejected_on` `approved_rejected_on` DATETIME NULL DEFAULT '1111-11-11 11:11:11'  ;

/* 30 june 2014 */

ALTER TABLE `users` ADD COLUMN `office_latitude` DOUBLE NULL  AFTER `isfirstlogin` ;
ALTER TABLE `users` ADD COLUMN `office_langitude` DOUBLE NULL  AFTER `office_latitude` ;
ALTER TABLE `users` ADD COLUMN `home_latitude` DOUBLE NULL  AFTER `office_langitude` ;
ALTER TABLE `users` ADD COLUMN `home_langitude` DOUBLE NULL  AFTER `home_latitude` ;

/* 01 July 2014 */

ALTER TABLE `users` ADD COLUMN `designation` VARCHAR(100) NOT NULL  AFTER `home_langitude` ;

/* 02 July 2014 */

ALTER TABLE `activity_outcomes` CHANGE COLUMN `id` `id` INT(7) NOT NULL  ;

/* 08 july 2014 */

ALTER TABLE `accounts` ADD COLUMN `user_limit` INT(5) NOT NULL DEFAULT 25  AFTER `status` ;

/* 18 July 2014 */

ALTER TABLE `customer_contacts` CHANGE COLUMN `birth_date` `birth_date` DATE NULL DEFAULT '1800-01-01'  , CHANGE COLUMN `anniversary_date` `anniversary_date` DATE NULL DEFAULT '1800-01-01'  ;

/* 28 July 2014 */

CREATE  TABLE `create_user_deafult_data` (
  `id` INT(7) NOT NULL ,
  `sql_query` TEXT NOT NULL ,
  `version` VARCHAR(10) NOT NULL ,
  `created_on` DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00' ,
  PRIMARY KEY (`id`) );

/* 19 August 2014 */

CREATE  TABLE `create_account_deafult_data` (
  `id` INT(7) NOT NULL ,
  `sql_query` TEXT NOT NULL ,
  `version` VARCHAR(10) NOT NULL ,
  `creted_on` DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00' ,
  PRIMARY KEY (`id`) );

/* 12 September 2014 */

ALTER TABLE `teams` ADD COLUMN `team_position_csv` TEXT NULL AFTER `created_by_id_fk` ;

ALTER TABLE teams ENGINE = MYISAM ;

/* 05 December 2014 */

ALTER TABLE `attendances` ADD COLUMN `punch_in_location_reason` VARCHAR(1000) NULL DEFAULT ''  AFTER `created_on` , ADD COLUMN `punch_out_location_reason` VARCHAR(1000) NULL DEFAULT ''  AFTER `punch_in_location_reason` ;

/* 08 January 2015*/

ALTER TABLE `users` ADD COLUMN `gcm_id` TEXT NULL  AFTER `designation` ;

/* 19 January 2015 */

ALTER TABLE `teams` ADD COLUMN `has_subordinates` TINYINT NOT NULL DEFAULT 0 COMMENT '0- no subordinates,\n1- has subordinates'  AFTER `team_position_csv` ;

/* 18 February 2015 */
ALTER TABLE `users` ADD COLUMN `accountcontact_type` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '1 - Primary contact \n0 - not primary contact '  AFTER `gcm_id` ;

/* 02 March 2015 */

ALTER TABLE `expenses` ADD COLUMN `category_id_fk` INT(7) NULL  AFTER `created_on` ;

CREATE TABLE `expense_categories` (
  `id` int(7) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(100) NOT NULL,
  `is_active` tinyint(2) NOT NULL,
  `created_on` timestamp NULL DEFAULT NULL,
  `created_by_id_fk` int(7) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `category_name_UNIQUE` (`category_name`)
)

INSERT INTO `fieldsense`.`register_account` (`id`, `sql_query`, `version`, `created_on`) VALUES (28, 'CREATE TABLE `expense_categories` (\n  `id` int(7) NOT NULL AUTO_INCREMENT,\n  `category_name` varchar(100) NOT NULL,\n  `is_active` tinyint(2) NOT NULL,\n  `created_on` timestamp NULL DEFAULT NULL,\n  `created_by_id_fk` int(7) NOT NULL,\n  PRIMARY KEY (`id`),\n  UNIQUE KEY `id_UNIQUE` (`id`),\n  UNIQUE KEY `category_name_UNIQUE` (`category_name`)\n) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;', '1', now());

/* 11 March 2015 */

ALTER TABLE `appointments` CHANGE COLUMN `appointment_description` `appointment_description` VARCHAR(1000) NOT NULL  ;

/* 12 March 2015 */

ALTER TABLE `messages` ADD COLUMN `modified_on` DATETIME NOT NULL  AFTER `is_read` ;

/* 09 April 2015 */

ALTER TABLE `appointments` ADD COLUMN `outcome_description` VARCHAR(2000) NULL  AFTER `created_by_id_fk` ;

/* 14 April 2015 */

ALTER TABLE `fieldsense`.`users` ADD COLUMN `full_name` VARCHAR(100) NOT NULL  AFTER `last_name` ;

UPDATE users SET full_name=Concat(first_name ,' ',last_name);

/* 05 June 2015 */

CREATE  TABLE `app_settings` (
  `app_type` INT(2) NOT NULL COMMENT '0- android\n1-ios' ,
  `app_version` INT(7) NOT NULL ,
  `app_store_url` VARCHAR(5000) NOT NULL
  PRIMARY KEY (`app_type`) );


INSERT INTO `app_settings` (`app_type`, `app_version`) VALUES (0, 1);
INSERT INTO `app_settings` (`app_type`,`app_version`) VALUES (1, 1);

/* 19 June 2015 */

ALTER TABLE `fieldsense`.`app_settings` ADD COLUMN `app_store_url` VARCHAR(5000) NOT NULL  AFTER `app_version` , CHANGE COLUMN `app_version` `app_version` INT(7) NOT NULL  ;
