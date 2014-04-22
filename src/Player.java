
public class Player extends Participant{

	private int chipCount;
	private Hand splitHand;
	private boolean hasSplit = false;
	
	public Player(String name)
	{
		super(name);
		chipCount = 100;
		splitHand = new Hand();
	}
	
	public void placeBet(int bet) throws GameException
	{
		if (bet > chipCount)
		{
			throw new GameException("Insufficient chips!");
		}
		if (bet <= 0)
		{
			throw new GameException("Can't bet a negative or zero amount.");
		}
		
		chipCount -= bet;
	}
	
	public void addChips(int chips)
	{
		chipCount += chips;
	}
	
	public int getChipCount()
	{
		return chipCount;
	}
	
	public Hand getSplitHand()
	{
		return splitHand;
	}
	
	public void addCardToSplitHand(Card c)
	{
		splitHand.addCard(c);
		hasSplit = true;
	}
	
	public void clearSplitHand()
	{
		splitHand.emptyHand();
		hasSplit = false;
	}
	
	public boolean hasSplit()
	{
		return hasSplit;
	}
}
