# QuakeLogParser
Author: Lucas Moreira Carneiro de Miranda

This project aims to process log files from a Quake 3 Arena server and extract information from this log file, generating 3 types of reports:
- Report with general match data
- Report grouping the causes of players' deaths in a match
- Global ranking of players

The project was developed with a focus on the following principles:
- Extracting data accurately
- Simplicity and modularity.
- Test-driven development


## Execution
1. **Download the JAR file**: Use [- this link - https://github.com/luc4smoreira/QuakeLogParser/releases/latest](https://github.com/luc4smoreira/QuakeLogParser/releases/latest) - to download the latest version of the JAR file.
   
2. **Install the Java Runtime Environment (JRE)**: If Java is not already installed, download and install it from the [official Oracle site - https://www.java.com/pt-BR/download/](https://www.java.com/pt-BR/download/).
   
3. **Open a Terminal in the JAR File Directory**: Use the terminal of your preference and navigate to the directory where the JAR file was downloaded.
   
4. **Execute the Command**: Enter the command to execute the application with default parameters:
> java -jar QuakeLogParser.jar

5. **Optional Parameters**: To view possible parameters and get additional help, use the command:
> java -jar QuakeLogParser.jar -help

## Structure
Logs are processed line by line, grouping them by the same time in a single match. If an error occurs, it is handled in a way that processing continues if possible.

### Batch Processing
As logs are processed grouped by time, a priority was defined for each log type within the same time, so they are not processed in the order of the sequential lines. This was done considering that a late line registration in the log might occur after an event. The enum LogEventTypeEnum defines the log types and their priorities, with lower number priorities being executed first.

### Ignored Data
Some log types are ignored; they are: Item, Exit. In the Kill event, the numbers are considered, and all descriptive text of the Kill is ignored. In the log line below, for example:

> 0:05 Kill: 6 7 3: Zeh killed Mal by MOD_MACHINEGUN

The program will only consider:

> 0:05 Kill: 6 7 3:

## Means of Death
An Enum was created for Means of Death, associating an id for each type. As the reference code indicates that there are "meansOfDeath" exclusive only if "mission pack" is enabled, and this affects the number associated with MOD_GRAPPLE. After analyzing the log, it was observed that no "mission pack" meansOfDeath were recorded, nor even MOD_GRAPPLE, so the means of death with id greater than 22 were not included in the enum.

## List of Decisions Made

### 1. Corrupted Log

There is a portion of the log file that seems to be corrupted, on line 97 where there should be a time, the number 26 followed by the time 00:00 is found.

  > 26 0:00 ------------------------------------------------------------

Considering this case, failure handling was added and a warning message can be enabled using the -warnings parameter.

### 2. Player Disconnects

It was not clear if the player who disconnects from the match before it ends needs to be considered in the report.

**I made the decision to consider all players, even those who disconnected**

### 3. Player Reconnection

A player may disconnect from the match and a new player may reconnect with the same id. It was not clear if this new connection should be considered a new player, and the previous "Player Disconnects" question enters.

**I considered that the player who disconnects and re-enters the match using the same name is the same player; the kill count will continue**

### 4. Player Ranking

In Report, it was not clear if the ranking of players would be global for all matches or for each match.

  >3.2. Report
  >
  >Create a script that prints a report (grouped information) for each match and a player ranking.

**A global ranking of all matches was created, assuming the player's name used as the identifier**

### 5. Execution and Output

It was not clear how the program should be executed and the output generated.
**It was defined that the program will be executed by the console and will generate the output also in the console**

#### Observation:
Within what was specified and observing the log, if the player kills themselves during the match, it counts as a kill, and they will be better ranked by killing themselves.
(not considering the case where WORLD is the killer)
