import java.util.List;

public abstract class Participant 
{
	private Hand participantHand;
	private String participantName;
	
	public Participant(String name)
	{
		participantName = name;
		participantHand = new Hand();
	}
	
	public List<Card> showHand()
	{
		return participantHand.getHand();
	}
	
	public void clearHand()
	{
		participantHand.emptyHand();
	}
	
	public Card showCardAtIndex(int n)
	{
		return participantHand.getHand().get(n);
	}
	
	public Card removeCardFromHand(Card c) throws GameException
	{
		return participantHand.removeCard(c);
	}
	
	public void addCardToHand(Card c)
	{
		participantHand.addCard(c);
	}
	
	public int getPointValue()
	{
		return participantHand.computeHandPointValue();
	}
	
	public String getName()
	{
		return participantName;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s's hand: %s", participantName, participantHand);
	}
}
