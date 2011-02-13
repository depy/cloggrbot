package gr.clog.bot.logging;

public interface ILogger 
{
	public void logMsg(String nick, String channel, String msg, long time);
}
