package quakelogparser.miranda.lucas.service;

import quakelogparser.miranda.lucas.dto.PlayerConnectionDTO;
import quakelogparser.miranda.lucas.dto.PlayerDTO;
import quakelogparser.miranda.lucas.exception.PlayerDoesntExist;

import java.util.Map;

public class RankingServiceImp implements RankingService{
    private Map<Integer, PlayerDTO> players;

    @Override
    public void addPlayer(int id) {
        if(!players.containsKey(id)) {
            players.put(id, new PlayerDTO());
        }
    }

    @Override
    public void updateName(int id, String name) throws PlayerDoesntExist {
        PlayerDTO playerDTO = players.get(id);
        if(playerDTO!=null) {
            playerDTO.setName(name);
        }
        else {
            throw new PlayerDoesntExist(id);
        }
    }

    @Override
    public void addPlayerKillScore(int id, int value) {

    }
}
