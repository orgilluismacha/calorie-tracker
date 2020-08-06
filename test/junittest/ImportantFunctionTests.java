package junittest;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import main.Gender;
import main.PlanType;
import main.ProfileData;

public class ImportantFunctionTests {

	ProfileData profile;
	
	@Before
	public void setUp()
	{
		profile = new ProfileData();
		profile.addProfile("admin", "password", 1, 1, 1, Gender.FEMALE, PlanType.WEIGHT_GAIN);
	}
	
	@Test
	public void logInPoliceTest() 
	{
		String result = "null";
		Assert.assertEquals(result, profile.logInPolice("admin", "password"));
	}

}
//public Object logInPolice(String username, String pw)
//{
//	for (int i=0; i<profiles.size(); i++) 
//	{
//		if(username.equals(profiles.get(i)[0]))
//		{
//			if(pw.equals(profiles.get(i)[6]))
//			{
//				return i;
//			}
//		}
//	}
//	return "null";
//}