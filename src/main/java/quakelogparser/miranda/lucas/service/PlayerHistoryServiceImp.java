package quakelogparser.miranda.lucas.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quakelogparser.miranda.lucas.dto.PlayerDTO;
import quakelogparser.miranda.lucas.exception.PlayerAlreadyExists;
import quakelogparser.miranda.lucas.exception.PlayerDoesntExist;
import quakelogparser.miranda.lucas.helpers.ReportComparatorProvider;

import java.util.*;

public class PlayerHistoryServiceImp implements PlayerHistoryService {
    private static final Logger logger = LogManager.getLogger(PlayerHistoryServiceImp.class);
    protected Map<String, PlayerDTO> players;

    public PlayerHistoryServiceImp() {
        players = new HashMap<>();
    }

    @Override
    public void addPlayerIfDoenstExists(String name) {

        PlayerDTO playerDTO = players.get(name);

        if(playerDTO==null) {
            playerDTO = new PlayerDTO();
            playerDTO.setName(name);

            players.put(name, playerDTO);

            logger.debug(String.format("Add player %s NEW", name));
        }
        else {
            logger.debug(String.format("Add player %s Exist", name));
        }
    }

    @Override
    public void updateName(String oldName, String name) throws PlayerDoesntExist, PlayerAlreadyExists {
        logger.debug(String.format("Update name old: %s new: %s", oldName, name));

        if(oldName.compareTo(name)!=0) {

            PlayerDTO playerDTO = players.get(oldName);
            if (playerDTO == null) {
                throw new PlayerDoesntExist(oldName);
            }

            playerDTO.setName(name);
            players.remove(oldName);


            //if ranking already have an user with with this name
            if (players.containsKey(name)) {

                //check if there was some score for the old user if so merge the kills
                if(playerDTO.getKills()!=0) {
                    //merge kills
                    logger.debug("  MERGED");
                    players.get(name).addKills(playerDTO.getKills());
                }
                else {
                    logger.debug(" NO CHANGES");
                }
                //else it will just keep the existent user without changes
            }
            else {
                //update the map with the new key
                players.put(name, playerDTO);
                logger.debug(" UPDATED!");
            }

        }
        else {
            logger.debug(" **SAME NAME **");
        }

    }

    @Override
    public void addPlayerKillScore(String name, int value) throws PlayerDoesntExist {
        logger.debug(String.format(" addPlayerKillScore: %s", name));

        PlayerDTO playerDTO = players.get(name);
        if(playerDTO == null) {
            throw new PlayerDoesntExist(name);
        }

        playerDTO.addKills(value);
    }


    @Override
    public List<PlayerDTO> getListOfPlayersOrderByKills() {

        List<PlayerDTO> playersOrderByKills = new ArrayList<>();

        for(PlayerDTO playerDTO : players.values()) {
            playersOrderByKills.add(playerDTO);
        }

        Collections.sort(playersOrderByKills, ReportComparatorProvider.getComparatorPlayerByKills());

        return playersOrderByKills;
    }
}
