
public class GameException extends Exception 
{

	private String message;
	
	public GameException(String m)
	{
		message = m;
	}
	
	@Override
	public String toString()
	{
		return message;
	}
}
