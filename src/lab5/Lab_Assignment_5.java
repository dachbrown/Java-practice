package lab5;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

public class Lab_Assignment_5 extends JFrame
{
	private static final long serialVersionUID = 3794059922116119530L;
	private int numAttempts =0;
	private int bestAttempt =0;
	private int correctButton =0;
	private String welcomePrompt = "Click the right button to win the game!";
	private JTextField mainTextField = new JTextField();
	private List<String> buttonNames = Arrays.asList("Click here!", "What about here!", "Could be here!");
	private JButton firstButton = new JButton("Click here!");
	private JButton secondButton = new JButton("What about here!");
	private JButton thirdButton = new JButton("Could be here!");
	private JButton[] buttonsArray = 
	{
			firstButton, secondButton, thirdButton
	};
	
	private class WinnerActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			numAttempts ++;
			if( numAttempts < bestAttempt && bestAttempt != 0)
			{
				bestAttempt = numAttempts;
			}
			else if( bestAttempt == 0)
			{
				bestAttempt = numAttempts;
			}
			winnerTextField();
			continueGame();
		}
	}
	
	private void winnerTextField()
	{
		mainTextField.setText("Congratulations! It took you " + numAttempts + " attempts! Your best score is " + bestAttempt + "! Click below to keep playing!");
	}
	
	private WinnerActionListener myWAL = new WinnerActionListener();
	
	private class IncorrectActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			numAttempts ++;
			incorrectTextField();
			updateContent();
		}
	}
	
	private String[] incorrectPrompts =
	{
			"That is incorrect, please try again!",
			"Sorry, that is wrong, give it another shot!",
			"No, that is not right, do over?"		
	};
	
	private void incorrectTextField()
	{
		Random random = new Random();
		int myPrompt = random.nextInt(incorrectPrompts.length);
		mainTextField.setText(incorrectPrompts[myPrompt]);
	}

	private IncorrectActionListener myIAL = new IncorrectActionListener();
	
	private JPanel getLowerInterface(JButton[] j)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, j.length));
		for( int x=0; x < j.length; x++)
		{
			j[x].setBorder(new LineBorder(Color.BLACK));
			panel.add(j[x]);
		}
		return panel;
	}
	
	private JButton[] conditionAdd(JButton[] b)
	{
		Random random = new Random();
		correctButton = random.nextInt(b.length);
		for( int x=0; x<b.length; x++)
		{
			if( x==correctButton )
			{
				b[x].addActionListener(myWAL);
			}
			else
			{
				b[x].addActionListener(myIAL);
			}
		}
		return b;
	}
	
	private JButton[] conditionRemove(JButton[] b)
	{
		for( int x=0; x<b.length; x++)
		{
			ActionListener[] theseListeners = b[x].getActionListeners();
			for( int y=0; y<theseListeners.length; y++)
			{
				if( theseListeners[y] == myWAL)
				{
					b[x].removeActionListener(myWAL);
				}
				else
				{
					b[x].removeActionListener(myIAL);
				}
			}
		}
		return b;
	}
	
	private JButton[] shuffleNames()
	{
		Collections.shuffle(buttonNames);
		String[] shuffledButtons = buttonNames.toArray(new String[0]);
		for( int x=0; x<buttonsArray.length; x++)
		{
			buttonsArray[x].setText(shuffledButtons[x]);
		}
		return buttonsArray;
	}
	
	private void updateContent()
	{
		conditionRemove(buttonsArray);
		conditionAdd(buttonsArray);
		shuffleNames();
		validate();
	}
	
	private void continueGame()
	{
		numAttempts =0;
		conditionRemove(buttonsArray);
		conditionAdd(buttonsArray);
		validate();
	}
	
	private class SaveActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			saveCurrentState();
		}
	}
	
	private SaveActionListener mySAL = new SaveActionListener();
	
	private class NewGameActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			bestAttempt =0;
			mainTextField.setText(welcomePrompt);
		}
	}
	
	private NewGameActionListener myNGAL = new NewGameActionListener();
	
	private class OpenActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			
			loadSaveState();
			mainTextField.setText("Your current best is " + bestAttempt + ". Click below to try to beat it!");
		}
	}
	
	private OpenActionListener myOAL = new OpenActionListener();
	
	private JMenuBar getLab5MenuBar()
	{
		JMenuBar labMenuBar = new JMenuBar();
		
		JMenu gameMenu = new JMenu("Game Options");
		gameMenu.setMnemonic('G');
		labMenuBar.add(gameMenu);
		JMenuItem saveState = new JMenuItem("Save");
		gameMenu.add(saveState);
		saveState.setMnemonic('S');
		saveState.addActionListener(mySAL);
		JMenuItem openState = new JMenuItem("Open");
		gameMenu.add(openState);
		openState.setMnemonic('O');
		openState.addActionListener(myOAL);
		JMenuItem newGame = new JMenuItem("New Game");
		gameMenu.add(newGame);
		newGame.setMnemonic('N');
		newGame.addActionListener(myNGAL);
		
		return labMenuBar;
		
	}
	
	private void saveCurrentState()
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
			writeSave.write(this.bestAttempt+ "\n");
			writeSave.flush();
			writeSave.close();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Unable to write file", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void loadSaveState()
	{
		// The below code blocks are modified from Dr. Fodor's Programming 3, Lecutre 11, Slide 13
		JFileChooser myJFC = new JFileChooser();
		if( myJFC.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
			return;
		if( myJFC.getSelectedFile() == null)
			return;
		File saveStateFile = myJFC.getSelectedFile();
		BufferedReader readSave = null;
		try
		{
			readSave = new BufferedReader(new FileReader(saveStateFile));
			String line = readSave.readLine();
			if( line == null || readSave.readLine() != null)
			{
				throw new Exception("Incorrect file format");
			}
			try
			{
				this.bestAttempt = Integer.parseInt(line);		
			}
			catch(Exception ex)
			{
				throw new Exception("Incorrect file format");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Unable to read file", JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			if( readSave != null)
			{
				try
				{
					readSave.close();
				}
				catch(Exception ex)
				{
					System.out.println(ex);
				}
			}
		}
	}
	
	public Lab_Assignment_5()
	{
		super("Shell Game: Click the Button to Win!");
		try
		{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
		catch(Exception ex)
		{
			System.out.println("Bad Look And Feel");
		}
		setSize(600,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getLowerInterface(buttonsArray), BorderLayout.SOUTH);
		conditionAdd(buttonsArray);
		getContentPane().add(mainTextField, BorderLayout.CENTER);
		mainTextField.setText(welcomePrompt);
		setJMenuBar(getLab5MenuBar());
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new Lab_Assignment_5();
	}
}
