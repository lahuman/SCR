package kr.pe.lahuman.cmd;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

import kr.pe.lahuman.common.Constants;
import kr.pe.lahuman.runner.ConSave;
import kr.pe.lahuman.xml.XMLParser;

public class DailyCmdRun {

  static Logger log = Logger.getLogger(DailyCmdRun.class);

  static {
    String layout = "%d %-5p [%t] %-17c{2} (%13F:%L) %3x - %m%n";
    String logfilename = "DailyLog.log";
    String datePattern = ".yyyy-MM-dd ";

    PatternLayout patternlayout = new PatternLayout(layout);
    DailyRollingFileAppender appender = null;
    ConsoleAppender consoleAppender = null;
    try {
        appender = new DailyRollingFileAppender(patternlayout, logfilename,
                datePattern);
        consoleAppender = new ConsoleAppender(patternlayout);

    } catch (IOException e) {
        e.printStackTrace();
    }
    log.addAppender(appender);
    log.addAppender(consoleAppender);
    log.setLevel(Level.DEBUG);
}
  
  public static void main(String[] args) {

    final String inputpath = args[0];
    final String outputpath = args[1];
    // validate
    if ("".equals(inputpath.trim())) {
      return;
    }
    if ("".equals(inputpath.trim())) {
      return;
    }

    // makeData
    try {
      final XMLParser xp = new XMLParser(inputpath);
      final NodeList result = xp.getNodeList(Constants.SERVER);
      log.info("inputpath : " + inputpath);
      log.info("outputpath : " + outputpath);
      new Thread() {
        public void run() {
          try {
            for (int i[] = new int[1]; i[0] < result.getLength(); i[0]++) {
              Node node = result.item(i[0]);
              log.info("---------------------------------------");
              log.info("SERVER INFO  : NAME=>[" + xp.getString(node, Constants.NAME) + "] IP=>["
                  + xp.getString(node, Constants.IP) + "]");

              ConSave cs = new ConSave(xp.getString(node, Constants.NAME),
                  xp.getString(node, Constants.IP),
                  Integer.parseInt(xp.getString(node, Constants.PORT)),
                  xp.getString(node, Constants.ID), xp.getString(node, Constants.PW), outputpath);
              // 종료를 위해 exit 명령어 삽입
              List<String> command = xp.getList(node, Constants.COMMAND);
              command.add("exit");
              log.info("=> SAVE COMMAND START ");
              log.info("=> " + command);
              cs.saveCommand(command);
              log.info("=> SAVE COMMAND END ");
              cs.disconnect();
              log.info("---------------------------------------");
            }
          } catch (Exception e) {
            log.error(e.toString());
          }
        }
      }.start();

    } catch (Exception e) {
      log.error(e.toString());
    }
  }

}
