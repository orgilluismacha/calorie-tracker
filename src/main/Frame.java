package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.swing.*;


public class Frame extends JFrame 
{
    public ProfileData data;
	private JButton loginBtn = new JButton("LOG IN");
	private JButton signupBtn = new JButton("SIGN UP");
	private JTextField usernametf = new JTextField(10);
	private JPasswordField passwordtf = new JPasswordField(10);
	private JLabel msg = new JLabel();

	private String incorrect= "Incorrect username or password!";
	private String login_success= "Log in successful. Welcome back!";
    
    //Initialize log in components
    private void initLogInComponents()
    {
    	JPanel p = new JPanel();
    	JPanel pS = new JPanel();
    	JPanel pC = new JPanel();
    	
    	JLabel usernamelbl = new JLabel("Username: ");
    	JLabel passwordlbl = new JLabel("Password: ");
    	
    	//Setting up font attributes for msg
    	msg.setFont(new Font("Arial", Font.ITALIC + Font.BOLD, msg.getFont().getSize()));
    	msg.setForeground(Color.red);
    	msg.setVisible(false);
    	
    	
    	logInButtonActionListener login_al = new logInButtonActionListener();
    	
    	//Frame layout arrangements
        this.setLayout(new BorderLayout());
        
        p.add(usernamelbl);
        p.add(usernametf);
        p.add(passwordlbl);
        p.add(passwordtf);
        add(p, BorderLayout.PAGE_START);
        
        loginBtn.addActionListener(login_al);
        signupBtn.addActionListener(login_al);
        
        pC.add(msg);
        add(pC, BorderLayout.CENTER);
        
        pS.add(loginBtn);
        pS.add(signupBtn);
        add(pS, BorderLayout.PAGE_END);
        
    }
    
    //Log in button action listener
    private class logInButtonActionListener implements ActionListener
    {

		@SuppressWarnings("deprecation")
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==loginBtn)
			{
				System.out.println("login");
				if(!data.logInPolice(usernametf.getText(), passwordtf.getText()).equals("null"))
				{
					int loginID = (Integer)data.logInPolice(usernametf.getText(), passwordtf.getText());
					
					
					msg.setText(login_success);
			    	msg.setForeground(Color.green.darker());
					msg.setVisible(true);

					//Casting logged in profile info to Object[]
					Object[] thisProfile= data.getProfileAt(loginID);
					
					
					TrackerFrame trckf = new TrackerFrame(thisProfile);
					trckf.setVisible(true);
					dispose();
					
				}else{
					msg.setText(incorrect);
					msg.setVisible(true);
				}
			}
			
			if(e.getSource() == signupBtn)
			{
					msg.setVisible(false);
					SignUpFrame signupframe = new SignUpFrame();
					signupframe.setVisible(true);
					dispose();
				
			}
			
		}
    	
    }
    
    @SuppressWarnings("unchecked")
	public Frame()
    {
    	super("Calorie Tracker | LOG IN");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
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
            }
        });
        
        // Build the window:
        setSize(new Dimension(400, 130));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        setResizable(false);
        initLogInComponents();
    }
}
