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
        System.out.print("Add player: "+name);

        PlayerDTO playerDTO = players.get(name);

        if(playerDTO==null) {
            playerDTO = new PlayerDTO();
            playerDTO.setName(name);

            players.put(name, playerDTO);

            System.out.println(" NEW");
        }
        else {
            System.out.println(" Exist");
        }
    }

    @Override
    public void updateName(String oldName, String name) throws PlayerDoesntExist, PlayerAlreadyExists {
        System.out.print("Update name old:"+oldName+" new:"+name);

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
                    System.out.println(" MERGED!");
                    players.get(name).addKills(playerDTO.getKills());
                }
                else {
                    System.out.println(" NO CHANGES");
                }
                //else it will just keep the existent user without changes
            }
            else {
                //update the map with the new key
                players.put(name, playerDTO);
                System.out.println(" UPDATED!");
            }

        }
        else {
            System.out.println(" **SAME NAME **");
        }

    }

    @Override
    public void addPlayerKillScore(String name, int value) throws PlayerDoesntExist {
        System.out.println(" addPlayerKillScore: "+name);
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
