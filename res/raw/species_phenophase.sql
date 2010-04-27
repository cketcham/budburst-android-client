DROP TABLE IF EXISTS `species_phenophase`;
CREATE TABLE IF NOT EXISTS `species_phenophase` (
  `protocol_id` integer,
  `phenophase_id` integer,
  `icon_id` integer,
  `type` text NOT NULL,
  `description` text NOT NULL
);

INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(2, 12, 3, 'Leaves', 'Report when 95-100% of the leaves that developed this season, have lost green color or are dried and dead. ');
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(2, 3, 4, 'Flowers', 'Report the date at which the first flowers are completely open. ');
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(2, 5, 6, 'Flowers', 'Report the date when the last flower has withered, dried up, or died. ');
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(2, 6, 7, 'Fruits', 'Report the date when you notice the first fruits becoming fully ripe or seeds dropping naturally from the plant. ');
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(3, 12, 3, 'Leaves', 'Report when 95-100% of the leaves that developed this season, have lost green color or are dried and dead. ');
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(3, 14, 12, 'Flowers', 'Report when the first flower stalk is emerging from the stem of the grass and you can see the first flower cluster (spikelet) rising above the leaves. ');
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(3, 8, 13, 'Flowers', 'Report the date when the plant starts releasing the powdery, yellow pollen from their flowers. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(3, 10, 14, 'Flowers', 'Report the date when there are no longer any flowers with pollen. ');
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(3, 6, 15, 'Fruits', 'Report the date when you notice the first stalks have fruits becoming fully ripe or seeds dropping naturally from the plant. ');
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(4, 1, 18, 'Leaves', 'Report the date at which there are at least three places on the tree or shrub where budburst has occurred. ');
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(4, 2, 19, 'Leaves', 'Report the date when at least 95% of the leaves have unfolded. ');
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(4, 11, 20, 'Leaves', 'Report the date when 50% of the leaves have started to change color. ');
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(4, 13, 21, 'Leaves', 'Report the date when 50% of the leaves have fallen off the tree or shrub. ');
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(4, 3, 23, 'Flowers', 'Report the date at which the first flowers are completely open on at least three places on the tree or shrub. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(4, 4, 24, 'Flowers', 'Report the date when 50% of the flowers are fully opened. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(4, 5, 25, 'Flowers', 'Report the date when the last flower has withered, dried up, or died. ');
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(5, 8, 26, 'Flowers', 'Report the date when the plant starts releasing the powdery, yellow pollen from their cones or catkins. ');
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(5, 9, 27, 'Flowers', 'Report the date when 50% of the flowers have pollen. ');
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(5, 10, 28, 'Flowers', 'Report the date when there are no longer any flowers with pollen. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(4, 6, 7, 'Fruits', 'Report the date when you notice when 3 or more branches with ripe or dispersing fruit. ');
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(6, 1, 30, 'Leaves', 'Report the date at which there are at least three places on the tree or shrub where budburst has occurred. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(6, 3, 32, 'Flowers', 'Report the date at which the first flowers are completely open on at least three places on the tree or shrub. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(6, 4, 33, 'Flowers', 'Report the date when 50% of the flowers are fully opened. ');
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(6, 5, 34, 'Flowers', 'Report the date when the last flower has withered, dried up, or died. ');
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(7, 8, 26, 'Flowers', 'Report the date when the plant starts releasing the powdery, yellow pollen from their cones or catkins. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(7, 9, 27, 'Flowers', 'Report the date when 50% of the flowers have pollen. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(7, 10, 28, 'Flowers', 'Report the date when there are no longer any flowers with pollen. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(6, 6, 7, 'Fruits', 'Report the date when you notice when 3 or more branches with ripe or dispersing fruit. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(8, 7, 30, 'Leaves', 'Report when new needles emerge from tips of buds, or are visible from the side of the buds. ');
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(8, 8, 35, 'Flowers', 'Report the date when the plant starts releasing the powdery, yellow pollen from their cones. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(8, 9, 36, 'Flowers', 'Report the date when at least 50% of the branches on the plant with flowers have pollen. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(8, 6, 38, 'Fruits', 'Report the date when you notice when the cones turn brown and the scales expand. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(1, 1, 18, 'Leaves', 'Report the date at which the first leaves are completely unfolded from the bud. ');
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(1, 2, 19, 'Leaves', 'Report the date when at least 95% of the leaves have unfolded. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(1, 11, 20, 'Leaves', 'Report the date when 50% of the leaves have started to change color. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(1, 12, 3, 'Leaves', 'Report when 95-100% of the leaves that developed this season, have lost green color or are dried and dead. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(1, 13, 21, 'Leaves', 'Report the date when 50% of the leaves have fallen off the tree or shrub. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(1, 3, 4, 'Flowers', 'Report the date at which the first flowers are completely open. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(1, 4, 5, 'Flowers', 'Report the date when 50% of the flowers are fully opened. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(1, 5, 6, 'Flowers', 'Report the date when the last flower has withered, dried up, or died. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(1, 8, 26, 'Flowers', 'Report the date when the plant starts releasing the powdery, yellow pollen from their cones or catkins. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(1, 9, 27, 'Flowers', 'Report the date when 50% of the flowers have pollen. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(1, 10, 28, 'Flowers', 'Report the date when there are no longer any flowers with pollen. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(1, 6, 7, 'Fruits', 'Report the date when you notice the first fruits becoming fully ripe or seeds dropping naturally from the plant. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(1, 16, 8, 'Fruits', 'Report the date when you notice the most fruits have become fully ripe or the most seeds are dropping naturally from the plant. '); 
INSERT INTO `species_phenophase` (`protocol_id`, `phenophase_id`, `icon_id`, `type`, `description`) VALUES
(1, 17, 9, 'Fruits', 'Report the date when you notice the last of the fruits have dropped naturally from the plant. ');
