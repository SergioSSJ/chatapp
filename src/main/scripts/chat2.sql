-- MySQL Script generated by MySQL Workbench
-- Tue Mar  6 10:32:07 2018
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema chat
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema chat
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `chat` DEFAULT CHARACTER SET utf8 ;
USE `chat` ;

-- -----------------------------------------------------
-- Table `chat`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chat`.`user` (
  `iduser` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `lastName` VARCHAR(45) NOT NULL,
  `mail` VARCHAR(45) NOT NULL,
  `nickname` TEXT NOT NULL,
  `birthdate` DATE NOT NULL,
  PRIMARY KEY (`iduser`),
  UNIQUE INDEX `maill_UNIQUE` (`mail` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 12
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `chat`.`group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chat`.`group` (
  `idgroup` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `creation` DATE NULL,
  PRIMARY KEY (`idgroup`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chat`.`message`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chat`.`message` (
  `idmessage` INT NOT NULL AUTO_INCREMENT,
  `content` TEXT(255) NULL,
  `creation` DATE NULL,
  `idgroup_message` INT NOT NULL,
  PRIMARY KEY (`idmessage`),
  INDEX `fk_idgroup_idx` (`idgroup_message` ASC),
  CONSTRAINT `fk_idgroup_message`
    FOREIGN KEY (`idgroup_message`)
    REFERENCES `chat`.`group` (`idgroup`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chat`.`user_group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chat`.`user_group` (
  `iduser_group` INT NOT NULL AUTO_INCREMENT,
  `idgroup_user_group` INT NOT NULL,
  `iduser_user_group` INT NOT NULL,
  PRIMARY KEY (`iduser_group`),
  INDEX `fk_idgroup_idx` (`idgroup_user_group` ASC),
  INDEX `fk_iduser_idx` (`iduser_user_group` ASC),
  CONSTRAINT `fk_idgroup_user_group`
    FOREIGN KEY (`idgroup_user_group`)
    REFERENCES `chat`.`group` (`idgroup`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_iduser_user_group`
    FOREIGN KEY (`iduser_user_group`)
    REFERENCES `chat`.`user` (`iduser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
