package lab1;

import java.util.Random;
import java.lang.Math;

public class Lab_Assignment_1
{
	public static void main(String[] args)
	{
		int match_AAA=0;
		int total_codons=0;
		for( int x=0; x < 1000; x++)
		{
			Random random = new Random();
			String codon = "";
			for( int b=0; b < 3; b++)
			{
				String new_letter = "";
				int r = random.nextInt(4);
				if ( r==0)
				{
					new_letter = "A";
				}
				else if ( r==1)
				{
					new_letter = "C";
				}
				else if ( r==2)
				{
					new_letter = "G";
				}
				else if ( r==3)
				{
					new_letter = "T";
				}
				else
					System.out.println("Whoopsie");
				codon = codon + new_letter;
			}
			total_codons++;
			System.out.println(codon);
			if( codon.equals("AAA"))
			{
				match_AAA++;
			}
		}
		System.out.println("The total number of codons in this sample is: " + total_codons);
		System.out.println("The observed number of AAA codons in this sample is: " + match_AAA);
		System.out.println("The probability of a AAA codon would be " + Math.pow(0.25, 3));
		System.out.println("The expected frequency of AAA codons in " + total_codons + " samples would be roughly " + total_codons * Math.pow(0.25,  3));
		System.out.println("This is close to the expected frequency.");
	}
}