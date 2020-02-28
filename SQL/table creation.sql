CREATE TABLE IF NOT EXISTS `softwareengineering`.`PersonalInfo` (
  `idUser` INT NOT NULL,
  `forename` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(60) NOT NULL,
  `username` VARCHAR(45) NOT NULL,
  `password` NVARCHAR(20) NOT NULL,
  `DOB` DATE NOT NULL,
  `height` DECIMAL(10) NOT NULL,
  `currentWeight` DECIMAL(10) NULL,
  `gender` VARCHAR(1) NOT NULL,
  UNIQUE INDEX `idUser_UNIQUE` (`idUser` ASC) VISIBLE,
  PRIMARY KEY (`idUser`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE);  
CREATE TABLE IF NOT EXISTS `softwareengineering`.`GoalWeight` (
  `idGoalWeight` INT NOT NULL,
  `weightGoal` DECIMAL(10) NOT NULL,
  `dateSet` DATE NULL,
  `targetDate` DATE NOT NULL,
  `idUser` INT NOT NULL,
  PRIMARY KEY (`idGoalWeight`),
  UNIQUE INDEX `idGoalWeight_UNIQUE` (`idGoalWeight` ASC) VISIBLE,
    CONSTRAINT `idUserinGoalWeight`
    FOREIGN KEY (`idUser`)
    REFERENCES `softwareengineering`.`PersonalInfo` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
CREATE TABLE IF NOT EXISTS `softwareengineering`.`Exercise` (
  `idExerciseType` INT NOT NULL,
  `calsPerMinute` DECIMAL(10) NOT NULL,
  `exerciseName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idExerciseType`),
  UNIQUE INDEX `idExerciseType_UNIQUE` (`idExerciseType` ASC) VISIBLE);
CREATE TABLE IF NOT EXISTS `softwareengineering`.`ExerciseSession` (
  `idExerciseSession` INT NOT NULL,
  `exerciseDate` DATE NOT NULL,
  `durationMinutesSeconds` DECIMAL(10) NULL,
  `idExerciseType` INT NOT NULL,
  `caloriesBurned` INT NULL,
  `idUser` INT NOT NULL,
  PRIMARY KEY (`idExerciseSession`),
  UNIQUE INDEX `idExcerciseSession_UNIQUE` (`idExerciseSession` ASC) VISIBLE,
  CONSTRAINT `idExerciseTypeinExerciseSession`
    FOREIGN KEY (`idExerciseSession`)
    REFERENCES `softwareengineering`.`Exercise` (`idExerciseType`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idUserinExerciseSession`
    FOREIGN KEY (`idUser`)
    REFERENCES `softwareengineering`.`PersonalInfo` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
CREATE TABLE IF NOT EXISTS `softwareengineering`.`Foods` (
  `idFood` INT NOT NULL,
  `foodName` VARCHAR(45) NOT NULL,
  `amountOfCalories` INT NOT NULL,
  `portionSize` DECIMAL(10) NOT NULL DEFAULT 1,
  PRIMARY KEY (`idFood`),
  UNIQUE INDEX `idFood_UNIQUE` (`idFood` ASC) VISIBLE);
CREATE TABLE IF NOT EXISTS `softwareengineering`.`Meal` (
  `idMeal` INT NOT NULL,
  `idFood` INT NOT NULL,
  `quantity` DECIMAL(10) NOT NULL DEFAULT 1,
  `mealCategory` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idMeal`),
  UNIQUE INDEX `idMeal_UNIQUE` (`idMeal` ASC) VISIBLE,
  INDEX `idFood_idx` (`idFood` ASC) VISIBLE,
  CONSTRAINT `idFoodinMeal`
    FOREIGN KEY (`idFood`)
    REFERENCES `softwareengineering`.`Foods` (`idFood`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
CREATE TABLE IF NOT EXISTS `softwareengineering`.`Diet` (
  `idDiet` INT NOT NULL,
  `idUser` INT NOT NULL,
  `idMeal` INT NOT NULL,  
  `date` DATE NOT NULL,
  PRIMARY KEY (`idDiet`),
  INDEX `idMeal_idx` (`idMeal` ASC) VISIBLE,
  CONSTRAINT `idUserinDiet`
    FOREIGN KEY (`idUser`)
    REFERENCES `softwareengineering`.`PersonalInfo` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `idMealinDiet`
    FOREIGN KEY (`idMeal`)
    REFERENCES `softwareengineering`.`Meal` (`idMeal`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
CREATE TABLE IF NOT EXISTS `softwareengineering`.`WeightTracking` (
  `idUser` INT NOT NULL,
  `date` DATE NOT NULL,
  `weight` DECIMAL(10) NOT NULL,
  PRIMARY KEY (`idUser`, `date`),
  CONSTRAINT `idUserinWeightTracking`
    FOREIGN KEY (`idUser`)
    REFERENCES `softwareengineering`.`PersonalInfo` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
CREATE TABLE IF NOT EXISTS `softwareengineering`.`WeeklySummary` (
  `idUser` INT NOT NULL,
  `dateWC` DATE NOT NULL,
  `avgCalIntake` DECIMAL(10) NULL,
  `avgExerciseCal` DECIMAL(10) NULL,
  `latestWeight` DECIMAL(10) NULL,
  PRIMARY KEY (`idUser`, `dateWC`),
  CONSTRAINT `idUserinWeeklySummary`
    FOREIGN KEY (`idUser`)
    REFERENCES `softwareengineering`.`PersonalInfo` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
