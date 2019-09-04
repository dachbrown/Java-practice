package lab1;

import java.util.Arrays;
import java.util.Random;
/*import org.apache.commons.math3.stat.inference.ChiSquareTest;*/

public class Lab_Assignment_1_EC_2
{	
	public static void main(String[] args)
	{
		int num_codons = 10000;
		int num_trials = 10000;
		long[][] two_way_table;
		two_way_table = new long[65][num_codons];
		
		for( int y=0; y < num_trials; y++)
		{
			for( int x=0; x < num_codons; x++)
			{
				Random random = new Random();
				String codon = "";
				for( long b=0; b < 3; b++)
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
						System.out.println("Error no letter assigned");
					codon = codon + new_letter;
				}
				if( codon.equals("AAA"))
				{
					two_way_table[1][y]++;
				}
				else if( codon.equals("AAC"))
				{
					two_way_table[2][y]++;
				}
				else if( codon.equals("AAG"))
				{
					two_way_table[3][y]++;
				}
				else if( codon.equals("AAT"))
				{
					two_way_table[4][y]++;
				}
				else if( codon.equals("ACA"))
				{
					two_way_table[5][y]++;
				}
				else if( codon.equals("ACC"))
				{
					two_way_table[6][y]++;
				}
				else if( codon.equals("ACG"))
				{
					two_way_table[7][y]++;
				}
				else if( codon.equals("ACT"))
				{
					two_way_table[8][y]++;
				}
				else if( codon.equals("AGA"))
				{
					two_way_table[9][y]++;
				}
				else if( codon.equals("AGC"))
				{
					two_way_table[10][y]++;
				}
				else if( codon.equals("AGG"))
				{
					two_way_table[11][y]++;
				}
				else if( codon.equals("AGT"))
				{
					two_way_table[12][y]++;
				}
				else if( codon.equals("ATA"))
				{
					two_way_table[13][y]++;
				}
				else if( codon.equals("ATC"))
				{
					two_way_table[14][y]++;
				}
				else if( codon.equals("ATG"))
				{
					two_way_table[15][y]++;
				}
				else if( codon.equals("ATT"))
				{
					two_way_table[16][y]++;
				}
				else if( codon.equals("CAA"))
				{
					two_way_table[17][y]++;
				}
				else if( codon.equals("CAC"))
				{
					two_way_table[18][y]++;
				}
				else if( codon.equals("CAG"))
				{
					two_way_table[19][y]++;
				}
				else if( codon.equals("CAT"))
				{
					two_way_table[20][y]++;
				}
				else if( codon.equals("CCA"))
				{
					two_way_table[21][y]++;
				}
				else if( codon.equals("CCC"))
				{
					two_way_table[22][y]++;
				}
				else if( codon.equals("CCG"))
				{
					two_way_table[23][y]++;
				}
				else if( codon.equals("CCT"))
				{
					two_way_table[24][y]++;
				}
				else if( codon.equals("CGA"))
				{
					two_way_table[25][y]++;
				}
				else if( codon.equals("CGC"))
				{
					two_way_table[26][y]++;
				}
				else if( codon.equals("CGG"))
				{
					two_way_table[27][y]++;
				}
				else if( codon.equals("CGT"))
				{
					two_way_table[28][y]++;
				}
				else if( codon.equals("CTA"))
				{
					two_way_table[29][y]++;
				}
				else if( codon.equals("CTC"))
				{
					two_way_table[30][y]++;
				}
				else if( codon.equals("CTG"))
				{
					two_way_table[31][y]++;
				}
				else if( codon.equals("CTT"))
				{
					two_way_table[32][y]++;
				}
				else if( codon.equals("GAA"))
				{
					two_way_table[33][y]++;
				}
				else if( codon.equals("GAC"))
				{
					two_way_table[34][y]++;
				}
				else if( codon.equals("GAG"))
				{
					two_way_table[35][y]++;
				}
				else if( codon.equals("GAT"))
				{
					two_way_table[36][y]++;
				}
				else if( codon.equals("GCA"))
				{
					two_way_table[37][y]++;
				}
				else if( codon.equals("GCC"))
				{
					two_way_table[38][y]++;
				}
				else if( codon.equals("GCG"))
				{
					two_way_table[39][y]++;
				}
				else if( codon.equals("GCT"))
				{
					two_way_table[40][y]++;
				}
				else if( codon.equals("GGA"))
				{
					two_way_table[41][y]++;
				}
				else if( codon.equals("GGC"))
				{
					two_way_table[42][y]++;
				}
				else if( codon.equals("GGG"))
				{
					two_way_table[43][y]++;
				}
				else if( codon.equals("GGT"))
				{
					two_way_table[44][y]++;
				}
				else if( codon.equals("GTA"))
				{
					two_way_table[45][y]++;
				}
				else if( codon.equals("GTC"))
				{
					two_way_table[46][y]++;
				}
				else if( codon.equals("GTG"))
				{
					two_way_table[47][y]++;
				}
				else if( codon.equals("GTT"))
				{
					two_way_table[48][y]++;
				}
				else if( codon.equals("TAA"))
				{
					two_way_table[49][y]++;
				}
				else if( codon.equals("TAC"))
				{
					two_way_table[50][y]++;
				}
				else if( codon.equals("TAG"))
				{
					two_way_table[51][y]++;
				}
				else if( codon.equals("TAT"))
				{
					two_way_table[52][y]++;
				}
				else if( codon.equals("TCA"))
				{
					two_way_table[53][y]++;
				}
				else if( codon.equals("TCC"))
				{
					two_way_table[54][y]++;
				}
				else if( codon.equals("TCG"))
				{
					two_way_table[55][y]++;
				}
				else if( codon.equals("TCT"))
				{
					two_way_table[56][y]++;
				}
				else if( codon.equals("TGA"))
				{
					two_way_table[57][y]++;
				}
				else if( codon.equals("TGC"))
				{
					two_way_table[58][y]++;
				}
				else if( codon.equals("TGG"))
				{
					two_way_table[59][y]++;
				}
				else if( codon.equals("TGT"))
				{
					two_way_table[60][y]++;
				}
				else if( codon.equals("TTA"))
				{
					two_way_table[61][y]++;
				}
				else if( codon.equals("TTC"))
				{
					two_way_table[62][y]++;
				}
				else if( codon.equals("TTG"))
				{
					two_way_table[63][y]++;
				}
				else if( codon.equals("TTT"))
				{
					two_way_table[64][y]++;
				}
				else
					System.out.println("Error no codon match");
			}
		}
		System.out.println(Arrays.deepToString(two_way_table));
		/*ChiSquareTest new_test = new ChiSquareTest();
		new_test.chiSquare(two_way_table);
		System.out.println(Arrays.deepToString(two_way_table));
		ChiSquareTest test_table = new ChiSquareTest();
		Test_table.chiSquare(two_way_table);*/
	}
}
