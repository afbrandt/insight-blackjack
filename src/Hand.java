import java.util.ArrayList;
import java.util.List;

public class Hand 
{
	private List<Card> hand;
	
	public Hand()
	{
		hand = new ArrayList<Card>();
	}
	
	public void addCard(Card c)
	{
		hand.add(c);
	}
	
	public Card removeCard(Card c) throws GameException
	{
		if(hand.isEmpty())
		{
			throw new GameException("Hand is empty!");
		}
		
		if(!hand.contains(c))
		{
			throw new GameException("Card not in hand!");
		}
		
		return hand.remove(hand.indexOf(c));
	}
	
	public boolean containsCard(Card c)
	{
		return hand.contains(c);
	}
	
	public List<Card> getHand()
	{
		return hand;
	}
	
	public int computeHandPointValue()
	{
		int result = 0;
		boolean hasAce = false;
		
		for (Card c: hand)
		{
			result += c.getPointValue();
			if (c.getValue().equalsIgnoreCase("Ace")) hasAce = true;
		}
		
		if (hasAce && (result + 10 <= 21)) result += 10;
		
		return result;
	}
	
	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		
		for(Card c: hand)
		{
			str.append(c.toString());
			str.append(", ");
		}
		
		return str.toString().substring(0, str.length()-2);
	}

	public void emptyHand() 
	{
		hand.clear();
	}
}
