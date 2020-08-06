package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FoodInfo  implements Serializable{
	
	//Food name				(String)
	//Calories per serving	(int)
	
	List<Object[]> foodinfo = new ArrayList<Object[]>();
	
	public void addFoodInfo(String food_name, int calories)
	{
		Object[] newlog = {food_name, calories};
		foodinfo.add(newlog);
	}
	
	public ArrayList getList()
	{
		ArrayList list = new ArrayList();
		
		for (int i = 0; i < foodinfo.size(); i++) {
			list.add(foodinfo.get(i)[0]);
		}
		
		return list;
	}
}
