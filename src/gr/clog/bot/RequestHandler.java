package gr.clog.bot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class RequestHandler 
{
	private static RequestHandler instance = null;
	private BufferedWriter writer;
	private List<String> authTokens;
	private List<String> channels;
	private List<String> channelsToLog;
	private DbLogger dbLogger;
	
	protected RequestHandler()
	{
		authTokens = new ArrayList<String>();
		channels = new ArrayList<String>();
		channelsToLog = new ArrayList<String>();
	}
	
	public static RequestHandler getInstance()
	{
		if(instance==null)
		{
			instance = new RequestHandler();
		}
		return instance;
	}
	
	public String processRequest(Request request) throws IOException
	{
		if(request.isPing())
		{
			return "PONG slapmelikeabitch\n";
		}
		
		if(request.isAuth())
		{
			String password = request.getLine().split("!auth")[1].trim();
			if(password.equals("c0kle"))
			{
				authTokens.add(getAuthToken(request.getLine()));
			}
		}
		
		if(request.isSystemMsg())
		{
			
		}
		
		if(request.isChannelMsg())
		{
			String[] msg = request.getLine().split("PRIVMSG");
			String nick = msg[0].substring(1, msg[0].indexOf("!"));
			String text = msg[1].split(":")[1];
			String chan = msg[1].split(":")[0].trim();
			Date d = new Date();
			
			if(channelsToLog.contains(chan.toLowerCase().toString()))
			{
				dbLogger.logMsg(nick, chan, text, d.getTime());
			}
		}
		
		if(request.isCommand())
		{
			if(isAuthenticated(getAuthToken(request.getLine())))
			{
				if(request.isJoin())
				{
					String channel = request.getLine().split("!join")[1];
			        channels.add(channel);
			        return "JOIN "+channel+"\n";
				}
				else if(request.isPart())
				{
					String channel = request.getLine().split("!part")[1];
			        channels.remove(channel);
			        return "PART "+channel+"\n";
				}
				if(request.isLog())
				{
					String channel = request.getLine().split("!log")[1].trim();
					channelsToLog.add(channel.toLowerCase().toString());
				}
				if(request.isUnlog())
				{
					String channel = request.getLine().split("!unlog")[1].trim();
					channelsToLog.remove(channel);
				}
				else if(request.isExit())
				{
					return "QUIT\n";
				}
			}
		}
		return "";
	}

	private String getAuthToken(String line)
	{
		String temp = line.split("!auth")[0];
		String nick = temp.substring(1, temp.indexOf("!"));
		String host = temp.substring(temp.indexOf("!")+1, temp.indexOf(" PRIVMSG"));
		return nick+"@"+host;
	}
	
	private boolean isAuthenticated(String authToken)
	{
		return authTokens.contains(authToken);
	}
	
	public void setWriter(BufferedWriter writer)
	{
		instance.writer = writer;
	}

	public void setDbLogger(DbLogger dbLogger)
	{
		this.dbLogger = dbLogger;
	}	
}
