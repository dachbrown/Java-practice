package lab7;

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
import javax.swing.JScrollPane;
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
	private JTextArea calculatorTextArea = new JTextArea();
	private JScrollPane scrollArea = new JScrollPane(calculatorTextArea);
	private JTextField inputTextField = new JTextField();
	private boolean inputIsNumber = false;
	private String userInput = "";
	private int userNumber = 0;
	private long startTime = 0;
	private long endTime = 0;
	private long timeElapsed = 0;
	private volatile boolean calculationComplete = false;
	private List<Integer> outputList = new ArrayList<Integer>();
	private String directionPrompt = "Welcome to the Prime Number Calculator! Click below to get started!";
	private String continuePrompt = "Click below to start again!";
	private String endingPrompt = "";
	
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
		myCalculatorThread = null;
		myCalculatorThread = new Thread(myPCAR);
		try
		{
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
			System.out.println("Exception with cancelling calculator");
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
		getContentPane().remove(mainTextArea);
		getContentPane().add(scrollArea, BorderLayout.CENTER);
		setVisible(true);
		repaint();
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
				//System.out.println("Most recent prime: " + primeList.get(primeList.size() - 1));
				calculatorTextArea.append("New prime " + primeList.get(primeList.size() - 1) + "\n");
				calculatorTextArea.requestFocusInWindow();
			}
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
			endPrimeCalculator();
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

		scrollArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
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
