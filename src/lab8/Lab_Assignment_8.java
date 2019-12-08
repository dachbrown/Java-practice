package lab8;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Lab_Assignment_8 extends JFrame
{
	private static final long serialVersionUID = 3794059922116119530L;
	private JButton cancelButton = new JButton("Cancel");
	private JButton startButton = new JButton("Start");
	private JTextArea mainTextArea = new JTextArea();
	private JTextArea calculatorTextArea = new JTextArea();
	private JScrollPane scrollArea = new JScrollPane(calculatorTextArea);
	private JTextField inputTextField = new JTextField();
	private boolean inputIsNumber = false;
	private String userInput = "";
	private int userNumber = 0;
	private long startTime = 0;
	private long endTime = 0;
	private long timeElapsed = 0;
	private volatile boolean calculationComplete;
	private List<Integer> outputList = new ArrayList<Integer>();
	private String directionPrompt = "Welcome to the Prime Number Calculator! Click below to get started!";
	private String continuePrompt = "Click below to start again!";
	private String endingPrompt = "";
	private AtomicIntegerArray threadSafeList;
	private List<Integer> finalOutputList = new ArrayList<Integer>();
	
	private class CancelButtonActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			endPrimeCalculator();
		}
	}
	private CancelButtonActionListener myCBAL = new CancelButtonActionListener();
	private void endPrimeCalculator()
	{
		calculationComplete = true;
		try
		{
			// Creates a formatted, readable output from the thread safe data structure
			createFinalOutput();
			endTime = System.currentTimeMillis();
			//System.out.println(outputList);
			timeElapsed = endTime - startTime;
			//System.out.println(timeElapsed);
			endingPrompt = "This calculation took " + timeElapsed/1000f + " seconds.\nThe number of primes found was " + outputList.size() + ".\n";
			inputIsNumber = false;
			inputTextField.setEditable(true);
			inputTextField.setText("");
			calculatorTextArea.setText("");
			getContentPane().remove(scrollArea);
			getContentPane().remove(inputTextField);
			getContentPane().remove(cancelButton);
			getContentPane().add(startButton, BorderLayout.SOUTH);
			getContentPane().add(mainTextArea);
			mainTextArea.setText(endingPrompt);
			mainTextArea.append(continuePrompt);
		}
		catch( Exception ex )
		{
			JOptionPane.showMessageDialog(mainTextArea, "Exception with cancelling calculator.");
		}
		calculationComplete = false;
		setVisible(true);
		repaint();
	}
	
	private class StartCalculatorActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			promptUserForInput();
		}
	}
	private StartCalculatorActionListener mySCAL = new StartCalculatorActionListener();
	private void promptUserForInput()
	{
		try
		{
			getContentPane().remove(startButton);
			getContentPane().add(cancelButton, BorderLayout.NORTH);
			getContentPane().add(inputTextField, BorderLayout.SOUTH);
		}
		catch( Exception ex )
		{
			JOptionPane.showMessageDialog(mainTextArea, "Exception with starting calculator.");
		}
		mainTextArea.setText("Please input a large number in the text box below and hit 'Enter'!");
		setVisible(true);
		repaint();
		inputTextField.requestFocusInWindow();
	}
	
	private class RunCalculatorActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			cancelButton.setEnabled(true);
			// Validate user input
			checkUserInput();
			// Create a list of integers from 1 to the user input as a thread safe data structure
			makeIntegerList();
			// Use the Sieve of Eratosthenes
			/*
			 * This is where some of TO DO stuff must go
			 */
			
			// Then split into the 4 threads
			new Thread(myPCAR).start();
		}
	}
	private RunCalculatorActionListener myRCAL = new RunCalculatorActionListener();
	private void checkUserInput()
	{
		userInput = inputTextField.getText();
		inputTextField.setEditable(false);
		getContentPane().remove(mainTextArea);
		getContentPane().add(scrollArea, BorderLayout.CENTER);
		setVisible(true);
		repaint();
		try
		{
			userNumber = Integer.valueOf(userInput);
			if( userNumber > 1 )
			{
				inputIsNumber = true;
			}
			else
			{
				JOptionPane.showMessageDialog(mainTextArea, "Please enter an integer greater than 2, the smallest prime number.");
				endPrimeCalculator();
			}
		}
		catch( Exception ex )
		{
			JOptionPane.showMessageDialog(mainTextArea, "Exception with user input. Please enter an integer.");
			endPrimeCalculator();
		}
		
	}
	private void runPrimeCalculator()
	{
		if( inputIsNumber == true )
		{
			/*
			 * TO DO:
			 * make the semaphore for the multi-threading control
			 * make the for loop for determining the values to divide by a prime and sieve out the multiples
			 * replace the outputList variable with finalOutputList
			 * remove unnecessary instances of outputList
			 */
			outputList.clear();
			outputList = calculatePrimes(userNumber);
			// This prime calculator is based on the Sieve of Eratosthenes
			/*
			// This must be atomic, as accessed by multiple threads
			for( int x = 0; x < userNumber; x++ )
			{
				// This specific piece is the multi-threading, where the value of 2 must be changed to any new prime
				
				if( threadSafeList.get(x) % 2 == 0 )
				{
					// The .compareAndSet() method is atomic and therefore thread safe.
					threadSafeList.compareAndSet(x, x+1, 0);
				}
			}*/
		}
	}

	private void makeIntegerList()
	{
		// Instantiate the thread safe data structure using AtomicIntegerArray
		threadSafeList = new AtomicIntegerArray(userNumber);
		// The .set() method is not atomic, however, this loop need not be atomic, as it is done once by a single thread
		for( int x = 0; x < userNumber; x++ )
		{
			threadSafeList.set(x, x+1);
		}
	}
	
	private void createFinalOutput()
	{
		// This need not be atomic, as it is done at the end
		for( int x = 0; x < userNumber; x++)
		{
			int q = threadSafeList.get(x);
			if( q != 0)
			{
				finalOutputList.add(q);
			}
		}
		// Need to remove these, as they are for testing
		System.out.println(threadSafeList);
		System.out.println(finalOutputList);
		System.out.println(finalOutputList.size());
	}
	
	private void sieveOfEratosthenes()
	{
		finalOutputList.clear();
//		while( !calculationComplete )
//		{
			for( int x = 0; x < userNumber; x++)
			{
				int w = threadSafeList.get(x);
			//	System.out.println("Hi");
				if( w == 1 )
				{
					System.out.println("gas0");
					threadSafeList.getAndSet(x, 0);
				}
				else
				{
					for( int y = x+1; y <= userNumber; y++ )
					{
						int q = threadSafeList.get(y);
						if( q % w == 0)
						{
							System.out.println("hello");
							threadSafeList.getAndSet(y, 0);
						}
						/*int q = threadSafeList.get(y);
						if( x == y)
						{
							continue;
						}
						else if( q%w == 0 )
						{
							threadSafeList.getAndSet(y, 0);
						}
						else
						{
							continue;
						}*/
					}
	//			else if( w == 0 )
	//			{
	//				System.out.println("w==0");
	//				continue;
	//			}
			/*	else if( w == x+1 )
				{
					System.out.println("w==x+1");
					continue;
				}
				else //if( w < userNumber )
				{
					for( int y = x; y < userNumber; y+=w )
					{
						int r = threadSafeList.get(y);
						if( r == 0 )
						{
							System.out.println("r==0");
							continue;
						}
						else if( r/w == 1 )
						{
							System.out.println("r/w==1");
							continue;
						}
						else
						{
							threadSafeList.getAndSet(y, 0);
						}
					}*/
					/*for( int y = 2*x; y < userNumber; y+=w)
					{
						int q = threadSafeList.get(y);
						int z = q % w;
						if( q == 0 )
						{
							break;
						}
						else if( z == 0 )
						{
							threadSafeList.getAndSet(y, 30);
						}
					}*/
				
				}
		}
	}

	private List<Integer> calculatePrimes( Integer someNumber )
	{
		// The below code blocks are modified from Dr. Fodor's Programming 3, Lecture 12, Slide 10.
		List<Integer> primeList = new ArrayList<Integer>();
		primeList.add(2);
		calculationComplete = false;
		while( !calculationComplete )
		{
			for( int x = 1; x <= someNumber; x++)
			{
				primeList.add(x);
				for( int y : primeList)
				{
					if( x % y == 0) // If a number is evenly divisible by any prime, remove that number and break the loop.
					{
						primeList.remove(primeList.size() - 1);
						break;
					}
					else if( x / y == 1 )  // Else if it is divisible by itself, break the loop.
					{
						break;
					}
				}
				//System.out.println(primeList.size());
				int lastPrime = primeList.get(primeList.size() - 1);
				if( x == lastPrime)
				{
					//System.out.println("Most recent prime: " + primeList.get(primeList.size() - 1));
					calculatorTextArea.append("New prime " + primeList.get(primeList.size() - 1) + "\n");
					calculatorTextArea.requestFocusInWindow();
				}
			}
		calculationComplete = true;
		}
		return primeList;
	}
	
	private class PrimeCalculatorActionRunnable implements Runnable
	{
		public void run()
		{
			try
				{
					startTime = System.currentTimeMillis();
					runPrimeCalculator();
					sieveOfEratosthenes();
				}
				catch( Exception ex )
				{
					JOptionPane.showMessageDialog(mainTextArea, "Exception with PCAR.");
				}
			try
			{
				SwingUtilities.invokeAndWait( new Runnable()
				{
					public void run()
					{
						cancelButton.setEnabled(true);;
					}
				});
			}
			catch( Exception ex )
			{
				JOptionPane.showMessageDialog(mainTextArea, "Exception with invokeAndWait");
			}
			endPrimeCalculator();
		}
	}
	private PrimeCalculatorActionRunnable myPCAR = new PrimeCalculatorActionRunnable();
	
 	private class SaveActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			saveTextOutput();
		}
	}
	private SaveActionListener mySAL = new SaveActionListener();
	private void saveTextOutput()
	{
		// The below code blocks are modified from Dr. Fodor's Programming 3, Lecture 11, Slide 6
		JFileChooser myJFC = new JFileChooser();
		if( myJFC.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
			return;
		if( myJFC.getSelectedFile() == null)
			return;
		File saveStateFile = myJFC.getSelectedFile();
		if( myJFC.getSelectedFile().exists())
		{
			String promptExists = "The file " + myJFC.getSelectedFile().getName() + " already exists. Are you sure you want to overwrite?";
			if( JOptionPane.showConfirmDialog(this, promptExists) != JOptionPane.YES_OPTION)
				return;
		}
		try
		{
			BufferedWriter writeSave = new BufferedWriter(new FileWriter(saveStateFile));
			writeSave.write("Thank you for playing!\nThe number you chose was " + userNumber + "\nThe total number of primes found was " + this.outputList.size() + ".\nThis calculation took " + timeElapsed/1000f + " seconds.\nThe list of the calculated primes is below.\n" + outputList);
			writeSave.flush();
			writeSave.close();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Unable to write file", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private JMenuBar getLab8MenuBar()
	{
		JMenuBar labMenuBar = new JMenuBar();
		
		JMenu primeCalcMenu = new JMenu("Options");
		primeCalcMenu.setMnemonic('O');
		labMenuBar.add(primeCalcMenu);
		JMenuItem saveOutputItem = new JMenuItem("Save Text Output");
		primeCalcMenu.add(saveOutputItem);
		saveOutputItem.setMnemonic('S');
		saveOutputItem.addActionListener(mySAL);
		JMenuItem newNumberItem = new JMenuItem("Try New Number");
		primeCalcMenu.add(newNumberItem);
		newNumberItem.setMnemonic('N');
		newNumberItem.addActionListener(mySCAL);
		JMenuItem newCancelItem = new JMenuItem("Cancel Calculation");
		primeCalcMenu.add(newCancelItem);
		newCancelItem.setMnemonic('C');
		newCancelItem.addActionListener(myCBAL);
		return labMenuBar;
		
	}
	
	public Lab_Assignment_8()
	{
		super("Find Prime Numbers");
		try
		{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(mainTextArea, "Bad Look And Feel");
		}
		setSize(550,200);

		scrollArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(startButton, BorderLayout.SOUTH);
		startButton.addActionListener(mySCAL);
		// You must add the cancel action listener here and the run calculator action listener, without showing them
		cancelButton.addActionListener(myCBAL);
		inputTextField.addActionListener(myRCAL);
		getContentPane().add(mainTextArea, BorderLayout.CENTER);
		mainTextArea.setEditable(false);
		mainTextArea.setLineWrap(true);
		mainTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		mainTextArea.setText(directionPrompt);
		setJMenuBar(getLab8MenuBar());
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new Lab_Assignment_8();
	}
	
}
