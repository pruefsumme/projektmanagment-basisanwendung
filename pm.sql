-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Host: mariadb:3306
-- Erstellungszeit: 06. Nov 2025 um 17:01
-- Server-Version: 11.8.2-MariaDB-ubu2404
-- PHP-Version: 8.2.29

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `pm`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Haus`
--

CREATE TABLE `Haus` (
  `haus_id` int(11) NOT NULL,
  `hat_dg` tinyint(1) NOT NULL,
  `haustyp_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `HausBild`
--

CREATE TABLE `HausBild` (
  `bild_id` int(11) NOT NULL,
  `hat_dg` tinyint(1) NOT NULL,
  `dateipfad` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Haustyp`
--

CREATE TABLE `Haustyp` (
  `haustyp_id` int(11) NOT NULL,
  `bezeichnung` varchar(100) DEFAULT NULL,
  `hat_dg` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Kunde`
--

CREATE TABLE `Kunde` (
  `kundennr` int(11) NOT NULL,
  `haus_id` int(11) NOT NULL,
  `vorname` varchar(50) NOT NULL,
  `nachname` varchar(50) NOT NULL,
  `telefon` varchar(30) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Kunde_Sonderwunsch`
--

CREATE TABLE `Kunde_Sonderwunsch` (
  `kundennr` int(11) NOT NULL,
  `sw_id` int(11) NOT NULL,
  `anzahl` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Sonderwunsch`
--

CREATE TABLE `Sonderwunsch` (
  `sw_id` int(11) NOT NULL,
  `kategorie_id` int(11) NOT NULL,
  `code` varchar(10) DEFAULT NULL,
  `bezeichnung` varchar(255) NOT NULL,
  `preis` decimal(10,2) NOT NULL,
  `einheit` varchar(50) DEFAULT NULL,
  `nur_mit_dg` tinyint(1) DEFAULT NULL,
  `nur_ohne_dg` tinyint(1) DEFAULT NULL,
  `aktiv` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `SonderwunschKategorie`
--

CREATE TABLE `SonderwunschKategorie` (
  `kategorie_id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `beschreibung` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `Haus`
--
ALTER TABLE `Haus`
  ADD PRIMARY KEY (`haus_id`),
  ADD KEY `fk_haus_haustyp` (`haustyp_id`);

--
-- Indizes für die Tabelle `HausBild`
--
ALTER TABLE `HausBild`
  ADD PRIMARY KEY (`bild_id`);

--
-- Indizes für die Tabelle `Haustyp`
--
ALTER TABLE `Haustyp`
  ADD PRIMARY KEY (`haustyp_id`);

--
-- Indizes für die Tabelle `Kunde`
--
ALTER TABLE `Kunde`
  ADD PRIMARY KEY (`kundennr`),
  ADD UNIQUE KEY `haus_id` (`haus_id`);

--
-- Indizes für die Tabelle `Kunde_Sonderwunsch`
--
ALTER TABLE `Kunde_Sonderwunsch`
  ADD PRIMARY KEY (`kundennr`,`sw_id`),
  ADD KEY `fk_ks_sw` (`sw_id`);

--
-- Indizes für die Tabelle `Sonderwunsch`
--
ALTER TABLE `Sonderwunsch`
  ADD PRIMARY KEY (`sw_id`),
  ADD KEY `fk_sw_kategorie` (`kategorie_id`);

--
-- Indizes für die Tabelle `SonderwunschKategorie`
--
ALTER TABLE `SonderwunschKategorie`
  ADD PRIMARY KEY (`kategorie_id`);

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `Haus`
--
ALTER TABLE `Haus`
  ADD CONSTRAINT `fk_haus_haustyp` FOREIGN KEY (`haustyp_id`) REFERENCES `Haustyp` (`haustyp_id`);

--
-- Constraints der Tabelle `Kunde`
--
ALTER TABLE `Kunde`
  ADD CONSTRAINT `fk_kunde_haus` FOREIGN KEY (`haus_id`) REFERENCES `Haus` (`haus_id`);

--
-- Constraints der Tabelle `Kunde_Sonderwunsch`
--
ALTER TABLE `Kunde_Sonderwunsch`
  ADD CONSTRAINT `fk_ks_kunde` FOREIGN KEY (`kundennr`) REFERENCES `Kunde` (`kundennr`),
  ADD CONSTRAINT `fk_ks_sw` FOREIGN KEY (`sw_id`) REFERENCES `Sonderwunsch` (`sw_id`);

--
-- Constraints der Tabelle `Sonderwunsch`
--
ALTER TABLE `Sonderwunsch`
  ADD CONSTRAINT `fk_sw_kategorie` FOREIGN KEY (`kategorie_id`) REFERENCES `SonderwunschKategorie` (`kategorie_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
