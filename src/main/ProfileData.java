package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ProfileData implements Serializable{
	/*
	 * Each Object[] is a user profile with the following elements: 
	 * Name 		(String)
	 * Height		(Double)
	 * Weight 		(Double)
	 * Age 			(Int)
	 * Gender 		(Enum)
	 * Plan type 	(Enum)
	 * Password		(String)
	 */
	List<Object[]> profiles = new ArrayList<Object[]>();
	
	public Object logInPolice(String username, String pw)
	{
		for (int i=0; i<profiles.size(); i++) 
		{
			if(username.equals(profiles.get(i)[0]))
			{
				if(pw.equals(profiles.get(i)[6]))
				{
					return i;
				}
			}
		}
		return "null";
	}
	
	public Object[] getProfileAt(int id)
	{
		return profiles.get(id);
		
	}

	
	public boolean signUpPolice(String username)
	{
		for (int i=0; i<profiles.size(); i++) 
		{
			if(username.equals(profiles.get(i)[0]))
			{
				return true;
			}
		}
		return false;
	}

	public void addProfile(Object username, Object pw, Object h, Object w, Object age, Object _gender, Object _plan) {
		
		Gender gender = null;
		if(_gender.equals("MALE"))
		{
			gender=Gender.MALE;
		}else {
			gender=Gender.FEMALE;
		}
		
		PlanType plan;
		if(_plan.equals("I want to lose weight"))
		{
			plan=PlanType.WEIGHT_LOSS;
		}else {
			plan=PlanType.WEIGHT_GAIN;
		}
		Object[] newprofile = {username, h, w, age, gender, plan, pw};
		
	    profiles.add(newprofile);
		
	}
	
}
