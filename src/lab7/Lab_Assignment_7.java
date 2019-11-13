package lab7;
/**
 * Current issues:
 * 		When cancelled, the list of primes will not print.
 * 			Need to change the method to return a list or something
 * 		Need to update the JPanel to print the outputs		
 * 			Need to change where the endPrimeCalculator fires
 * 		Need to change all the game options menus
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Lab_Assignment_7 extends JFrame
{
	private static final long serialVersionUID = 3794059922116119530L;
	private JButton cancelButton = new JButton("Cancel");
	private JButton startButton = new JButton("Start");
	private JTextArea mainTextArea = new JTextArea();
	private JTextField inputTextField = new JTextField();
	private boolean inputIsNumber = false;
	private String userInput = "";
	private int userNumber = 0;
	private long startTime = 0;
	private long endTime = 0;
	private long timeElapsed = 0;
	private volatile boolean calculationComplete = false;
	private String directionPrompt = "Welcome to the Prime Number Calculator! Click below to get started!";
	//private List<Integer> primeList = new ArrayList<Integer>();
	private List<Integer> outputList = new ArrayList<Integer>();
	
	
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
		myCalculatorThread.stop();
		myCalculatorThread = null;
		myCalculatorThread = new Thread(myPCAR); 
	//	calculationComplete = true;
		
		try
		{
			endTime = System.currentTimeMillis();
			System.out.println(outputList);
			timeElapsed = endTime - startTime;
			inputIsNumber = false;
			//calculationComplete = false;
			inputTextField.setEditable(true);
			inputTextField.setText("");
			//update main text field with the last calculated prime, total number primes found, the number working towards, and overall time
			mainTextArea.setText("Total time: " + timeElapsed/1000f + "\n");
			getContentPane().remove(inputTextField);
			getContentPane().remove(cancelButton);
			getContentPane().add(startButton, BorderLayout.SOUTH);
		}
		catch( Exception ex )
		{
			System.out.println("Exception with cancelling calculator");
		}
		calculationComplete = false;
		mainTextArea.setText(directionPrompt);
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
		//outputList.clear();
		try
		{
			getContentPane().remove(startButton);
			getContentPane().add(cancelButton, BorderLayout.NORTH);
			getContentPane().add(inputTextField, BorderLayout.SOUTH);
			inputTextField.addActionListener(myRCAL);
		}
		catch( Exception ex )
		{
			System.out.println("Exception with starting calculator");
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
			checkUserInput();
			if( myCalculatorThread.getState().equals(Thread.State.NEW) )
			{
				myCalculatorThread.start();
			}
			else
			{
				myCalculatorThread.resume();
			}
		}
	}
	private RunCalculatorActionListener myRCAL = new RunCalculatorActionListener();
	private void checkUserInput()
	{
		userInput = inputTextField.getText();
		inputTextField.setEditable(false);
		mainTextArea.requestFocusInWindow();
		try
		{
			userNumber = Integer.valueOf(userInput);
			inputIsNumber = true;
		}
		catch( Exception ex )
		{
			System.out.println("Exception with user input");
		}
		
	}
	private void runPrimeCalculator()
	{
		if( inputIsNumber == true )
		{
			outputList.clear();
			outputList = calculatePrimes(userNumber);
		}
	}
	private List<Integer> calculatePrimes( Integer someNumber )
	{
		// The below code blocks are modified from Dr. Fodor's Programming 3, Lecture 12, Slide 10.
		List<Integer> primeList = new ArrayList<Integer>();
		primeList.add(2);
		if( calculationComplete )
		{
			return primeList;
		}
		for( int x = 1; x <= someNumber && !calculationComplete; x++)
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
				System.out.println("Most recent prime: " + primeList.get(primeList.size() - 1));
			}
		}
		//outputList = primeList;
		return primeList;
		//primeList = null;
	}
	
	private class PrimeCalculatorActionRunnable implements Runnable
	{
		public void run()
		{
			try
				{
					startTime = System.currentTimeMillis();
					runPrimeCalculator();
				}
				catch( Exception ex )
				{
					System.out.println("Exception with PCAR");
				}
				finally
				{
					calculationComplete = true;
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
				System.out.println("Exception with invokeAndWait");
			}
		}
	}
	private PrimeCalculatorActionRunnable myPCAR = new PrimeCalculatorActionRunnable();
	private Thread myCalculatorThread = new Thread(myPCAR);
	
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
		
		//Adjust the below to account for the new output types
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
			// Need to adjust the below to print the entire text area
			writeSave.write("Thank you for playing!");
			writeSave.flush();
			writeSave.close();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Unable to write file", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private JMenuBar getLab7MenuBar()
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
	
	public Lab_Assignment_7()
	{
		super("Find Prime Numbers");
		try
		{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
		catch(Exception ex)
		{
			System.out.println("Bad Look And Feel");
		}
		setSize(550,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(startButton, BorderLayout.SOUTH);
		startButton.addActionListener(mySCAL);
		// You must add the cancel action listener here, without showing it
		cancelButton.addActionListener(myCBAL);
		getContentPane().add(mainTextArea, BorderLayout.CENTER);
		mainTextArea.setEditable(false);
		mainTextArea.setLineWrap(true);
		mainTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		mainTextArea.setText(directionPrompt);
		setJMenuBar(getLab7MenuBar());
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new Lab_Assignment_7();
	}
	
}
