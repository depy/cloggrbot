package gr.clog.bot;

import gr.clog.bot.logging.DbLogger;
import gr.clog.bot.logging.FileLogger;
import gr.clog.bot.logging.ILogger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class CloggrBot implements Runnable
{
	private String server;
    private int port = 6667;
	private String nick;
	private BufferedWriter writer;
	private BufferedReader reader;
	private boolean exit = false;
	private String line;
	private RequestHandler requestHandler;
	private List<ILogger> loggers;
	
	public CloggrBot(String server, int port, String nick, String logFilePath) throws InterruptedException
	{
		this.server = server;
		this.port = port;
		this.nick = nick;
	    this.requestHandler = RequestHandler.getInstance();    
	    ILogger dbLogger = new DbLogger("localhost", 27017, "cloggr", "irclogs");
	    ILogger fileLogger = new FileLogger(logFilePath);
	    this.loggers = new ArrayList<ILogger>();
	    this.loggers.add(dbLogger);
	    this.loggers.add(fileLogger);
	    
	}
	
	@Override
	public void run() 
	{
		try 
		{
			requestHandler.setLoggers(loggers);
			connect();
			boolean joinedChannels = false;
			while(!exit)
			{
				line = reader.readLine();
				Request request = new Request(line, server);
				String response = requestHandler.processRequest(request);
				if(response.length()>0)
				{
					writer.write(response);
					writer.flush();
				}
				if(line.contains("End of /MOTD command.") && !joinedChannels)
				{
					System.out.println("Joining speified channels (this may take a while!)...");
					joinChans(this.requestHandler.getChannels());
					joinedChannels=true;
				}
			}
		} 
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void connect() throws UnknownHostException, IOException
	{
		Socket socket = new Socket(server, port);
		writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		login(socket.getLocalAddress().toString());
	}
	
	public void login(String address) throws IOException
	{
        writer.write("NICK "+nick+"\n");
        writer.write("USER "+nick+" "+address+": Cloggr Bot\n");
        writer.flush();
	}
	
	public void joinChans(List<String> chans)
	{
		try 
		{
			for(String chan: chans)
			{
				writer.write("JOIN "+chan+"\n");
				writer.flush(); 
				Thread.sleep(1000);
			}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws InterruptedException, IOException
	{
		if(args.length!=4)
		{
			System.out.println("Invalid number of passed arguments...");
		}
		else
		{			
			CloggrBot bot = new CloggrBot(args[0], Integer.parseInt(args[1]), args[2], args[3]);
			Thread t = new Thread(bot);
			t.start();
			t.join();
		}
	}
}
