package quakelogparser.miranda.lucas.service;

import quakelogparser.miranda.lucas.constants.GameConstantValues;
import quakelogparser.miranda.lucas.dto.*;
import quakelogparser.miranda.lucas.exception.NoGameInitialized;
import quakelogparser.miranda.lucas.exception.PlayerAlreadyExists;
import quakelogparser.miranda.lucas.exception.PlayerDoesntExist;
import quakelogparser.miranda.lucas.exception.PlayerIsNotInTheGame;
import quakelogparser.miranda.lucas.helpers.GameState;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GameServiceImp implements GameService {

    private PlayerHistoryService rankingService;
    private List<GameState> matches;
    private GameState currentMatch;



    public GameServiceImp(){
        matches = new ArrayList<>();

        rankingService = new PlayerHistoryServiceImp();

    }

    @Override
    public void initGame() {

        currentMatch = new GameState();
        matches.add(currentMatch);
        currentMatch.setId(matches.size());
    }

    @Override
    public void shutDownGame() {
        validateMatch();
        currentMatch.setShutdown(true);
        currentMatch = null;
    }


    @Override
    public void playerConnect(int id) throws PlayerAlreadyExists {
        validateMatch();

        PlayerConnectionDTO playerConnectionDTO = currentMatch.getPlayerById(id);
        if (playerConnectionDTO != null) {

            if(playerConnectionDTO.isConnected()) {
                throw new PlayerAlreadyExists(id);
            }
            else {
                playerConnectionDTO.setConnected(true);
            }
        }
        else {
            currentMatch.connectPlayer(id);
        }
    }



    @Override
    public void playerUpdate(int id, String name) throws PlayerDoesntExist, PlayerAlreadyExists {
        validateMatch();
        PlayerConnectionDTO playerConnectionDTO = currentMatch.getPlayerById(id);
        if(playerConnectionDTO == null) {
            throw new PlayerDoesntExist(id);
        }



        String oldName = playerConnectionDTO.getName();
        //if the old name is null so add the player to the ranking
        if(oldName==null || oldName.isEmpty()) {
            currentMatch.addPlayerIfDoenstExists(name);
            rankingService.addPlayerIfDoenstExists(name);
        }
        else {
            currentMatch.updateName(oldName, name);
            rankingService.updateName(oldName, name);
        }


        playerConnectionDTO.setName(name);
        playerConnectionDTO.setNameWhenConnected(name);
    }

    @Override
    public void playerDisconnect(int id) throws PlayerDoesntExist {
        validateMatch();

        PlayerConnectionDTO playerConnectionDTO = currentMatch.getPlayerById(id);
        if(playerConnectionDTO == null) {
            throw new PlayerDoesntExist(id);
        }
        playerConnectionDTO.setConnected(false);
        playerConnectionDTO.setBegin(false);
        playerConnectionDTO.setName(null); //IMPORTANT! Clear the name to keep the ranking correct



    }

    @Override
    public void playerBegin(int id) throws PlayerDoesntExist {
        validateMatch();

        PlayerConnectionDTO playerConnectionDTO = currentMatch.getPlayerById(id);
        if(playerConnectionDTO == null) {
            throw new PlayerDoesntExist(id);
        }
        playerConnectionDTO.setBegin(true);
    }

    @Override
    public void playerKill(int idKiller, int idVictim, int meansOfDeath) throws PlayerDoesntExist, PlayerIsNotInTheGame {
        validateMatch();

        //When <world> kill a player, that player loses -1 kill score.
        //Since <world> is not a player, it should not appear in the list of players or in the dictionary of kills.
        //The counter total_kills includes player and world deaths.

        PlayerConnectionDTO playerVictimDTO = getAndValidatePlayerInTheGame(idVictim);

        if(idKiller == GameConstantValues.WORLD_KILLER_ID) {
            playerVictimDTO.addKills(GameConstantValues.SCORE_SUICIDE);
            currentMatch.addKill(meansOfDeath);
            currentMatch.addPlayerKillScore(playerVictimDTO.getName(), GameConstantValues.SCORE_SUICIDE);
            rankingService.addPlayerKillScore(playerVictimDTO.getName(), GameConstantValues.SCORE_SUICIDE);
        }
        else {
            PlayerConnectionDTO playerKillerDTO = getAndValidatePlayerInTheGame(idKiller);
            playerKillerDTO.addKills(GameConstantValues.SCORE_NORMAL_KILL);
            currentMatch.addKill(meansOfDeath);

            currentMatch.addPlayerKillScore(playerKillerDTO.getName(), GameConstantValues.SCORE_NORMAL_KILL);
            rankingService.addPlayerKillScore(playerKillerDTO.getName(), GameConstantValues.SCORE_NORMAL_KILL);
        }

    }

    private PlayerConnectionDTO getAndValidatePlayerInTheGame(int id) throws PlayerDoesntExist, PlayerIsNotInTheGame {
        PlayerConnectionDTO playerConnectionDTO = currentMatch.getPlayerById(id);
        if(playerConnectionDTO == null) {
            throw new PlayerDoesntExist(id);
        }
        if(!playerConnectionDTO.isConnected() || !playerConnectionDTO.isBegin()) {
            throw new PlayerIsNotInTheGame(id, playerConnectionDTO.isConnected() , playerConnectionDTO.isBegin());
        }
        return playerConnectionDTO;
    }

    @Override
    public GameState getCurrentGameData() {
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

        for(GameState gameState : matches) {

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

            reportGameDTO.setTotal_kills(gameState.getTotalKills());
            reportGameDTO.setPlayers(gameState.getPlayersLastNames());
            reportGameDTO.setKills(gameState.getPlayersNamesWithKillsSorted());

            reports.put(GameConstantValues.REPORT_MATCH_PREFIX+ gameState.getId(), reportGameDTO);
        }
        return reports;
    }

    @Override
    public Map<String, ReportKillsByMeansDTO> generateKillByMeansReport() {
        Map<String, ReportKillsByMeansDTO> reports = new LinkedHashMap();

        for(GameState gameState : matches) {
//        "game-1": {
//            "kills_by_means": {
//                "MOD_SHOTGUN": 10,
//                "MOD_RAILGUN": 2,
//                "MOD_GAUNTLET": 1,
//    ...
//            }
//        }
            ReportKillsByMeansDTO reportKillsByMeansDTO = new ReportKillsByMeansDTO();
            reportKillsByMeansDTO.setKills_by_means(gameState.getKillByMeansWithName());

            reports.put(GameConstantValues.REPORT_MATCH_PREFIX+ gameState.getId(), reportKillsByMeansDTO);
        }

        return reports;
    }

    @Override
    public PlayerDTO getPlayerById(int id) {
        validateMatch();
        return currentMatch.getPlayerById(id);
    }

    @Override
    public ReportRankingDTO generateRanking() {
        ReportRankingDTO reportRankingDTO = new ReportRankingDTO();
        List<PlayerDTO> players = rankingService.getListOfPlayersOrderByKills();

        int i = 1;
        for(PlayerDTO playerDTO : players) {
            reportRankingDTO.getRanking().put(i, playerDTO);
            i++;
        }

        return reportRankingDTO;
    }
}
