package org.metric.history.view.parts;

import csv.reader.*;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.*;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.LayoutConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Panel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.eclipse.swt.awt.SWT_AWT;

public class MetricsView extends ViewPart {
	
	private JTable table;
	
	private Label artifactType;
	private Label results;
	private Label projectN;
	
	private static String metricsFolder;
	private static String projectName;
	
	private Text projectTitle;
	
	Shell shell = new Shell(Display.getCurrent(), SWT.SHELL_TRIM);
	
	@PostConstruct
	public void createPartControl(Composite parent) {
		artifactType = new Label(parent, SWT.NONE);
		artifactType.setText("ARTIFACT TYPE");
		
		results = new Label(parent, SWT.NONE);
		results.setText("RESULTS - Version Folder :");
		
		List list = new List(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		
		list.add("Annotation.csv");
		list.add("Attribute.csv");
		list.add("Class.csv");
		list.add("CloneClass.csv");
		list.add("CloneInstance.csv");
		list.add("Component.csv");
		list.add("Enum.csv");
		list.add("File.csv");
		list.add("Folder.csv");
		list.add("Interface.csv");
		list.add("Method.csv");
		list.add("Package.csv");
		
		Composite viewer = new Composite(parent, SWT.NO_BACKGROUND | SWT.EMBEDDED);

		GridDataFactory.fillDefaults().span(3, 1).align(SWT.CENTER, SWT.CENTER).applyTo(viewer);
		GridLayoutFactory.fillDefaults().numColumns(2).margins(LayoutConstants.getMargins()).generateLayout(parent);
		
		projectN = new Label(parent, SWT.CENTER);
		projectN.setText("Enter Repo Name:");
		
		projectTitle = new Text(parent, SWT.BORDER);
		
		Button btnFolder = new Button(parent, SWT.PUSH);
		btnFolder.setText("Select Version Metrics Folder");
		
		btnFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnFolder.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
            	projectName = projectTitle.getText();
            	DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
            	dialog.setMessage("Please choose a directory");
        	    dialog.setFilterPath(shell.getText());
        	    String metricsFolderPath = dialog.open();
        	    if (metricsFolderPath != null) {
        			shell.setText(metricsFolderPath);
        		}
        	    if (metricsFolderPath != null) {
        	    	metricsFolder = metricsFolderPath;
        	    	results.setText("RESULTS - Version Folder :"+metricsFolder);
        	    }
            }
            
            public void widgetDefaultSelected(SelectionEvent event) {
	        }
        });
		
		list.addSelectionListener(new SelectionListener() {
		      public void widgetSelected(SelectionEvent event) {
		        
		        if(list.getSelectionIndex() == 0) {
		            CSVFile Rd = new CSVFile();
		            MyModelAnnotation NewModel = new MyModelAnnotation();
		            table.setModel(NewModel);
		        	
		        	String csvFile = metricsFolder+"\\"+projectName+"-Annotation.csv";
	                
	                File DataFile = new File(csvFile);
	                ArrayList<String[]> Rs2 = Rd.ReadCSVfile(DataFile);
	                NewModel.AddCSVData(Rs2);
		        }
		        
		        else if(list.getSelectionIndex() == 1) {	  
		            CSVFile Rd = new CSVFile();
		            MyModelAttribute NewModel = new MyModelAttribute();
		            table.setModel(NewModel);
	                String csvFile = metricsFolder+"\\"+projectName+"-Attribute.csv";
	                
	                File DataFile = new File(csvFile);
	                ArrayList<String[]> Rs2 = Rd.ReadCSVfile(DataFile);
	                NewModel.AddCSVData(Rs2);
		        }
		        
		        else if(list.getSelectionIndex() == 2) {
		            CSVFile Rd = new CSVFile();
		            MyModelClass NewModel = new MyModelClass();
		            table.setModel(NewModel);
		        	
		        	String csvFile = metricsFolder+"\\"+projectName+"-Class.csv";
	                
	                File DataFile = new File(csvFile);
	                ArrayList<String[]> Rs2 = Rd.ReadCSVfile(DataFile);
	                NewModel.AddCSVData(Rs2);
		        }
		        
		        else if(list.getSelectionIndex() == 3) {
		            CSVFile Rd = new CSVFile();
		            MyModelCloneClass NewModel = new MyModelCloneClass();
		            table.setModel(NewModel);
		        	
		        	String csvFile = metricsFolder+"\\"+projectName+"-CloneClass.csv";
	                
	                File DataFile = new File(csvFile);
	                ArrayList<String[]> Rs2 = Rd.ReadCSVfile(DataFile);
	                NewModel.AddCSVData(Rs2);
		        }
		        
		        else if(list.getSelectionIndex() == 4) {
		            CSVFile Rd = new CSVFile();
		            MyModelCloneInstance NewModel = new MyModelCloneInstance();
		            table.setModel(NewModel);
		        	
		        	String csvFile = metricsFolder+"\\"+projectName+"-CloneInstance.csv";
	                
	                File DataFile = new File(csvFile);
	                ArrayList<String[]> Rs2 = Rd.ReadCSVfile(DataFile);
	                NewModel.AddCSVData(Rs2);
		        }
		        
		        else if(list.getSelectionIndex() == 5) {
		            CSVFile Rd = new CSVFile();
		            MyModelComponent NewModel = new MyModelComponent();
		            table.setModel(NewModel);
		        	
		        	String csvFile = metricsFolder+"\\"+projectName+"-Component.csv";
	                
	                File DataFile = new File(csvFile);
	                ArrayList<String[]> Rs2 = Rd.ReadCSVfile(DataFile);
	                NewModel.AddCSVData(Rs2);
		        }
		        
		        else if(list.getSelectionIndex() == 6) {
		            CSVFile Rd = new CSVFile();
		            MyModelEnum NewModel = new MyModelEnum();
		            table.setModel(NewModel);
		        	
		        	String csvFile = metricsFolder+"\\"+projectName+"-Enum.csv";
	                
	                File DataFile = new File(csvFile);
	                ArrayList<String[]> Rs2 = Rd.ReadCSVfile(DataFile);
	                NewModel.AddCSVData(Rs2);
		        }
		        
		        else if(list.getSelectionIndex() == 7) {
		            CSVFile Rd = new CSVFile();
		            MyModelFile NewModel = new MyModelFile();
		            table.setModel(NewModel);
		        	
		        	String csvFile = metricsFolder+"\\"+projectName+"-File.csv";
	                
	                File DataFile = new File(csvFile);
	                ArrayList<String[]> Rs2 = Rd.ReadCSVfile(DataFile);
	                NewModel.AddCSVData(Rs2);
		        }
		        
		        else if(list.getSelectionIndex() == 8) {
		            CSVFile Rd = new CSVFile();
		            MyModelFolder NewModel = new MyModelFolder();
		            table.setModel(NewModel);
		        	
		        	String csvFile = metricsFolder+"\\"+projectName+"-Folder.csv";
	                
	                File DataFile = new File(csvFile);
	                ArrayList<String[]> Rs2 = Rd.ReadCSVfile(DataFile);
	                NewModel.AddCSVData(Rs2);
		        }
		        
		        else if(list.getSelectionIndex() == 9) {
		            CSVFile Rd = new CSVFile();
		            MyModelInterface NewModel = new MyModelInterface();
		            table.setModel(NewModel);
		        	
		        	String csvFile = metricsFolder+"\\"+projectName+"-Interface.csv";
	                
	                File DataFile = new File(csvFile);
	                ArrayList<String[]> Rs2 = Rd.ReadCSVfile(DataFile);
	                NewModel.AddCSVData(Rs2);
		        }
		        
		        else if(list.getSelectionIndex() == 10) {
		            CSVFile Rd = new CSVFile();
		            MyModelMethod NewModel = new MyModelMethod();
		            table.setModel(NewModel);
		        	
		        	String csvFile = metricsFolder+"\\"+projectName+"-Method.csv";
	                
	                File DataFile = new File(csvFile);
	                ArrayList<String[]> Rs2 = Rd.ReadCSVfile(DataFile);
	                NewModel.AddCSVData(Rs2);
		        }
		        
		        else if(list.getSelectionIndex() == 11) {
		        	CSVFile Rd = new CSVFile();
		            MyModelPackage NewModel = new MyModelPackage();
		            table.setModel(NewModel);
		        	
		        	String csvFile = metricsFolder+"\\"+projectName+"-Package.csv";
	                
	                File DataFile = new File(csvFile);
	                ArrayList<String[]> Rs2 = Rd.ReadCSVfile(DataFile);
	                NewModel.AddCSVData(Rs2);
		        }
		        	
		      }
		      
		      public void widgetDefaultSelected(SelectionEvent event) {
		        }
		});
		
		try {
		      System.setProperty("sun.awt.noerasebackground", "true");
		    } 
		catch (NoSuchMethodError error) {
		    }
		
		 /* Create and setting up frame */
		BorderLayout bL = new BorderLayout();
	    Frame frame = SWT_AWT.new_Frame(viewer);
	    Panel panel = new Panel(new BorderLayout()) {
	      public void update(java.awt.Graphics g) {
	        /* Do not erase the background */
	        paint(g);
	      }
	    };
	    frame.add(panel);
	    JRootPane root = new JRootPane();
	    panel.add(root);
	    java.awt.Container contentPane = root.getContentPane();
	    
	    table = new JTable(new MyModel());
	    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//	    table.setPreferredScrollableViewportSize(new Dimension(1000, 1000));
        table.setFillsViewportHeight(true);
        table.createDefaultColumnsFromModel();
        table.setRowHeight(30);
	    JScrollPane scrollPane = new JScrollPane(table);
	    scrollPane.setHorizontalScrollBarPolicy(
	    	    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    	scrollPane.setVerticalScrollBarPolicy(
	    	    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
	    contentPane.setLayout(bL);
	    contentPane.add(scrollPane);
	    
	    viewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}

	@Focus
	public void setFocus() {
		artifactType.setFocus();
		results.setFocus();
	}
	
	public class CSVFile {
        private final ArrayList<String[]> Rs = new ArrayList<String[]>();
        private String[] OneRow;

        public ArrayList<String[]> ReadCSVfile(File DataFile) {
            try {
                BufferedReader brd = new BufferedReader(new FileReader(DataFile));
                while (brd.ready()) {
                    String st = brd.readLine();
                    OneRow = st.split(",|\\s|;");
                    Rs.add(OneRow);
                } // end of while
            } // end of try
            catch (Exception e) {
                String errmsg = e.getMessage();
                System.out.println("File not found:" + errmsg);
            } // end of Catch
            return Rs;
        }// end of ReadFile method
    }// end of CSVFile class
}
