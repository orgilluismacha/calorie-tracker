package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumnModel;

import org.w3c.dom.css.Rect;


public class TrackerFrame extends JFrame{
	
	LogData data, today_input;
	FoodInfo foodlist;
	Object[] thisprofile;
	JButton logfood_today;
	JComboBox foodcb;

	ArrayList foodlistcb;
	menuItemActionListener al = new menuItemActionListener();
	ProfilePanel pcomponents;
	HabitsPanel phabits;
	ProgressPanel pprogress;
	JLabel recommendedJL = new JLabel();
	
	//Harris-Benedict BMR
	
	public class ProfilePanel extends JPanel{
		GridBagConstraints gbc = new GridBagConstraints();
		ProfilePanel(){
			JLabel welcomemsg = new JLabel("Welcome, " + thisprofile[0] +"!");
			welcomemsg.setFont(new Font("Arial", Font.ITALIC + Font.BOLD, 25));
			welcomemsg.setForeground(Color.green.darker().darker());
			add(welcomemsg);
			
			this.setLayout(new GridBagLayout());
			
			JLabel userJL = new JLabel("Username: " + thisprofile[0]);
			gbc.gridx=0;
			gbc.gridy=1;
			gbc.anchor = GridBagConstraints.LINE_START;
			add(userJL, gbc);
			
			JLabel ageJL = new JLabel("Your age: " +thisprofile[3], SwingConstants.LEFT);
			gbc.gridx=0;
			gbc.gridy=2;
			add(ageJL, gbc);
			
			JLabel hJL = new JLabel("Your height: " +thisprofile[1], SwingConstants.LEFT);
			gbc.gridx=0;
			gbc.gridy=3;
			add(hJL, gbc);
			
			JLabel wJL = new JLabel("Your weight: " +thisprofile[2], SwingConstants.LEFT);
			gbc.gridx=0;
			gbc.gridy=4;
			add(wJL, gbc);
			
			String planJLtoString="";
			if(thisprofile[5]==PlanType.WEIGHT_LOSS) {planJLtoString= "lose weight.";}else {planJLtoString= "gain weight.";}
			
			JLabel planJL = new JLabel("You would like to " + planJLtoString) ;
			gbc.gridx=0;
			gbc.gridy=5;
			add(planJL, gbc);
			

			
		}
	}
	
	public class HabitsPanel extends JPanel{
		HabitsPanel(){
			JLabel welcomemsg = new JLabel("Welcome, " + thisprofile[0] +"!");
			welcomemsg.setFont(new Font("Arial", Font.ITALIC + Font.BOLD, 25));
			welcomemsg.setForeground(Color.green.darker().darker());
			add(welcomemsg);
			
			this.setLayout(new BorderLayout());
			
			JTable table;
			table = new JTable(data);
			add(new JScrollPane(table), BorderLayout.CENTER);
			table.setFillsViewportHeight(true);
			
			TableColumnModel tcm = table.getColumnModel();
			tcm.getColumn(2).setPreferredWidth(100); 
			
	        table.setAutoResizeMode(table.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		}
	}
	
	public class ProgressPanel extends JPanel
	{
		JProgressBar prog;
		double recom=dailyCalorie();
		double did=consumedSoFar();
		
		private int dailyCalorie()
		{
			double recommended=0;
			
			if(thisprofile[5] == Gender.MALE)
			{
				recommended=66.5 
						+ (13.75 * Double.valueOf((String) thisprofile[2])) 
						+ (5.003 * Double.valueOf((String) thisprofile[1])) 
						- (6.755 * Double.valueOf((String) thisprofile[3]));
			}else {
				recommended=655.1 
						+ ( 9.563 * Double.valueOf((String) thisprofile[2])) 
						+ ( 1.850 *  Double.valueOf((String) thisprofile[1])) 
						- (4.676 *  Double.valueOf((String) thisprofile[3]));
			}
			
			
			return (int) recommended;
		}
		
		private int consumedSoFar()
		{
			int totalcal=0;
			
			for (int i = 0; i < today_input.getRowCount(); i++) {

				totalcal = totalcal + (Integer) today_input.foodlogs.get(i)[1];
			}
			
			return totalcal;
			
		}
		
