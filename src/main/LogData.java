package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class LogData  extends AbstractTableModel implements Serializable{

	/*
	 * Food name			(String)
	 * Amount in grams		(int)
	 * Date of consumption	(Date)
	 * 
	 */
	
	List<Object[]> foodlogs = new ArrayList<Object[]>();
    private String[] titles= {"Food","Calories","Date of Consumption",};
	
	public LogData()
	{
		
	}
	
	public LogData(List<Object[]> omit) {
		this.foodlogs=omit;
	}

	public void addUserLog(Object food_name, int amount)
	{
			Date now= Calendar.getInstance().getTime();
			
			Object[] newlog = {food_name, amount, now};
			foodlogs.add(newlog);

		
	}

	@Override
	public int getColumnCount() {
		
		return 3;
	}

	@Override
	public int getRowCount() {
		return foodlogs.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		return foodlogs.get(arg0)[arg1];
	}
	
	@Override
	public String getColumnName(int column) {
        return titles[column];
    }
	
	public List<Object[]> omit()
	{
		List<Object[]> new_log = new ArrayList<Object[]>();
		
		Date today = Calendar.getInstance().getTime();
		
		for (int i = 0; i < this.getRowCount(); i++) 
		{
			if(today.getYear() == ((Date) foodlogs.get(i)[2]).getYear() 
					&& today.getMonth() == ((Date) foodlogs.get(i)[2]).getMonth()
					&& today.getDay() == ((Date) foodlogs.get(i)[2]).getDay()) 
			{
				new_log.add(foodlogs.get(i));
			}else {
				if(i == this.getRowCount()-1)
				new_log.add(new Object[]{0,0,0});
				
			}
		}
		
		return new_log;
	}
}
