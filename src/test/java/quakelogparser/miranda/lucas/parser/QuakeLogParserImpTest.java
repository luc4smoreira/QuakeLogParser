package quakelogparser.miranda.lucas.parser;

import org.junit.jupiter.api.Test;
import quakelogparser.miranda.lucas.constants.LogEventTypeEnum;
import quakelogparser.miranda.lucas.constants.MeansOfDeathEnum;
import quakelogparser.miranda.lucas.dto.PlayerDTO;
import quakelogparser.miranda.lucas.dto.ReportGameDTO;
import quakelogparser.miranda.lucas.dto.ReportKillsByMeansDTO;
import quakelogparser.miranda.lucas.dto.ReportRankingDTO;
import quakelogparser.miranda.lucas.events.ClientUserinfoChangedEvent;
import quakelogparser.miranda.lucas.events.KillEvent;
import quakelogparser.miranda.lucas.exception.CorruptedLogLine;
import quakelogparser.miranda.lucas.service.GameService;
import quakelogparser.miranda.lucas.service.GameServiceImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class QuakeLogParserImpTest {



    @Test
    public void testGetName() {
        QuakeLogParser quakeLogParser = new QuakeLogParserImp();

        String lineName = " 20:34 ClientUserinfoChanged: 2 n\\Isgalamido\\t\\0\\model\\xian/default\\hmodel\\xian/default\\g_redteam\\\\g_blueteam\\\\c1\\4\\c2\\5\\hc\\100\\w\\0\\l\\0\\tt\\0\\tl\\0";
        try {
            LogLine logLine = quakeLogParser.parseLine(lineName);
            assertEquals(LogEventTypeEnum.CLIENT_USERINFO_CHANGED, logLine.getType());
            assertInstanceOf(ClientUserinfoChangedEvent.class, logLine);
            ClientUserinfoChangedEvent clientUserinfoChangedEvent = (ClientUserinfoChangedEvent) logLine;
            assertEquals("Isgalamido", clientUserinfoChangedEvent.getName());
        }
        catch(CorruptedLogLine e) {
            throw new AssertionError("Should not generate an error", e);
        }
    }


    @Test
    public void testUserKilled() {
        QuakeLogParser quakeLogParser = new QuakeLogParserImp();

        String lineName = "  20:54 Kill: 1022 2 22: <world> killed Isgalamido by MOD_TRIGGER_HURT";
        try {
            LogLine logLine = quakeLogParser.parseLine(lineName);
            assertEquals(LogEventTypeEnum.KILL, logLine.getType());
            assertInstanceOf(KillEvent.class, logLine);
            KillEvent killEvent = (KillEvent) logLine;
            assertEquals(1022, killEvent.getKiller());
            assertEquals(2, killEvent.getVictim());
            assertEquals(22, killEvent.getMeansOfDeath());
        }
        catch(CorruptedLogLine e) {
            throw new AssertionError("Should not generate an error", e);
        }
    }

    @Test
    public void testParseFileTest1() {
        final String namePlayer2 = "Isgalamido";
        final String namePlayer3 = "Mocinha";

        QuakeLogParser quakeLogParser = new QuakeLogParserImp();
        GameService gameService = quakeLogParser.parseFile("test1.log");

        Map<String, ReportGameDTO> reportGame = gameService.generateMatchesReport();
        assertEquals(1, reportGame.size());

        ReportGameDTO reportGameDTO = reportGame.values().iterator().next();

        assertEquals(2, reportGameDTO.getPlayers().size());
        assertEquals(10, reportGameDTO.getTotal_kills());

        assertEquals(-4, reportGameDTO.getKills().get(namePlayer2));
        assertEquals(0, reportGameDTO.getKills().get(namePlayer3));


        assertEquals(namePlayer3, reportGameDTO.getPlayers().get(0));
        assertEquals(namePlayer2, reportGameDTO.getPlayers().get(1));




        Map<String, ReportKillsByMeansDTO> reportMeans = gameService.generateKillByMeansReport();
        assertEquals(1, reportMeans.size());
        ReportKillsByMeansDTO reportKillsByMeansDTO = reportMeans.values().iterator().next();

        assertEquals(6, reportKillsByMeansDTO.getKills_by_means().get(MeansOfDeathEnum.MOD_TRIGGER_HURT.name()));
        assertEquals(3, reportKillsByMeansDTO.getKills_by_means().get(MeansOfDeathEnum.MOD_ROCKET_SPLASH.name()));
        assertEquals(1, reportKillsByMeansDTO.getKills_by_means().get(MeansOfDeathEnum.MOD_FALLING.name()));


    }


    @Test
    public void testParseFileTest2() {
        final String namePlayer2 = "Isgalamido";
        final String namePlayer3 = "Mocinha";
        final String namePlayer4 = "Zeh";

        QuakeLogParser quakeLogParser = new QuakeLogParserImp();
        GameService gameService = quakeLogParser.parseFile("test2.log");

        Map<String, ReportGameDTO> reportGame = gameService.generateMatchesReport();
        assertEquals(1, reportGame.size());

        ReportGameDTO reportGameDTO = reportGame.values().iterator().next();

        assertEquals(3, reportGameDTO.getPlayers().size());
        assertEquals(11, reportGameDTO.getTotal_kills());

        assertEquals(2, reportGameDTO.getKills().get(namePlayer2));
        assertEquals(0, reportGameDTO.getKills().get(namePlayer3));
        assertEquals(3, reportGameDTO.getKills().get(namePlayer4));



    }



    @Test
    public void testParseFileTest3() {
        QuakeLogParser quakeLogParser = new QuakeLogParserImp();
        GameService gameService = quakeLogParser.parseFile("test3.log");

        Map<String, ReportGameDTO> reportGame = gameService.generateMatchesReport();
        assertEquals(1, reportGame.size());
        for(ReportGameDTO reportGameDTO : reportGame.values()) {
            assertEquals(7, reportGameDTO.getPlayers().size());
            assertEquals(60, reportGameDTO.getTotal_kills());
        }

    }

    @Test
    public void testParseFileTest4() {
        QuakeLogParser quakeLogParser = new QuakeLogParserImp();
        GameService gameService = quakeLogParser.parseFile("test4.log");

        Map<String, ReportGameDTO> reportGame = gameService.generateMatchesReport();
        assertEquals(2, reportGame.size());


    }

    @Test
    public void testParseFileTest5() {
        QuakeLogParser quakeLogParser = new QuakeLogParserImp();
        GameService gameService = quakeLogParser.parseFile("test5.log");


        Map<String, ReportGameDTO> reportWithGames = gameService.generateMatchesReport();
        assertEquals(1, reportWithGames.size());

        ReportGameDTO reportGameDTO = reportWithGames.values().iterator().next();

        ReportRankingDTO reportRankingDTO = gameService.generateRanking();

        assertEquals(3, reportGameDTO.getTotal_kills());
        assertEquals(1, reportGameDTO.getKills().get("Isgalamido"));
        assertEquals(-1, reportGameDTO.getKills().get("Mal"));
        assertEquals(1, reportGameDTO.getKills().get("Zeh"));

        assertEquals(reportGameDTO.getPlayers().size(), reportRankingDTO.getRanking().size());

        //players in matchreport
        for(PlayerDTO playerDTO : reportRankingDTO.getRanking().values()) {
            assertTrue(reportGameDTO.getPlayers().contains(playerDTO.getName()), String.format("$s is not present in game report", playerDTO.getName()));
        }

    }


    @Test
    public void testParseFileTest6() {
        QuakeLogParser quakeLogParser = new QuakeLogParserImp();
        GameService gameService = quakeLogParser.parseFile("test6.log");


        Map<String, ReportGameDTO> reportWithGames = gameService.generateMatchesReport();
        assertEquals(1, reportWithGames.size());

        ReportGameDTO reportGameDTO = reportWithGames.values().iterator().next();

        ReportRankingDTO reportRankingDTO = gameService.generateRanking();

        assertEquals(17, reportGameDTO.getTotal_kills());
        assertEquals(4, reportGameDTO.getKills().get("Zeh"));
        assertEquals(-1, reportGameDTO.getKills().get("Mal"));
        assertEquals(-10, reportGameDTO.getKills().get("Isgalamido"));
        assertEquals(0, reportGameDTO.getKills().get("Dono da Bola"));


        assertEquals(reportGameDTO.getPlayers().size(), reportRankingDTO.getRanking().size());

        //players in matchreport
        for(PlayerDTO playerDTO : reportRankingDTO.getRanking().values()) {
            assertTrue(reportGameDTO.getPlayers().contains(playerDTO.getName()), String.format("$s is not present in game report", playerDTO.getName()));
        }




    }
    @Test
    public void testBatch() {
        try {
            QuakeLogParser quakeLogParser = new QuakeLogParserImp();

            LogLine logLineConnectPlayer = quakeLogParser.parseLine(" 00:00 ClientConnect: 2");
            LogLine logLineInitGame = quakeLogParser.parseLine("00:00 InitGame: \\sv_floodProtect\\1\\sv_maxPing\\0\\sv_minPing\\0\\sv_maxRate\\10000\\sv_minRate\\0\\sv_hostname\\Code Miner Server\\g_gametype\\0\\sv_privateClients\\2\\sv_maxclients\\16\\sv_allowDownload\\0\\bot_minplayers\\0\\dmflags\\0\\fraglimit\\20\\timelimit\\15\\g_maxGameClients\\0\\capturelimit\\8\\version\\ioq3 1.36 linux-x86_64 Apr 12 2009\\protocol\\68\\mapname\\q3dm17\\gamename\\baseq3\\g_needpass\\0\n");
            List<LogLine> logsSameTime = new ArrayList<>();

            logsSameTime.add(logLineConnectPlayer);
            logsSameTime.add(logLineInitGame);

            GameService gameService = new GameServiceImp();

            quakeLogParser.processLogsSameTime(logsSameTime, gameService);

        } catch (CorruptedLogLine e) {
            throw new RuntimeException(e);
        }
    }
}