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
import java.util.concurrent.Semaphore;
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
	private String directionPrompt = "Welcome to the Prime Number Calculator! Click below to get started!";
	private String continuePrompt = "Click below to start again!";
	private String endingPrompt = "";
	private AtomicIntegerArray threadSafeList;
	private List<Integer> finalOutputList = new ArrayList<Integer>();
	private int numThreads = 8;
	private Semaphore sem = new Semaphore(numThreads);
	
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
			timeElapsed = endTime - startTime;
			endingPrompt = "This calculation took " + timeElapsed/1000f + " seconds.\nThe number of primes found was " + finalOutputList.size() + ".\n";
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
			finalOutputList.clear();
			// Validate user input
			checkUserInput();
			// Create a list of integers from 1 to the user input as a thread safe data structure
			makeIntegerList();
			// Launch the manager thread
			new Thread(mySCAR).start();
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
		/*
		// Need to remove these, as they are for testing
		System.out.println(threadSafeList);
		System.out.println(finalOutputList);
		System.out.println(finalOutputList.size());
		*/
	}
	
	private void sieveOfEratosthenes( int x )
	{
		// All multiples and the number 1 are set to 0 for easy removal later. The method .getAndSet() is atomic.
		while( !calculationComplete )
		{
			int w = threadSafeList.get(x);
			if( w == 1 )
			{
				threadSafeList.getAndSet(x, 0);
			}
			else if( w == 0)
			{
				continue;
			}
			else
			{
				for( int y = 0; y < userNumber; y++ )
				{
					int q = threadSafeList.get(y);
					if( q / w != 1 && q % w == 0 )
					{
						threadSafeList.getAndSet(y, 0);
					}
				}
			}
		}
	}

	private class StartCalculatorActionRunnable implements Runnable
	{
		public void run()
		{
			try
			{
				sem.acquire();
				startTime = System.currentTimeMillis();
				for( int x = 0; x < userNumber; x++)
				{
					new Thread(new SOEActionRunnable(x)).start();
				}
				endPrimeCalculator();
			}
			catch( Exception ex )
			{
				JOptionPane.showMessageDialog(mainTextArea, "Exception with SCAR.");
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
				JOptionPane.showMessageDialog(mainTextArea, "Exception with invokeAndWait, SCAR");
			}
		}
	}
	private StartCalculatorActionRunnable mySCAR = new StartCalculatorActionRunnable();
	
	private class SOEActionRunnable implements Runnable
	{
		private final int x;
		private SOEActionRunnable(int x)
		{
			this.x = x;
		}
		public void run()
		{
			try
			{
				sieveOfEratosthenes(x);
				sem.release();
			}
			catch( Exception ex )
			{
				JOptionPane.showMessageDialog(mainTextArea, "Exception with SOEAR.");
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
				JOptionPane.showMessageDialog(mainTextArea, "Exception with invokeAndWait, SOEAR");
			}
		}
	}
	
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
			writeSave.write("Thank you for playing!\nThe number you chose was " + userNumber + "\nThe total number of primes found was " + this.finalOutputList.size() + ".\nThis calculation took " + timeElapsed/1000f + " seconds.\nThe list of the calculated primes is below.\n" + finalOutputList);
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
