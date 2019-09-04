package lab2;

import java.util.Random;

public class Lab_Assignment_2
{
	public static void main(String[] args)
	{
		long start_time = System.currentTimeMillis();
		long end_time = start_time + 30000;
		long current_time = System.currentTimeMillis();
		int current_score = 0;
		Random random = new Random();
		while(current_time < end_time)
		{
			int r = random.nextInt(20);
			String test_aa = FULL_NAMES[r];
			System.out.println("What is the one letter code for " + test_aa + "?");
			String user_answer = System.console().readLine().toUpperCase();
			if( SHORT_NAMES[r].equals(user_answer))
			{
				current_score++;
				System.out.println("Correct! Score: " + current_score + "; Time Remaining: " + (end_time - current_time)/1000f + " seconds.");
			}
			else
			{
				System.out.println("Sorry, that was incorrect.  The test has ended.");
			}
			current_time = System.currentTimeMillis();
		}
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

};
