package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class SignUpFrame extends JFrame{

	private Object[] genderobj= {"MALE","FEMALE"};
	private Object[] ageobj= {"18","19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
			"31", "32", "33", "34", "35", "36", "37", "38", "39", 
			"40", "41", "42", "43", "44", "45", "46", "47", "48", "49", 
			"50", "51", "52", "53", "54", "55", "56", "57", "58", "59", 
			"60", "61", "62", "63", "64", "65", "66", "67", "68", "69", 
			"70", "71", "72", "73", "74", "75", "76", "77", "78", "79", 
			"80", "81", "82", "83", "84", "85", "86", "87", "88", "89", 
			};
	private Object[] planobj= {"I want to lose weight", "I want to gain weight"};
	private ProfileData data;
    private JButton signmeup = new JButton("Sign me up!");
    private Object[] thisprofile;
    private List<Object[]> foodlogs = new ArrayList<Object[]>();
	private JTextField usernametf = new JTextField(20);
	private JTextField passwordtf = new JTextField(20);
	private JTextField heighttf = new JTextField(20);
	private JTextField weighttf = new JTextField(20);
	private JComboBox agecb = new JComboBox(ageobj);
	private JComboBox gendercb = new JComboBox(genderobj);
	private JComboBox plancb = new JComboBox(planobj);
	private JLabel msg = new JLabel();
	private String existing_username = "Username already exists!";
	private String signup_success = "Sign up successful. Welcome! ";
	private boolean newuser=false;
	
	
    public void initSignUpComponents() 
    {
    	JPanel p =new JPanel();
    	JPanel pS= new JPanel();
    	p.setLayout(new GridLayout(7,1,10,5));
    	pS.setLayout(new GridLayout(0,1,10,5));
    	p.setBorder(new EmptyBorder(5,5,5,5));
    	
    	JLabel username = new JLabel("Choose a username: ");
    	JLabel password = new JLabel("Choose a password: ");
    	JLabel height = new JLabel("Your height in cm: ");
    	JLabel weight = new JLabel("Your weight in kg: ");
    	JLabel age = new JLabel("Your age: ");
    	JLabel gender = new JLabel("Your gender: ");
    	JLabel plan = new JLabel("What is your goal?");
    	
    	
		
    	//Setting up font attributes for msg
    	msg.setFont(new Font("Arial", Font.ITALIC + Font.BOLD, msg.getFont().getSize()));
    	msg.setForeground(Color.red);
    	msg.setVisible(false);
    	msg.setHorizontalAlignment(JLabel.CENTER);

    	signUpButtonActionListener al = new signUpButtonActionListener();
    	
    	//Frame layout arrangements
        this.setLayout(new BorderLayout());
        
        p.add(username);
        p.add(usernametf);
        p.add(password);
        p.add(passwordtf);
        p.add(height);
        p.add(heighttf);
        p.add(weight);
        p.add(weighttf);
        p.add(age);
        p.add(agecb);
        p.add(gender);
        p.add(gendercb);
        p.add(plan);
        p.add(plancb);
        pS.add(msg, BorderLayout.CENTER);
        pS.add(signmeup);
        
        signmeup.addActionListener(al);
        
        add(p, BorderLayout.CENTER);
        add(pS, BorderLayout.PAGE_END);
    }
    
    public class signUpButtonActionListener implements ActionListener
    {

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(e.getSource()==signmeup)
			{
				if(data.signUpPolice(usernametf.getText()))
				{
					System.out.println("Existing");
					msg.setText(existing_username);
					msg.setVisible(true);
				}else {
					data.addProfile(usernametf.getText(),
									passwordtf.getText() , 
									heighttf.getText(),
									weighttf.getText(),
									agecb.getSelectedItem(),
									gendercb.getSelectedItem(), 
									plancb.getSelectedItem());
					
					msg.setText(signup_success);
			    	msg.setForeground(Color.green.darker());
					msg.setVisible(true);
					newuser=true;
				}
			}
		}
    	
    }
    
    @SuppressWarnings("unchecked")
	public SignUpFrame() 
	{
		super("Calorie Tracker | SIGN UP");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
     // Load data at start up
        try {
            data = new ProfileData();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("profiles.dat"));
            data.profiles = (List<Object[]>)ois.readObject();
            ois.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        
        // Save data at shutdown:
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("profiles.dat"));
                    oos.writeObject(data.profiles);
                    oos.close();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
                
                if(newuser)
                {
                    //Create user profile
                    final String filename = data.profiles.get(data.profiles.size()-1)[0] + "_logs.dat";
                    
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
                        oos.writeObject(foodlogs);
                        oos.close();
                    } catch(Exception ex) {
                    	
                        ex.printStackTrace();
                    }
                    newuser=false;
                }
            }
        });

        setSize(new Dimension(600, 300));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        setResizable(false);
        initSignUpComponents();
	}
}
