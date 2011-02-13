package gr.clog.bot;

import java.util.regex.*;

public class Request
{
	private String line;
	private String server;
	
	public Request(String line, String server) 
	{
		System.out.println(line);
		this.line = line;
		this.server = server;
	}
	
	public boolean isPing()
	{
		Pattern pingRegex = Pattern.compile( "^PING", Pattern.CASE_INSENSITIVE );
        Matcher ping = pingRegex.matcher(this.line);
        return ping.find();
	}
	
	public boolean isExit()
	{
        Pattern exitRegex = Pattern.compile( "!exit", Pattern.CASE_INSENSITIVE );
        Matcher exit = exitRegex.matcher(this.line);
        return exit.find() && isCommand();
	}
	
	
	public boolean isAuth()
	{
        Pattern joinRegex = Pattern.compile( "!auth", Pattern.CASE_INSENSITIVE );
        Matcher join = joinRegex.matcher(this.line);
        return join.find() && isCommand();
	}
	
	public boolean isJoin()
	{
        Pattern joinRegex = Pattern.compile( "!join", Pattern.CASE_INSENSITIVE );
        Matcher join = joinRegex.matcher(this.line);
        return join.find() && isCommand();
	}
	
	public boolean isPart()
	{
        Pattern partRegex = Pattern.compile( "!part", Pattern.CASE_INSENSITIVE );
        Matcher part = partRegex.matcher(this.line);
        return part.find() && isCommand();
	}
	
	public boolean isLog()
	{
        Pattern logRegex = Pattern.compile( "!log", Pattern.CASE_INSENSITIVE );
        Matcher log = logRegex.matcher(this.line);
        return log.find() && isCommand();
	}
	
	public boolean isUnlog()
	{
        Pattern unlogRegex = Pattern.compile( "!unlog", Pattern.CASE_INSENSITIVE );
        Matcher unlog = unlogRegex.matcher(this.line);
        return unlog.find() && isCommand();
	}
	
	public boolean isCommand()
	{
        Pattern commandRegex = Pattern.compile(" PRIVMSG ", Pattern.CASE_INSENSITIVE );
        Matcher command = commandRegex.matcher(this.line);
        return command.find() && line.charAt(line.indexOf("PRIVMSG")+8)!='#';
	}
	
	public boolean isChannelMsg()
	{
        Pattern channelMsgRegex = Pattern.compile(" PRIVMSG #", Pattern.CASE_INSENSITIVE );
        Matcher channelMsg = channelMsgRegex.matcher(this.line);
        return channelMsg.find() && line.charAt(line.indexOf("PRIVMSG")+8)=='#';
	}
	
	public boolean isSystemMsg()
	{
        Pattern systemMsgRegex = Pattern.compile(":"+server, Pattern.CASE_INSENSITIVE );
        Matcher systemMsg = systemMsgRegex.matcher(this.line);
        return systemMsg.find();
	}
	
	public String getLine()
	{
		return line;
	}
}
