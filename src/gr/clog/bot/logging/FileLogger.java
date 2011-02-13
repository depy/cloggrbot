package gr.clog.bot.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class FileLogger implements ILogger
{
	String filePath;
	BufferedWriter writer;
	public FileLogger(String filePath)
	{
		this.filePath = filePath;
	}
	
	@Override
	public void logMsg(String nick, String channel, String msg, long time)
	{
			try
			{
				if(getFilePath()!=filePath || writer==null)
				{
					initWriter();
				}
				writer.write(String.valueOf(time)+" "+channel+" "+nick+" "+msg+"\r\n");
				writer.flush();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}			
	}
	
	private String getFilePath()
	{
		Calendar cal = Calendar.getInstance();
		String day = String.valueOf(cal.get(Calendar.DATE));
		String month = String.valueOf(cal.get(Calendar.MONTH)+1);
		String year = String.valueOf(cal.get(Calendar.YEAR));
		if(day.length()==1) day="0"+day;
		if(month.length()==1) month="0"+day;
		String fileName = new String(year+month+day+".log");
		return fileName;
	}
	
	public void initWriter() throws IOException
	{
		if(writer!=null)
		{
			writer.close();
		}
		writer = new BufferedWriter(new FileWriter(filePath+File.separator+getFilePath(), true));
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		writer.close();
	}

}
