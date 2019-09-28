package lab4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Lab_Assignment_4
{
		private static class FastaSequence
		{
			/**
			 * Helper (inner) class contains 2 variables, 3 methods, and a static factory method.
			 */
			private String head;
			private String seq;
			
			public FastaSequence(String head, String seq)
			{
				this.head = head;
				this.seq = seq;
			}
			public String getHeader()
			{
				return this.head.replaceAll(">", "");
			}
			public String getSequence()
			{
				return this.seq;
			}
			public float getGCRatio()
			{
				String current_seq = this.seq;
				Map<Character,Integer> baseMap = new TreeMap<Character,Integer>();
				float seq_len = current_seq.length();
				/**
				 * The following is adapted from Dr. Fodor's code in Lecture 9.
				 * This is so much cleaner and in a better time scale than the nested 
				 * for loops which I used previously.
				 */
				for( int x=0; x < seq_len; x++)
				{
					Integer base_count = baseMap.get(current_seq.charAt(x));
					if( base_count == null)
					{
						base_count = 0;
					}
					base_count++;
					baseMap.put(current_seq.charAt(x), base_count);
				}
				float num_C = baseMap.get('C');
				float num_G = baseMap.get('G');
				float output_ratio = ((num_C + num_G) / seq_len) * 100;
				return output_ratio;
			}
			public static List<FastaSequence>readFastaFile(String filepath) throws Exception
			{
				/**
				 * Static factory method that instantiates a list of FastaSequence objects.
				 */
				BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)));
				String line = reader.readLine();
				String header = "";
				String sequence = "";
				HashMap<String,String> seqMap = new HashMap<String,String>();
				List<FastaSequence>readFastaFile = new ArrayList<FastaSequence>();
				while(line != null)
				{
					if( line.startsWith(">") == true)
					{
						header = line;
						sequence = "";
						line = reader.readLine();
					}
					else
					{
						sequence = sequence + line;
						seqMap.put(header, sequence);
						line = reader.readLine();
					}
				}
				reader.close();
				for( Map.Entry<String,String> entry : seqMap.entrySet())
				{
					FastaSequence fs = new FastaSequence(entry.getKey(), entry.getValue());
					readFastaFile.add(fs);
				}
				return readFastaFile;
			}
		}
		public static void main(String[] args) throws Exception
		{
			System.out.println("\n\nThis is a FASTA parser.\nPlease input the filepath to the FASTA file:");
			//String input_filepath = System.console().readLine();
			String input_filepath = "wooble.fasta";
			List<FastaSequence>fastaList = FastaSequence.readFastaFile(input_filepath);
			for( FastaSequence fs : fastaList)
			{
				System.out.println(fs.getHeader());
				System.out.println(fs.getSequence());
				System.out.println(fs.getGCRatio());
			}
		}
		/***
		 * This needs fixing to be like slide 3 in the Lab
		 * 
		 * public static void writeUnique(File inFile, File outFile) throws Exception
		 *
		{*/
			/**
			 * for key in 1st hashmap, add all 1st hashmap values to second hashmap as keys the value incrementing for each new key occurrence
			 
			Map<String, Integer> countMap = new TreeMap<String, Integer>();
			for( x=0; x < keys in seqMap; x++)
			{
				if( countMap.get(key) == false)
				{
						countMap.put(key, 0);
				}
				else
				{
					countMap.put(key, countMap.get(key) + 1);
				}
			}
			for( key in countMap)
			{
				
			}
		}*/
}