package quakelogparser.miranda.lucas.service;

import quakelogparser.miranda.lucas.dto.GameDTO;
import quakelogparser.miranda.lucas.dto.ReportGameDTO;
import quakelogparser.miranda.lucas.dto.PlayerDTO;
import quakelogparser.miranda.lucas.dto.ReportKillsByMeansDTO;
import quakelogparser.miranda.lucas.exception.NoGameInitialized;
import quakelogparser.miranda.lucas.exception.PlayerAlreadyExists;
import quakelogparser.miranda.lucas.exception.PlayerDoesntExist;
import quakelogparser.miranda.lucas.exception.PlayerIsNotInTheGame;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GameServiceImp implements GameService {
    private List<GameDTO> matches;
    private GameDTO currentMatch;

    public static final int WORLD_KILLER_ID = 1022;

    private static final int SCORE_SUICIDE = -1;
    private static final int SCORE_NORMAL_KILL = 1;
    private static final String REPORT_MATCH_PREFIX = "game_";

    public GameServiceImp(){
        matches = new ArrayList<>();

    }

    @Override
    public void initGame() {
        if(currentMatch!=null) {
            archiveCurrentGame();
        }
        currentMatch = new GameDTO();
        currentMatch.setId(matches.size()+1);
    }

    @Override
    public void shutDownGame() {
        validateMatch();

        if(currentMatch!=null) {
            archiveCurrentGame();
        }
        else {
            throw new RuntimeException();
        }
        currentMatch = null;
    }

    private void archiveCurrentGame() {
        if(currentMatch!=null) {
            matches.add(currentMatch);
        }
    }

    @Override
    public void playerConnect(int id) throws PlayerAlreadyExists {
        validateMatch();

        PlayerDTO playerDTO = currentMatch.getPlayerById(id);
        if (playerDTO != null) {

            if(playerDTO.isConnected()) {
                throw new PlayerAlreadyExists(id);
            }
            else {
                playerDTO.setConnected(true);
            }
        }
        else {
            currentMatch.addNewPlayer(id);
        }

    }

    @Override
    public void playerUpdate(int id, String name) throws PlayerDoesntExist {
        validateMatch();
        PlayerDTO playerDTO = currentMatch.getPlayerById(id);
        if(playerDTO == null) {
            throw new PlayerDoesntExist(id);
        }
        playerDTO.setName(name);
    }

    @Override
    public void playerDisconnect(int id) throws PlayerDoesntExist {
        validateMatch();

        PlayerDTO playerDTO = currentMatch.getPlayerById(id);
        if(playerDTO == null) {
            throw new PlayerDoesntExist(id);
        }
        playerDTO.setConnected(false);
        playerDTO.setBegin(false);
    }

    @Override
    public void playerBegin(int id) throws PlayerDoesntExist {
        validateMatch();

        PlayerDTO playerDTO = currentMatch.getPlayerById(id);
        if(playerDTO == null) {
            throw new PlayerDoesntExist(id);
        }
        playerDTO.setBegin(true);
    }

    @Override
    public void playerKill(int idKiller, int idVictim, int meansOfDeath) throws PlayerDoesntExist, PlayerIsNotInTheGame {
        validateMatch();


        //When <world> kill a player, that player loses -1 kill score.
        //Since <world> is not a player, it should not appear in the list of players or in the dictionary of kills.
        //The counter total_kills includes player and world deaths.

        PlayerDTO playerVictimDTO = getAndValidatePlayerInTheGame(idVictim);

        if(idKiller==WORLD_KILLER_ID) {
            playerVictimDTO.addKills(SCORE_SUICIDE);
            currentMatch.addKill(meansOfDeath);
        }
        else {
            PlayerDTO playerKillerDTO = getAndValidatePlayerInTheGame(idKiller);
            playerKillerDTO.addKills(SCORE_NORMAL_KILL);
            currentMatch.addKill(meansOfDeath);
        }
    }

    private PlayerDTO getAndValidatePlayerInTheGame(int id) throws PlayerDoesntExist, PlayerIsNotInTheGame{
        PlayerDTO playerDTO = currentMatch.getPlayerById(id);
        if(playerDTO == null) {
            throw new PlayerDoesntExist(id);
        }
        if(!playerDTO.isConnected() || !playerDTO.isBegin()) {
            throw new PlayerIsNotInTheGame(id);
        }
        return playerDTO;
    }

    @Override
    public GameDTO getCurrentGameData() {
        return currentMatch;
    }

    private void validateMatch() {
        if(currentMatch==null) {
            throw new NoGameInitialized();
        }
    }


    @Override
    public Map<String, ReportGameDTO> generateMatchesReport() {
        Map<String, ReportGameDTO> reports = new LinkedHashMap();

        for(GameDTO gameDTO : matches) {

//            "game_1": {
//                "total_kills": 45,
//                "players": ["Dono da bola", "Isgalamido", "Zeh"],
//                "kills": {
//                    "Dono da bola": 5,
//                     "Isgalamido": 18,
//                     "Zeh": 20
//                }
//            }


            ReportGameDTO reportGameDTO = new ReportGameDTO();

            reportGameDTO.setTotal_kills(gameDTO.getTotalKills());
            reportGameDTO.setPlayers(gameDTO.getPlayersNames());
            reportGameDTO.setKills(gameDTO.getPlayersNamesWithKillsSorted());

            reports.put(REPORT_MATCH_PREFIX+gameDTO.getId(), reportGameDTO);
        }
        return reports;
    }

    @Override
    public Map<String, ReportKillsByMeansDTO> generateKillByMeansReport() {
        Map<String, ReportKillsByMeansDTO> reports = new LinkedHashMap();

        for(GameDTO gameDTO : matches) {
//        "game-1": {
//            "kills_by_means": {
//                "MOD_SHOTGUN": 10,
//                "MOD_RAILGUN": 2,
//                "MOD_GAUNTLET": 1,
//    ...
//            }
//        }
            ReportKillsByMeansDTO reportKillsByMeansDTO = new ReportKillsByMeansDTO();
            reportKillsByMeansDTO.setKills_by_means(gameDTO.getKillByMeansWithName());

            reports.put(REPORT_MATCH_PREFIX+gameDTO.getId(), reportKillsByMeansDTO);
        }

        return reports;
    }
}
