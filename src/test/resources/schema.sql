
CREATE SCHEMA IF NOT EXISTS `chat`;
USE `chat` ;



CREATE TABLE IF NOT EXISTS `user` (
    iduser INT PRIMARY KEY  auto_increment ,
    name VARCHAR(45) NULL,
    lastName VARCHAR(45) NULL,
    mail VARCHAR(45) NULL UNIQUE ,
    nickName VARCHAR(45) NULL,
    birthdate DATE
);

CREATE TABLE IF NOT EXISTS  `group` (
  idgroup INT PRIMARY KEY  auto_increment,
  name VARCHAR(45) NULL,
 creation DATE NULL,
  admin VARCHAR(45) NULL,
 );




-- -----------------------------------------------------
-- Table `chat`.`user`
-- -----------------------------------------------------
--CREATE TABLE IF NOT EXISTS user (
  --iduser INT(11) NOT NULL AUTO_INCREMENT,
 -- name VARCHAR(45) NOT NULL,
  --lastName VARCHAR(45) NOT NULL,
 -- mail VARCHAR(45) NOT NULL,
 -- nickname TEXT NOT NULL,
 -- birthdate DATE NOT NULL,
  --PRIMARY KEY (iduser),
 -- UNIQUE INDEX `maill_UNIQUE` (`mail` ASC));


-- -----------------------------------------------------
-- Table `chat`.`group`
-- -----------------------------------------------------
--CREATE TABLE IF NOT EXISTS group (
--  idgroup INT NOT NULL,
 --name VARCHAR(45) NOT NULL,
--  creation DATE NOT NULL,
--PRIMARY KEY (idgroup));


--CREATE TABLE IF NOT EXISTS `Person` (
  --  `id`         INTEGER  PRIMARY KEY AUTO_INCREMENT,
   --  `first_name` VARCHAR(50) NOT NULL,
   --  `age`        INTEGER  NOT NULL
--);





/*
CREATE TABLE IF NOT EXISTS `group` (
  `idgroup` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
 `creation` DATE NULL,
 `admin` VARCHAR(45) NULL,
  PRIMARY KEY (`idgroup`));
*/



  CREATE TABLE IF NOT EXISTS `message` (
    `idmessage` INT NOT NULL AUTO_INCREMENT,
    `content` VARCHAR(255) NULL,
    `creation` DATE NULL,
    `idgroup_message` INT NOT NULL,
    `sender` VARCHAR(255) NULL,
    PRIMARY KEY (`idmessage`),
    CONSTRAINT `fk_idgroup_message`
      FOREIGN KEY (`idgroup_message`)
      REFERENCES `group` (`idgroup`)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);
--  INDEX `fk_idgroup_idx` (`idgroup_message` ASC),
CREATE INDEX IF NOT EXISTS `fk_idgroup_idx` on message(`idgroup_message` ASC);




  -- -----------------------------------------------------
  -- Table `chat`.`user_group`
  -- -----------------------------------------------------
  CREATE TABLE IF NOT EXISTS `user_group` (
    `iduser_group` INT NOT NULL AUTO_INCREMENT,
    `idgroup_user_group` INT NOT NULL,
    `iduser_user_group` INT NOT NULL,
    PRIMARY KEY (`iduser_group`),
    CONSTRAINT `fk_idgroup_user_group`
      FOREIGN KEY (`idgroup_user_group`)
      REFERENCES `group` (`idgroup`)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
    CONSTRAINT `fk_iduser_user_group`
      FOREIGN KEY (`iduser_user_group`)
      REFERENCES `user` (`iduser`)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);


    --      INDEX `fk_idgroup_idx` (`idgroup_user_group` ASC),
    --    INDEX `fk_iduser_idx` (`iduser_user_group` ASC),
create INDEX IF NOT EXISTS `fk_idgroupug_idx`  on user_group(`idgroup_user_group` ASC);
create INDEX IF NOT EXISTS `fk_iduser_idx` on user_group(`iduser_user_group` ASC);










