package kr.pe.lahuman.test;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import kr.pe.lahuman.common.Constants;
import kr.pe.lahuman.runner.ConSave;
import kr.pe.lahuman.xml.XMLParser;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.jcraft.jsch.JSchException;

public class Test {
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException, NumberFormatException, JSchException {
		XMLParser xp = new XMLParser("C:\\workspaces\\kepler_workspace\\DailyCheckList\\src\\kr\\pe\\lahuman\\SERVER_INFO.xml");
		NodeList result = xp.getNodeList(Constants.SERVER);
		
		for(int i=0; i<result.getLength(); i++){
			Node node = result.item(i);
			System.out.println("start server : " + xp.getString(node, Constants.IP));
			
			ConSave cs = new ConSave(xp.getString(node, Constants.NAME),xp.getString(node, Constants.IP), Integer.parseInt(xp.getString(node, Constants.PORT)), xp.getString(node, Constants.ID), xp.getString(node, Constants.PW), "C:\\Temp\\sample.txt");
			System.out.println(xp.getList(node, Constants.COMMAND));
			//종료를 위해 exit 명령어 삽입
			List<String> command = xp.getList(node, Constants.COMMAND);
			command.add("exit");
			
			cs.saveCommand(command);
			cs.disconnect();
			System.out.println("end server : " + xp.getString(node, Constants.IP));
		}
	}
}
