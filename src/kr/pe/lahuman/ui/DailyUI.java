package kr.pe.lahuman.ui;

import java.io.File;
import java.io.IOException;
import java.util.List;

import kr.pe.lahuman.common.Constants;
import kr.pe.lahuman.runner.ConSave;
import kr.pe.lahuman.xml.XMLParser;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

public class DailyUI {

	
	static Logger log = Logger.getLogger(DailyUI.class);

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
		new DailyUI();
	}
	
	Display display = new Display();
	Shell shell = new Shell(display, SWT.CLOSE);
	
	public DailyUI() {
		shell.setText("SSH COMMAND SAVE");
		shell.setLayout(new FillLayout());
		shell.setSize(400, 200);
		
		Composite wholeComposite = new Composite(shell, SWT.NONE);
		GridLayout layoutWhole = new GridLayout();
		layoutWhole.numColumns = 1;
		wholeComposite.setLayout(layoutWhole);
		
		final Group group = makeGroupUI(wholeComposite, "INFO",
				3);
		
		
		makeChoiseFile(group , "INPUT XML PATH :", "FILE", true);
		makeChoiseFile(group , "OUTPUT TXT PATH :", "FILE", false);

		
		Group btnGroup = new Group(wholeComposite, SWT.NONE);
		org.eclipse.swt.layout.FormLayout layout = new org.eclipse.swt.layout.FormLayout();

		layout.marginLeft = layout.marginRight = 5;
		btnGroup.setLayout(layout);
		btnGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		
		Button loadBtn = new Button(btnGroup, SWT.PUSH);
		FormData loadData = new FormData();
		loadData.left = new FormAttachment(37, 0);

		loadBtn.setLayoutData(loadData);
		loadBtn.setText("SSH COMMAND SAVE");
		
		loadBtn.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				final Shell dialog = new Shell(shell, SWT.DIALOG_TRIM
						| SWT.APPLICATION_MODAL);
				dialog.setLayout(new FillLayout());
				dialog.setSize(400, 120);
				dialog.setText("Result ");

				Composite wholeComposite = new Composite(dialog, SWT.NONE);
				GridLayout layoutWhole = new GridLayout();
				layoutWhole.numColumns = 1;
				wholeComposite.setLayout(layoutWhole);
				Label infoLab = new Label(wholeComposite, SWT.NONE);
				infoLab.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				infoLab.setText("Daily Checking........");
				final ProgressBar bar = new ProgressBar(wholeComposite,
						SWT.SMOOTH);
				bar.setBounds(10, 10, 200, 20);
				bar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

				final Button clsBtn = new Button(wholeComposite, SWT.PUSH);
				clsBtn.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
				clsBtn.setText("OK");
				clsBtn.setEnabled(false);
				clsBtn.addSelectionListener(new SelectionListener() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						dialog.close();
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {

					}
				});
				final String inputpath = ((Text) group.getChildren()[1]).getText();
				final String outputpath = ((Text) group.getChildren()[4]).getText();
				//validate
				if(checkStr(inputpath, "INPUT XML PATH")){
					return;
				}
				if(checkStr(outputpath, "OUTPUT TXT PATH")){
					return;
				}
				
				//makeData
				try{
					final XMLParser xp = new XMLParser(inputpath);
					final NodeList result = xp.getNodeList(Constants.SERVER);
					bar.setMaximum(result.getLength());
					log.info("inputpath : " + inputpath);
					log.info("outputpath : " + outputpath);
					new Thread() {
						public void run() {
							try{
								for(int i[]= new int[1]; i[0]<result.getLength(); i[0]++){
									Node node = result.item(i[0]);
									log.info("---------------------------------------");
									log.info("SERVER INFO  : NAME=>["+xp.getString(node, Constants.NAME)+"] IP=>[" + xp.getString(node, Constants.IP)+"]");
//									log.info("ID/PW  : [" + xp.getString(node, Constants.ID)+"/"+ xp.getString(node, Constants.PW)+"]");
									
									ConSave cs = new ConSave(xp.getString(node, Constants.NAME), xp.getString(node, Constants.IP), Integer.parseInt(xp.getString(node, Constants.PORT)), xp.getString(node, Constants.ID), xp.getString(node, Constants.PW), outputpath);
									//종료를 위해 exit 명령어 삽입
									List<String> command = xp.getList(node, Constants.COMMAND);
									command.add("exit");
									log.info("=> SAVE COMMAND START ");
									log.info("=> " + command);
									cs.saveCommand(command);
									log.info("=> SAVE COMMAND END ");
									cs.disconnect();
									barUpdate(bar, clsBtn, i);
									log.info("---------------------------------------");
								}
							}catch(Exception e){
								log.debug(e.toString());
								showMessage(e.getMessage());
								display.syncExec(new Runnable() {
									@Override
									public void run() {
										if(dialog.isVisible())
											dialog.close();	
									}
								});
							}
						}
					}.start();
					
				}catch(Exception e){
					log.debug(e.toString());
					if(e instanceof SAXParseException){
						showMessage("XML 형식을 다시 확인 하세요.");
					}else{
						showMessage(e.getMessage());
					}
				}
				dialog.open();
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
			}
		});
		
		
		
		
		shell.open();
		// textUser.forceFocus();

		// Set up the event loop.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				// If no more entries in event queue
				display.sleep();
			}
		}

		display.dispose();
	}
	private  FileDialog makeFileDialog(String[] filterNames, String[] filterExtensions) {
		return makeFileDialog(filterNames, filterExtensions, SWT.OPEN);
	}
	private  FileDialog makeFileDialog(String[] filterNames, String[] filterExtensions, int type) {
		FileDialog dialog = new FileDialog(
				shell, type);
	
		String filterPath = "/";
		String platform = SWT.getPlatform();
		if (platform.equals("win32") || platform.equals("wpf")) {
			filterPath = "c:\\";
		}
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterPath(filterPath);
		dialog.setFileName("");
		return dialog;
	}
	
	Button fileOutBtn = null;
	Text filepathOutput =null; 
	Button fileInBtn = null;
	Text filepathInput =null; 
	private void makeChoiseFile(Group commonGroup, String lableText,
			final String btnText, boolean isInput) {
		Label label = new Label(commonGroup, SWT.NULL);
		label.setText(lableText);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		if(isInput){
			filepathInput = new Text(commonGroup, SWT.SINGLE | SWT.BORDER);
			filepathInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			filepathInput.setEnabled(false);
			fileInBtn = new Button(commonGroup, SWT.PUSH);
			fileInBtn.setText(btnText);
			fileInBtn.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			fileInBtn.addSelectionListener(input);
		}else{
			filepathOutput = new Text(commonGroup, SWT.SINGLE | SWT.BORDER);
			filepathOutput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			filepathOutput.setEnabled(false);
			fileOutBtn = new Button(commonGroup, SWT.PUSH);
			fileOutBtn.setText(btnText);
			fileOutBtn.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			fileOutBtn.addSelectionListener(output);
		}
	}
	
	SelectionListener input = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			
			String[] filterNames = new String[] { "XML File" };
			String[] filterExtensions = new String[] { "*.xml" };
			
			FileDialog dialog = makeFileDialog(filterNames, filterExtensions, SWT.OPEN);
			try {
				String filePath  = dialog.open();
				File file = new File(filePath);
				
				if(!file.isFile() && !file.exists()){
					showMessage("파일이 존재 하지 않습니다.");
				}else{
					filepathInput.setText(filePath);
				}
			} catch (Exception e) {
				log.debug(e.toString());
			}
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			filepathOutput.setText("");
		}
	};
	SelectionListener output = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent arg0) {
			
				String[] filterNames = new String[] { "TXT File" };
				String[] filterExtensions = new String[] { "*.txt" };
				
				FileDialog dialog = makeFileDialog(filterNames, filterExtensions, SWT.SAVE);
				try {
					filepathOutput.setText(dialog.open());
				} catch (Exception e) {
					log.debug(e.toString());
				}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			filepathOutput.setText("");
		}
	};
	
	private Group makeGroupUI(Composite wholeComposite,
			String commonGrouptitle, int commonColumns) {
		final Group commonGroup = new Group(wholeComposite, SWT.NONE);
		commonGroup.setText(commonGrouptitle);
		GridLayout layout = new GridLayout();
		layout.numColumns = commonColumns;
		commonGroup.setLayout(layout);

		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		commonGroup.setLayoutData(data);
		return commonGroup;
	}

	private void showMessage(final String message) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				final Shell dialogMsg =
				          new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
				        dialogMsg.setLayout(new GridLayout());
				        dialogMsg.setText("ERROR MASSAGE!");
				        Label msg = new Label(dialogMsg, SWT.NULL);
						msg.setText(message);
				        msg.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));
				        
				        Button okButton = new Button(dialogMsg, SWT.PUSH);
				        okButton.setText("OK");
				        okButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
				        okButton.addSelectionListener(new SelectionListener() {
				          public void widgetSelected(SelectionEvent e) {
				            dialogMsg.close();
				          }

				          public void widgetDefaultSelected(SelectionEvent e) {
				          }
				        });

				        dialogMsg.pack();
				        dialogMsg.open();
				
//				MessageDialog.openError(shell, "Error", message);
			}
		});
	}
	private boolean checkStr(String str, String pattan){
		if("".equals(str.trim() ))
			showMessage("Check your "+pattan);
		
		return "".equals(str.trim() );
	}
	
	private void barUpdate(final ProgressBar bar,
			final Button clsBtn, final int[] i) {
		display.syncExec(new Runnable() {
			public void run() {
				
				if (bar.isDisposed())
					return;
				// bar update
				bar.setSelection(i[0]+1);
				if (bar.getMaximum() <=( i[0]+1)) {
					clsBtn.setEnabled(true);
				}
			}
		});
	}
}
