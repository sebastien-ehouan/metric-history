package org.metric.history.view.parts;

import javax.annotation.PostConstruct;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.LayoutConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import java.io.*;

public class ResultsView {
	
	private Label metricHistoryHome;
	private Label metricHistoryHomePath;
	private Label versionLabel;
	private Label localRepoLabel;
	private Label outputFolderLabel;
	private Label localRepoPathLabel;
	private Label outputFolderPath;
	private Label versionWarningLabel;
	private Label sourceMeterExec;
	private Label sourceMeterExecPath;
	private Label spaceLabel;
	private Label spaceLabel4;
	
	private Text versionText;
	private Text alterCmd;
	
	private static String mhpath;
	private static String lpath;
	private static String opath;
	private static String smpath;
	private static String cmd;
	private static String alter_cmd;
	
	Shell shell = new Shell(Display.getCurrent(), SWT.SHELL_TRIM);
	Shell shell2 = new Shell(Display.getCurrent(), SWT.SHELL_TRIM);
	Shell shell3 = new Shell(Display.getCurrent(), SWT.SHELL_TRIM);
	Shell shell4 = new Shell(Display.getCurrent(), SWT.SHELL_TRIM);

	@PostConstruct
	public void createPartControl(Composite parent) {
		
		metricHistoryHome = new Label(parent, SWT.NONE);
		metricHistoryHome.setText("MetricHistory Home Path:");
		
		Button btnMetricHistoryHome = new Button(parent, SWT.PUSH);
		btnMetricHistoryHome.setText("Browse");
		
		metricHistoryHomePath = new Label(parent, SWT.CENTER);
		metricHistoryHomePath.setText("");
		
		btnMetricHistoryHome.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnMetricHistoryHome.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
            	DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
            	dialog.setMessage("Please choose a directory");
        	    dialog.setFilterPath(shell4.getText());
        	    String metricHistoryHome = dialog.open();
        	    if (metricHistoryHome != null) {
        			shell.setText(metricHistoryHome);
        		}
        	    if (metricHistoryHome != null) {
        	    	mhpath = metricHistoryHome;
        	    	metricHistoryHomePath.setText(""+mhpath);
        	    }
            }
        });
		
		versionLabel = new Label(parent, SWT.NONE);
		versionLabel.setText("Versions:");
		
		versionText = new Text(parent, SWT.BORDER);
		
		versionWarningLabel = new Label(parent, SWT.CENTER);
		versionWarningLabel.setText("** Versions file needs to be located inside MetricHistory main folder");
			
		localRepoLabel = new Label(parent, SWT.NONE);
		localRepoLabel.setText("Local Repo path:");
		
		Button btnLocalRepo = new Button(parent, SWT.PUSH);
		btnLocalRepo.setText("Browse");
		
		localRepoPathLabel = new Label(parent, SWT.CENTER);
		localRepoPathLabel.setText("");
		
		btnLocalRepo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnLocalRepo.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
            	DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
            	dialog.setMessage("Please choose a directory");
        	    dialog.setFilterPath(shell.getText());
        	    String localPath = dialog.open();
        	    if (localPath != null) {
        			shell.setText(localPath);
        		}
        	    if (localPath != null) {
        	    	lpath = localPath;
        	    	localRepoPathLabel.setText(""+lpath);
        	    }
            }
        });
		
		outputFolderLabel = new Label(parent, SWT.PUSH);
		outputFolderLabel.setText("Output Folder:");
		
		Button btnOutput = new Button(parent, SWT.NONE);
		btnOutput.setText("Browse");
		
		outputFolderPath = new Label(parent, SWT.CENTER);
		outputFolderPath.setText("");
		
		btnOutput.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnOutput.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
            	DirectoryDialog dialog = new DirectoryDialog(shell2, SWT.OPEN);
            	dialog.setMessage("Please choose a directory");
            	dialog.setFilterPath(shell2.getText());
        	    String outputPath = dialog.open();
        	    if (outputPath != null) {
        			shell2.setText(outputPath);
        		}
        	    if (outputPath != null) {
        	        opath = outputPath;
        	        outputFolderPath.setText(""+opath);
        	    }
            }
        });
		
		sourceMeterExec = new Label(parent, SWT.NONE);
		sourceMeterExec.setText("SourceMeter Executable Path:");
		
		Button btnSourceMeterExec = new Button(parent, SWT.PUSH);
		btnSourceMeterExec.setText("Browse");
		
		sourceMeterExecPath = new Label(parent, SWT.CENTER);
		sourceMeterExecPath.setText("");
		
		btnSourceMeterExec.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnSourceMeterExec.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
            	DirectoryDialog dialog = new DirectoryDialog(shell3, SWT.OPEN);
            	dialog.setMessage("Please choose a directory");
            	dialog.setFilterPath(shell3.getText());
        	    String sourceMeterPath = dialog.open();
        	    if (sourceMeterPath != null) {
        			shell2.setText(sourceMeterPath);
        		}
        	    if (sourceMeterPath != null) {
        	        smpath = sourceMeterPath;
        	        sourceMeterExecPath.setText(""+smpath);
        	    }
            }
        });
		
		Composite buttonBar = new Composite(parent, SWT.NONE);
		
		spaceLabel = new Label(parent, SWT.CENTER);
		spaceLabel.setText("");
		
		Button btnAlterCmd = new Button(parent, SWT.NONE);
		btnAlterCmd.setText("Alter CMD");
		
		alterCmd = new Text(parent, SWT.BORDER);
		
		spaceLabel4 = new Label(parent, SWT.CENTER);
		spaceLabel4.setText("** Optional");
		
		Button btnAnalyze = new Button(parent, SWT.NONE);
		btnAnalyze.setText("Analyze");
		
		Button btnCmdParam = new Button(parent, SWT.PUSH);
		btnCmdParam.setText("Display Command");
		
		btnAlterCmd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnAlterCmd.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
            	alter_cmd = "\"\""+mhpath+"\\metric-history\" collect \""+mhpath+"\\" +versionText.getText()+ "\" \"" +lpath+ "\" \"" +opath+ "\" SOURCEMETER -e=\""+smpath+"\\SourceMeterJava.exe\" > \""+opath+"\\output-log.txt\"";
        		alterCmd.setText(alter_cmd);
        		alter_cmd = alterCmd.getText();
            }
        });
		
		btnAnalyze.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnAnalyze.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) { 	
            	String s = null;
            	
            	if(alterCmd.getText() != null) {
            		alter_cmd = alterCmd.getText();
            		cmd = alter_cmd;
            	}
            	else if(alterCmd.getText() == ""){
                	cmd = "\"\""+mhpath+"\\metric-history\" collect \""+mhpath+"\\" +versionText.getText()+ "\" \"" +lpath+ "\" \"" +opath+ "\" SOURCEMETER -e=\""+smpath+"\\SourceMeterJava.exe\" > \""+opath+"\\output-log.txt\"";            		
            	}
            	            	
            	try {
                    // using the Runtime exec method:
                    Process p = Runtime.getRuntime().exec("cmd /c start /min cmd.exe /c "+cmd);
                    
                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

                    // read the output from the command
                    System.out.println("Here is the standard output of the command:\n");
                    while ((s = stdInput.readLine()) != null) {
                    	System.out.println(s);
                    }
                    
                    // read any errors from the attempted command
                    System.out.println("Here is the standard error of the command (if any):\n");
                    while ((s = stdError.readLine()) != null) {
                        System.out.println(s);
                    }
                    //System.exit(0);
                }
                
                catch (IOException e) {
                    System.out.println("An exception happened - here's what I know: ");
                    e.printStackTrace();
                }
            }
        });
		
		btnCmdParam.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnCmdParam.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
            	cmd = "\"\""+mhpath+"\\metric-history\" collect \""+mhpath+"\\" +versionText.getText()+ "\" \"" +lpath+ "\" \"" +opath+ "\" SOURCEMETER -e=\""+smpath+"\\SourceMeterJava.exe\" > \""+opath+"\\output-log.txt\"";
                
            	MessageDialog.openInformation(shell, "Command Parameters", cmd);
            }
        });
		
		Button btnClear = new Button(parent, SWT.PUSH);
		btnClear.setText("Clear");
		
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnClear.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
            	metricHistoryHomePath.setText("");
            	versionText.setText("");
            	localRepoPathLabel.setText("");
            	outputFolderPath.setText("");
            	sourceMeterExecPath.setText("");
            	alterCmd.setText("");
            }
        });
		
		GridLayoutFactory.fillDefaults().numColumns(3).generateLayout(
				buttonBar);
		GridDataFactory.fillDefaults().span(2, 1).align(SWT.CENTER,
				SWT.CENTER).applyTo(buttonBar);
		GridLayoutFactory.fillDefaults().numColumns(3).margins(
				LayoutConstants.getMargins()).generateLayout(parent);
	}

	@Focus
	public void setFocus() {
		versionLabel.setFocus();
		localRepoLabel.setFocus();
		outputFolderLabel.setFocus();
		localRepoPathLabel.setFocus();
		outputFolderPath.setFocus();
		versionWarningLabel.setFocus();
		versionText.setFocus();
		metricHistoryHome.setFocus();
		metricHistoryHomePath.setFocus();
		sourceMeterExecPath.setFocus();
		spaceLabel.setFocus();
		spaceLabel4.setFocus();
	}
}
