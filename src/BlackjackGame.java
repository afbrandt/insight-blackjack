import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class BlackjackGame {

	private Player player;
	private Dealer dealer;

	private boolean exit = false;

	private int currentBet = 0;
	
	private Scanner keyboard;
	
	private List<Card> discardPile;

	public static void main(String[] args) {

		BlackjackGame game = new BlackjackGame();
		game.startGame();

	}

	public void startGame() {
		keyboard = new Scanner(System.in);
		discardPile = new ArrayList<Card>();
		
		System.out.println("Welcome to the blackjack table!");
		System.out.print("Enter your name: ");

		String name = keyboard.next();
		keyboard.nextLine();

		Deck deck = Deck.generateSingleDeck();

		player = new Player(name);
		dealer = new Dealer("Bob the dealer", deck);

		dealer.shuffleDeck();
		
		System.out.println("Let's play a hand.");

		do 
		{
			dealer.shuffleDeck();
			
			bet();
			dealOpening();
			
			playHand(player);
			
			if (player.getPointValue() <= 21) {
				playHand(dealer);
				awardPlayer(determineWinner());
			}
			else
			{
				determineWinner();
			}
			
			discardHands();
			
			if (player.getChipCount() > 0)
			{
				System.out.printf("Want to continue playing, %s? (y/n) ", player.getName());
				if (keyboard.nextLine().charAt(0) == 'n') 
				{
					exit = true;
				}
				else
				{
					System.out.println("Well let's play another hand!");
				}
			}
		} 
		while (player.getChipCount() > 0 && !exit);

		if (player.getChipCount() > 100)
		{
			System.out.printf("You left the table with %d chips, nice job!\n", player.getChipCount());
		}
		else
		{
			System.out.println("Thanks for playing!");
		}

	}

	private void dealOpening()
	{
		dealCard(player);
		dealCard(player);
		
		dealCard(dealer);
		dealCard(dealer, false);
		System.out.printf("%s was dealt a face down card...\n", dealer.getName());
	}

	private int determineWinner() 
	{
		int handValue = player.getPointValue();
		int dealerValue = dealer.getPointValue();
		int reward = 0;
		
		if (player.hasSplit())
		{
			System.out.printf("%s's first hand...\n", player.getName());
		}
		
		if (handValue > 21)
		{
			System.out.println("You're over 21... Busted!");
		}
		else if (dealerValue > 21)
		{
			System.out.printf("%s is over 21! You win!\n", dealer.getName());
			reward += currentBet * 2;
		}
		else if (handValue > dealerValue)
		{
			System.out.println("You win the hand!");
			reward += currentBet * 2;
		}
		else if (handValue == dealerValue)
		{
			System.out.println("Push!  It's a tie...");
			reward += currentBet;
		}
		else
		{
			System.out.println("Tough luck, the house wins...");
		}
		
		if (player.hasSplit())
		{
			handValue = player.getSplitHand().computeHandPointValue();
			
			System.out.printf("%s's second hand...\n", player.getName());
			
			if (handValue > 21)
			{
				System.out.println("You're over 21... Busted!");
			}
			else if (dealerValue > 21)
			{
				System.out.printf("%s is over 21! You win!\n", dealer.getName());
				reward += currentBet * 2;
			}
			else if (handValue > dealerValue)
			{
				System.out.println("You win the hand!");
				reward += currentBet * 2;
			}
			else if (handValue == dealerValue)
			{
				System.out.println("Push!  It's a tie...");
				reward += currentBet;
			}
			else
			{
				System.out.println("Tough luck, the house wins...");
			}
		}
		
		return reward;
	}
	
	private void awardPlayer(int chips)
	{
		if (chips > 0)
		{
			System.out.printf("%s gets %d chips\n", player.getName(), chips);
			player.addChips(chips);
		}
	}

	private void playHand(Participant p) 
	{
		System.out.printf("======|%s's turn|======\n", p.getName());
		
		boolean stand = false;
		boolean firstMove = true;
		boolean canSplit = false;
		
		do
		{
			System.out.println(p);
			System.out.printf("Hand value: %d\n", p.getPointValue());
			int choice = 0;
			
			if (firstMove && p.getPointValue() == 21)
			{
				System.out.printf("%s has blackjack!!!\n", p.getName());
				choice = 2;
			}
			else if(p instanceof Dealer)
			{
				if (p.getPointValue() < 17)
				{
					choice = 1;
				}
				else
				{
					choice = 2;
				}
			}
			else
			{
				System.out.println("(1) Hit");
				System.out.println("(2) Stand");
				
				if (firstMove)
				{
					System.out.println("(3) Double Down");
					Card c1 = player.showCardAtIndex(0);
					Card c2 = player.showCardAtIndex(1);
					if (c1.getValue().equalsIgnoreCase(c2.getValue()))
					{
						canSplit = true;
						System.out.println("(4) Split");
					}
				}
				
				System.out.print("Choose: ");
				
				try
				{
					choice = keyboard.nextInt();
					keyboard.nextLine();
				}
				catch(Exception ex)
				{
					keyboard.nextLine();
					choice = 0;
				}
				
			}
			
			switch(choice)
			{
			case 1:
				hit(p);
				firstMove=false;
				break;
			case 2:
				stand(p);
				firstMove=false;
				stand = true;
				break;
			case 3:
				if (firstMove && p instanceof Player) 
				{
					try
					{
						((Player) p).placeBet(currentBet);
						System.out.printf("%s bet %d extra chips.\n", p.getName(), currentBet);
						currentBet *= 2;
						firstMove = false;
						stand = true;

						dealCard(p);
						stand(p);
					}
					catch(GameException ex)
					{
						System.out.println(ex);
					}
					break;
				}
			case 4:
				if (firstMove && canSplit && p instanceof Player)
				{
					Card c = p.showCardAtIndex(0);
					
					try
					{
						((Player) p).placeBet(currentBet);
						System.out.printf("%s bet %d extra chips.\n", player.getName(), currentBet);
						((Player) p).removeCardFromHand(c);
						((Player) p).addCardToSplitHand(c);
						System.out.printf("%s splits their hand!\n", player.getName());
						firstMove = false;
					}
					catch(GameException ex)
					{
						System.out.println(ex);
					}
					break;
				}
			default:
				System.out.println("Invalid selection, try again.");
				break;
			}
		}
		while (!stand && p.getPointValue() <= 21);
		
		if (player.hasSplit() && p instanceof Player)
		{
			System.out.printf("------%s's second hand------\n", player.getName());
			stand = false;
			
			do
			{
				System.out.printf("Split Hand: %s", player.getSplitHand());
				System.out.printf("Hand value: %d\n",player.getSplitHand().computeHandPointValue());
				int choice = 0;
				
				
				System.out.println("(1) Hit");
				System.out.println("(2) Stand");
					
				System.out.print("Choose: ");
					
				try
				{
					choice = keyboard.nextInt();
					keyboard.nextLine();
				}
				catch(Exception ex)
				{
					keyboard.nextLine();
					choice = 0;
				}
				
				switch(choice)
				{
				case 1:
					dealCardToHand(player.getSplitHand());
					break;
				case 2:
					System.out.printf("%s stands at %d.\n", player.getName(), player.getSplitHand().computeHandPointValue());
					stand = true;
					break;
				
				default:
					System.out.println("Invalid selection, try again.");
					break;
				}
			}
			while(!stand && player.getSplitHand().computeHandPointValue() <= 21);
		}
	}

	private void stand(Participant p) 
	{
		System.out.printf("%s stands at %d.\n", p.getName(), p.getPointValue());
	}

	private void hit(Participant p) 
	{
		System.out.printf("%s hits!\n", p.getName());
		dealCard(p);
	}

	private void bet() 
	{
		boolean betPlaced = false;
		int bet = 0;
		
		do
		{
			System.out.println("Place your bet for this hand.");
			System.out.printf("Enter amount (1-%d): ", player.getChipCount());
			//System.out.println();
			
			try
			{
				bet = keyboard.nextInt();
				
				player.placeBet(bet);
				betPlaced = true;
				currentBet = bet;
			}
			catch(GameException ex)
			{
				System.out.println(ex);
			}
			catch(InputMismatchException ex)
			{
				System.out.println("Invalid entry! Try again...");
			}
			
			keyboard.nextLine();
		}
		while(!betPlaced);
	}
	
	private void dealCardToHand(Hand h)
	{
		boolean cardDealt = false;
		
		do
		{
			try
			{
				System.out.printf("Hand was dealt a %s.\n", dealer.dealCardToHand(h));
				cardDealt = true;
			}
			catch (GameException ex)
			{
				System.out.println(ex);
				System.out.println("Rebuilding deck...");
				dealer.recoverCards(discardPile);
				discardPile.clear();
				dealer.shuffleDeck();
			}
		}
		while(!cardDealt);
	}
	
	private void dealCard(Participant p)
	{
		dealCard(p, true);
	}
	
	private void dealCard(Participant p, boolean display)
	{
		boolean cardDealt = false;
		
		do
		{
			try
			{
				if (display)
				{
					System.out.printf("%s was dealt a %s.\n", p.getName(), dealer.dealCardToParticipant(p));
				}
				else
				{
					dealer.dealCardToParticipant(p);
				}
				
				cardDealt = true;
			}
			catch (GameException ex)
			{
				System.out.println(ex);
				System.out.println("Rebuilding deck...");
				dealer.recoverCards(discardPile);
				discardPile.clear();
				dealer.shuffleDeck();
			}
		}
		while(!cardDealt);
	}
	
	private void discardHands() 
	{
		discardPile.addAll(player.showHand());
		player.clearHand();
		
		if (player.hasSplit())
		{
			discardPile.addAll(player.getSplitHand().getHand());
			player.clearSplitHand();
		}
		
		discardPile.addAll(dealer.showHand());
		dealer.clearHand();
	}
}
