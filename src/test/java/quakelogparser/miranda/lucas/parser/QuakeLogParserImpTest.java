package quakelogparser.miranda.lucas.parser;

import org.junit.jupiter.api.Test;
import quakelogparser.miranda.lucas.constants.LogEventTypeEnum;
import quakelogparser.miranda.lucas.events.ClientUserinfoChangedEvent;
import quakelogparser.miranda.lucas.events.KillEvent;
import quakelogparser.miranda.lucas.exception.CorruptedLogLine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

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
}