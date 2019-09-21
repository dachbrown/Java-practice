package lab3;
import java.io.BufferedReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Lab_Assignment_3
{
	public static void main(String[] args) throws Exception
	{
		/**
		 * Prompts the user for a FASTA file for parsing and returns the tab separated output
		 */
		int[][] data_counts;
		String[][] input_data;
		String[][] output_data;
		System.out.println("\n\nThis is a FASTA parser.\nPlease input the filepath to the FASTA file:");
		String input_filepath = System.console().readLine();
		BufferedReader reader = new BufferedReader(new FileReader(new File(input_filepath)));
		String line = reader.readLine();
		String seq = "";
		List<String> input_list = new ArrayList<String>();
		while(line != null)
		{
			if( line.startsWith(">") == false)
			{
				seq = seq + line;
				line = reader.readLine();
			}
			else
			{
				input_list.add(seq);
				seq = "";
				input_list.add(line);
				line = reader.readLine();
			}
		}
		input_list.add(seq);
		reader.close();
		input_list.remove(0);
		String[] input_array = input_list.toArray(new String[0]);
		input_data = FASTA_FORMAT(input_array);
		data_counts = FASTA_COUNTS(input_data);
		output_data = FASTA_JOIN(input_data, data_counts);
		FASTA_OUTPUT(output_data);
	}
	public static String[][] FASTA_FORMAT(String[] s)
	{
		/**
		 * Splits an array into a 2D array
		 */
		String [][] twoD_array = new String[s.length/2][2];
		for( int x=0; x < twoD_array.length; x++)
		{
			int y = x*2;
			twoD_array[x][0] = s[y];
			twoD_array[x][1] = s[y+1];
		}
		return twoD_array;
	}
	public static void FASTA_OUTPUT(String[][] s) throws IOException
	{
		/**
		 * Writes the output file
		 */
		String output_line = "sequenceID\tnumA\tnumC\tnumG\tnumT\tsequence";
		Writer writer = new FileWriter("parsed_fasta.txt");
		writer.write(output_line + "\n");
		for( int x=0; x < s.length; x++)
		{
			output_line = "";
			for( int y=0; y < s[x].length; y++)
			{
				if( y == (s[x].length - 1))
				{
					output_line = output_line + s[x][y];
				}
				else
				{
					output_line = output_line + s[x][y] + "\t";
				}
			}
			writer.write(output_line + "\n");
		}
		writer.flush(); writer.close();
	}
	public static String[][] FASTA_JOIN(String[][] s, int[][] q)
	{
		/**
		 * Method makes a new 2D array with the sequence IDs, sequences, and counts information
		 */
		int array_size = s[0].length + q[0].length;
		String[][] final_data = new String[s.length][array_size];
		for( int x=0; x < s.length; x++)
		{
			for( int y=0; y < array_size; y++)
			{
				if( y == 0)
				{
					final_data[x][y] = s[x][y];
				}
				else if( y < 5)
				{
					final_data[x][y] = String.valueOf(q[x][y - 1]);
				}
				else
				{
					final_data[x][y] = s[x][1];
				}
			}
		}
		return final_data;
	}
	public static int[] COUNT_BASES(String s)
	{
		/**
		 * Counts the number of each base in a string
		 */
		int[] COUNTS = {0,0,0,0};
		char[] temp_s = s.toCharArray();
		char[] BASES = {'A','C','G','T'};
		for( int x=0; x < s.length(); x++)
		{
			for( int y=0; y < BASES.length; y++)
			{
				if(temp_s[x] == (BASES[y]))
				{
					COUNTS[y]++;
				}
			}
		}
		/** The below is a much more verbose, less effective method of the above.
		 * I am saving this comment as reference.
		 * for( int x=0; x < s.length(); x++)
		{
			char pos = s.charAt(x);
			if( pos == 'A')
			{
				COUNTS[0]++;
			}
			else if( pos == 'C')
			{
				COUNTS[1]++;
			}
			else if( pos == 'G')
			{
				COUNTS[2]++;
			}
			else if( pos == 'T')
			{
				COUNTS[3]++;
			}
			else
			{
				System.out.println("There was an error in COUNT_BASES");
			}
		}
		*/
		return COUNTS;
	}
	public static int[][] FASTA_COUNTS(String[][] s)
	{
		/**
		 * Calls the COUNT_BASES method on a 2D array, creating a new 2D array with the count data
		 */
		int[][] fasta_counts = new int[s.length][4];
		for( int x=0; x < s.length; x++)
		{
			int[] base_counts; 
			base_counts = COUNT_BASES(s[x][1]);
			for( int y=0; y < base_counts.length; y++)
			{
				fasta_counts[x][y] = base_counts[y];
			}
		}
		return fasta_counts;
	}
}
