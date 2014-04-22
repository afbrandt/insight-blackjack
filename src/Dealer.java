import java.util.List;

public class Dealer extends Participant
{

	private Deck deck;
	
	public Dealer(String name, Deck d)
	{
		super(name);
		deck = d;
	}
	
	public Card dealCardToParticipant(Participant p) throws GameException
	{
		Card c = deck.dealCard();
		p.addCardToHand(c);
		return c;
	}
	
	public Card dealCardToHand(Hand h) throws GameException
	{
		Card c = deck.dealCard();
		h.addCard(c);
		return c;
	}
	
	public void recoverCards(List<Card> discard)
	{
		for (Card c: discard)
		{
			deck.addCard(c);
		}
	}
	
	public void shuffleDeck()
	{
		deck.shuffle();
	}
	
	
}
