package lab1;

import java.util.Random;
import java.lang.Math;

public class Lab_Assignment_1_EC
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
				float r = random.nextFloat();
				if ( r>=0 && r<=0.12)
				{
					new_letter = "A";
				}
				else if ( r>0.12 && r<=0.50)
				{
					new_letter = "C";
				}
				else if ( r>0.50 && r<=0.89)
				{
					new_letter = "G";
				}
				else if ( r>0.89 && r<=1)
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
		System.out.println("The probability of a AAA codon would be " + Math.pow(0.12, 3));
		System.out.println("The expected frequency of AAA codons in " + total_codons + " samples would be roughly " + total_codons * Math.pow(0.12,  3));
		System.out.println("I ran the code several times, and got answers ranging from 0 to 6.");
		System.out.println("This is close to the expected frequency, but with more variance than I would prefer.");
	}
}