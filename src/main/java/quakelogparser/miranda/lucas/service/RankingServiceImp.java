package quakelogparser.miranda.lucas.service;

import quakelogparser.miranda.lucas.dto.PlayerDTO;
import quakelogparser.miranda.lucas.exception.PlayerAlreadyExists;
import quakelogparser.miranda.lucas.exception.PlayerDoesntExist;
import quakelogparser.miranda.lucas.helpers.ReportComparatorProvider;

import java.util.*;

public class RankingServiceImp implements RankingService{
    private Map<String, PlayerDTO> players;

    public RankingServiceImp() {
        players = new HashMap<>();
    }

    @Override
    public void addPlayerIfDoenstExists(String name) {

        PlayerDTO playerDTO = players.get(name);

        if(playerDTO==null) {
            playerDTO = new PlayerDTO();
            playerDTO.setName(name);

            players.put(name, new PlayerDTO());
        }
    }

    @Override
    public void updateName(String oldName, String name) throws PlayerDoesntExist, PlayerAlreadyExists {
        if(oldName.compareTo(name)!=0) {

            PlayerDTO playerDTO = players.get(oldName);
            if (playerDTO == null) {
                throw new PlayerDoesntExist(oldName);
            }

            //if ranking already have an user with with this name
            if (players.containsKey(name)) {
                throw new PlayerAlreadyExists(name);
            }

            playerDTO.setName(name);

            //update the map with the new key
            players.remove(oldName);
            players.put(name, playerDTO);
        }

    }

    @Override
    public void addPlayerKillScore(String name, int value) throws PlayerDoesntExist {
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
