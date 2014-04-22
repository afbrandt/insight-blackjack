public class Card 
{

	private String cardSuit;
	private String cardValue;
	private int pointValue;
	
	public Card(String suit, String value, int val)
	{
		cardSuit = suit;
		cardValue = value;
		pointValue = val;
	}
	
	public String getValue()
	{
		return cardValue;
	}
	
	public String getType()
	{
		return cardSuit;
	}
	
	public int getPointValue()
	{
		return pointValue;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s of %s", cardValue, cardSuit);
	}
}