		private void calcRecom()
		{
			if((dailyCalorie() - consumedSoFar()) <= 0)
			{
				recommendedJL.setText("You consumed " + (-1)*(dailyCalorie() - consumedSoFar())+ " extra calories");
			}else {

				recommendedJL.setText("You may consume " + (dailyCalorie() - consumedSoFar()) + " calories today");
			}
		}
		
		
		GridBagConstraints c = new GridBagConstraints();
		ProgressPanel(){
			JPanel pCal= new JPanel();
			JPanel pLog= new JPanel();
			int diff=dailyCalorie() - consumedSoFar();
			
			JLabel welcomemsg = new JLabel("Your daily intake, " + thisprofile[0] +"!");
			welcomemsg.setFont(new Font("Arial", Font.ITALIC + Font.BOLD, 25));
			welcomemsg.setForeground(Color.green.darker().darker());
			c.gridx=0;
			c.gridy=0;
			add(welcomemsg);
			
			this.setLayout(new GridBagLayout());
			
			JTable table;
			table = new JTable(today_input);
			c.gridx=0;
			c.gridy=1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.ipady = 300; 
			add(new JScrollPane(table), c);
			table.setFillsViewportHeight(true);
			c.ipady = 0; 
			TableColumnModel tcm = table.getColumnModel();
			tcm.getColumn(2).setPreferredWidth(100); 
			
	        table.setAutoResizeMode(table.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
	        
	       
	        
	        
	        
	        
	        calcRecom();
	        pCal.add(recommendedJL);
	        
			JLabel add_food = new JLabel("What did you eat today?");
			add_food.setFont(new Font("Arial", Font.ITALIC + Font.BOLD, 12));
			pLog.add(add_food);
			
			foodcb = new JComboBox(foodlistcb.toArray());
			pLog.add(foodcb);
			
			logfood_today = new JButton ("Add food");
			pLog.add(logfood_today);
			
			LogFoodActionListener al= new LogFoodActionListener();
			logfood_today.addActionListener(al);
			
			
			c.gridx=0;
			c.gridy=2;
			
			add(pCal, c);
			c.gridx=0;
			c.gridy=3;
			
			add(pLog, c);
			
		}
		
		
		public class LogFoodActionListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource()==logfood_today)
				{
					System.out.println("Food add button");
					
					for (int i = 0; i < foodlist.foodinfo.size(); i++) {
						if(foodlist.foodinfo.get(i)[0] == foodcb.getSelectedItem())
						{
							data.addUserLog(foodlist.foodinfo.get(i)[0], (Integer )foodlist.foodinfo.get(i)[1]);
							data.fireTableDataChanged();
							today_input.addUserLog(foodlist.foodinfo.get(i)[0], (Integer )foodlist.foodinfo.get(i)[1]);
							today_input.fireTableDataChanged();
							break;
						}
					}
					
					calcRecom();
				}
			}
		}
	}
	
	
	JButton x1, x2, x3;

	JPanel mainpanel = new JPanel();
	
	public void initComponents()
	{
		this.setLayout(new BorderLayout());
		//Main container of the window
		mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.Y_AXIS));
		
		//Sub panels of the window. Menu bar panel will always be there, while the components panel will switch between the 3
		JPanel pmenubar = new JPanel();
		
		//Setting up the menu bar
		JMenuBar mb= new JMenuBar();
		pmenubar.add(mb);


		pcomponents = new ProfilePanel();
		phabits = new HabitsPanel();
		pprogress = new ProgressPanel();
		
		x1 = new JButton("Daily progress");
		x1.addActionListener(al);
		x2 = new JButton("View eating habit");
		x2.addActionListener(al);
		x3 = new JButton("Profile");
		x3.addActionListener(al);
		
		mb.add(x1);
		mb.add(x2);
		mb.add(x3);

		//Adding the sub panels to the main panel, and the main panel to the window
		mainpanel.add(pmenubar);
		
		mainpanel.add(pcomponents);
		
		mainpanel.add(phabits);
		phabits.setVisible(false);
		
		mainpanel.add(pprogress);
		pprogress.setVisible(false);
		
		add(mainpanel);
		
	}
	public class menuItemActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==x1)
			{
				pcomponents.setVisible(false);
				pprogress.setVisible(true);
				phabits.setVisible(false);
				System.out.println("Daily progress");
			}
			if(e.getSource()==x2)
			{
				pcomponents.setVisible(false);
				pprogress.setVisible(false);
				phabits.setVisible(true);
				System.out.println("View eating habit");
			}
			if(e.getSource()==x3)
			{
				pcomponents.setVisible(true);
				pprogress.setVisible(false);
				phabits.setVisible(false);
				System.out.println("Profile");
			}
			
		}
		
	}
	public TrackerFrame(Object[] _thisProfile)
    {
		
    	super("Calorie Tracker | ");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        
        thisprofile=_thisProfile;
        
        
        final String filename = thisprofile[0] + "_logs.dat";
        
        // Load data at start up
        try {
            data = new LogData();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
            data.foodlogs = (List<Object[]>)ois.readObject();
            ois.close();
            
            today_input= new LogData(data.omit());
            
        } catch(Exception ex) {
        	
        	
            ex.printStackTrace();
        }
        
        //Load food list
        try {
            foodlist = new FoodInfo();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("food_info.dat"));
            foodlist.foodinfo = (List<Object[]>)ois.readObject();
            ois.close();
            
			foodlistcb = foodlist.getList();
            
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        
        this.setTitle("Calorie Tracker | " + thisprofile[0]);
        // Save data at shutdown:
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
                    oos.writeObject(data.foodlogs);
                    oos.close();
                } catch(Exception ex) {
                	
                    ex.printStackTrace();
                }
                
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("food_info.dat"));
                    oos.writeObject(foodlist.foodinfo);
                    oos.close();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        // Build the window:
        setSize(new Dimension(500, 500));
        setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        initComponents();
    }
}
