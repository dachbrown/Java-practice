package lab2;

import java.util.Random;

public class Lab_Assignment_2_EC
{
	public static void main(String[] args)
	{
		System.out.println("Welcome to the Amino Acid Quiz. How long would you like to play in seconds?");
		int user_timer = Integer.parseInt(System.console().readLine());
		long start_time = System.currentTimeMillis();
		long end_time = start_time + user_timer * 1000;
		long current_time = System.currentTimeMillis();
		int current_score = 0;
		String user_quit = "QUIT";
		boolean exit_while_loop = false;
		boolean mistake_made = false;
		Random random = new Random();
		while(current_time < end_time && exit_while_loop == false)
		{
			int r = random.nextInt(20);
			String test_aa = FULL_NAMES[r];
			System.out.println("What is the one letter code for " + test_aa + "?");
			current_time = System.currentTimeMillis();
			String user_answer = System.console().readLine().toUpperCase();
			if( SHORT_NAMES[r].equals(user_answer) == true)
			{
				current_score++;
				CORRECT[r]++;
				System.out.println("Correct! Score: " + current_score + "\nTime Remaining: " + (end_time - current_time)/1000f + " seconds.\n");
			}
			else if( user_quit.equals(user_answer))
			{
				exit_while_loop = true;
				System.out.println("OK, you can quit the test.\n");
			}
			else if( SHORT_NAMES[r].equals(user_answer) == false)
			{
				mistake_made = true;
				MISTAKES[r]++;
				System.out.println("Sorry, that was incorrect. The correct answer is : " + SHORT_NAMES[r] + "\nTime Remaining: " + (end_time - current_time)/1000f + " seconds.\n");
			}
			else
			{
				exit_while_loop = true;
				System.out.println("This is an error message for an incorrect input.");
			}
		}
		if( mistake_made == true)
		{
			System.out.println("Here are your overall results:");
			for( int x=0; x < FULL_NAMES.length; x++)
			{
				if(MISTAKES[x] == 0 && CORRECT[x] == 0)
				{
					continue;
				}
				else
				{
					System.out.println(SHORT_NAMES[x] + "\tCorrect: " + CORRECT[x] + "; Mistakes: " + MISTAKES[x]);
				}
			}
		}
		else
		{
			System.out.println("You made no mistakes! Congratulations!");
		}
		System.out.println("The test has ended. Final score: " + current_score + "\n");
	};
	public static String[] SHORT_NAMES = 
	{
		"A","R", "N", "D", "C", "Q", "E", 
		"G",  "H", "I", "L", "K", "M", "F", 
		"P", "S", "T", "W", "Y", "V"
	};
	public static String[] FULL_NAMES = 
	{
		"alanine","arginine", "asparagine", 
		"aspartic acid", "cysteine",
		"glutamine",  "glutamic acid",
		"glycine" ,"histidine","isoleucine",
		"leucine",  "lysine", "methionine", 
		"phenylalanine", "proline", 
		"serine","threonine","tryptophan", 
		"tyrosine", "valine"
	};
	public static int[] MISTAKES = 
	{
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	};
	public static int[] CORRECT = 
		{
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
		};
};
