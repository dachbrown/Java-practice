package lab4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
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
		public static void writeUnique(File inFile, File outFile) throws Exception
		{
			/**
			 * Creates an output file which identifies and counts unique Fasta sequences from an input file.
			 */
			BufferedReader reader = new BufferedReader(new FileReader(inFile));
			String line = reader.readLine();
			String sequence = "";
			Map<String,Integer> countMap = new TreeMap<String,Integer>();
			while(line != null)
			{
				if( line.startsWith(">") == true && sequence != "")
				{
					Integer seq_count = countMap.get(sequence);
					if( seq_count == null)
					{
						seq_count = 0;
					}
					seq_count++;
					countMap.put(sequence, seq_count);
					sequence = "";
					line = reader.readLine();
				}
				else
				{
					sequence = sequence + line;
					line = reader.readLine();
				}
			}
			reader.close();
			Writer writer = new FileWriter(outFile);
			for( Map.Entry<String,Integer> entry : countMap.entrySet())
			{
				writer.write(">" + entry.getValue() + "\n" + entry.getKey() + "\n");
			}
			writer.flush(); writer.close();
		}
		public static void main(String[] args) throws Exception
		{
			System.out.println("\n\nThis is a FASTA parser.\nPlease input the filepath to the FASTA file:");
			String input_filepath = System.console().readLine();
			List<FastaSequence>fastaList = FastaSequence.readFastaFile(input_filepath);
			File inFile = new File(input_filepath);
			File outFile = new File(input_filepath + ".output.txt");
			writeUnique(inFile, outFile);
			for( FastaSequence fs : fastaList)
			{
				System.out.println(fs.getHeader());
				System.out.println(fs.getSequence());
				System.out.println(fs.getGCRatio());
			}
		}
}