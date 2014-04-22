import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck 
{
	private List<Card> deck;
	
	private static final String[] CARD_VALUES = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", 
							"Eight", "Nine", "Ten", "Jack", "Queen", "King"};
	private static final int[] CARD_POINT_VALUES = {1,2,3,4,5,6,7,8,9,10,10,10,10};
	private static final String[] CARD_TYPES = {"Hearts", "Diamonds", "Clubs", "Spades"};

	public Deck()
	{
		deck = new ArrayList<Card>();
	}
	
	public Deck(List<Card> cards)
	{
		deck = cards;
	}
	
	public Card dealCard() throws GameException
	{
		if(deck.size() == 0)
		{
			throw new GameException("Deck is empty!");
		}
		
		return deck.remove(0);
	}
	
	public void addCard(Card c)
	{
		deck.add(0,c);
	}
	
	public int getSize()
	{
		return deck.size();
	}
	
	public void shuffle()
	{
		Random r = new Random();
		
		for(int i = 0; i < deck.size(); i++)
		{
			int pos = r.nextInt(1000) % deck.size();
			Card temp = deck.get(i);
			deck.set(i, deck.get(pos));
			deck.set(pos, temp);
		}
	}
	
	public static Deck generateSingleDeck()
	{
		Card card;
		Deck newDeck = new Deck();
		
		for(int i = 0; i < CARD_TYPES.length; i++)
		{
			for(int j = 0; j < CARD_VALUES.length; j++)
			{
				card = new Card( CARD_TYPES[i], CARD_VALUES[j], CARD_POINT_VALUES[j]);
				newDeck.addCard(card);
			}
		}
		
		return newDeck;
	}
	
}
