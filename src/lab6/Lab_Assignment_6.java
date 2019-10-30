package lab6;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

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
import javax.swing.Timer;

//The major remaining issues are the timer (myEQAL and TAR)

public class Lab_Assignment_6 extends JFrame
{
	private static final long serialVersionUID = 3794059922116119530L;
	private int numCorrect =0;
	private int numIncorrect =0;
	private boolean quizRunning =false;
	private long startTime =0;
	private long endTime =0;
	private long timeRemaining =0;
	private final static int oneSecond =1000;
	private JTextField answerTextField = new JTextField();
	private JTextArea mainTextArea = new JTextArea();
	private JTextArea timerTextArea = new JTextArea();
	private JTextArea scoreTextArea = new JTextArea();
	private JButton cancelButton = new JButton("Click Here to Cancel Quiz!");
	private JButton startQuizButton = new JButton("Begin Quiz!");
	private String directionPrompt = "\n\tType the single letter which represents each amino acid.\n\n\tClick the button when you are ready to begin!";
	private String userAnswer = "";
	private String correctAnswer = "";
	
	private class UpdateTimerActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			SwingUtilities.invokeLater( new Runnable()
			{
				//This was added with Dr. Fodor's help to make certain that the timer is added to a different thread
				@Override
				public void run() 
				{
					timeRemaining = endTime - System.currentTimeMillis();
					if( timeRemaining < 0)
					{
						endQuiz();
					}
					timerTextArea.setText("Timer: " + timeRemaining/1000 + " seconds");
					repaint();
				}
			});
		}
	}
	private UpdateTimerActionListener myUTAL = new UpdateTimerActionListener();

	Timer timer = new Timer(oneSecond, myUTAL);
	
	public static String[] shortNames = 
	{
		"A","R", "N", "D", "C", "Q", "E", 
		"G",  "H", "I", "L", "K", "M", "F", 
		"P", "S", "T", "W", "Y", "V"
	};
	public static String[] fullNames = 
	{
		"alanine","arginine", "asparagine", 
		"aspartic acid", "cysteine",
		"glutamine",  "glutamic acid",
		"glycine" ,"histidine","isoleucine",
		"leucine",  "lysine", "methionine", 
		"phenylalanine", "proline", 
		"serine","threonine","tryptophan", 
		"tyrosine", "valine"
	};
	
	private class BeginQuizActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			numCorrect =0;
			numIncorrect =0;
			runQuiz();
		}
	}
	private BeginQuizActionListener myBQAL = new BeginQuizActionListener();
	
	private void runQuiz()
	{
		Random random = new Random();
		int r = random.nextInt(fullNames.length);
		String questionFullName = fullNames[r];
		correctAnswer = shortNames[r];
		if( quizRunning == false)
		{	
			try
			{
				getContentPane().remove(startQuizButton);
				getContentPane().add(cancelButton, BorderLayout.NORTH);
				cancelButton.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
				cancelButton.addActionListener(myCBAL);
				getContentPane().add(answerTextField, BorderLayout.SOUTH);
				answerTextField.addActionListener(myCAAL);
				getContentPane().add(timerTextArea, BorderLayout.EAST);
				timerTextArea.setEditable(false);
				timerTextArea.setLineWrap(true);
				timerTextArea.setText("You will have\n30 seconds.");
				getContentPane().add(scoreTextArea, BorderLayout.WEST);
				scoreTextArea.setEditable(false);
				scoreTextArea.setLineWrap(true);
				}
			catch( Exception ex )
			{
				System.out.println("Exception with starting quiz: layout");
			}
			quizRunning = true;
		}
		mainTextArea.setText("\n\tType the one letter code for: \n\n\t   " + questionFullName);
		scoreTextArea.setText("  Total Score" + "\n Correct: " + numCorrect + "\n Incorrect: " + numIncorrect);
		setVisible(true);
		repaint();
		answerTextField.requestFocusInWindow();
	}
	
	private class CheckAnswerActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			checkAnswer();
		}
	}
	private CheckAnswerActionListener myCAAL = new CheckAnswerActionListener();
	
	private void checkAnswer()
	{
		userAnswer = answerTextField.getText().toUpperCase();
		answerTextField.setText("");
		if( userAnswer.equals(correctAnswer) == true )
		{
			numCorrect++;
		}
		else if( userAnswer.equals(correctAnswer) == false )
		{
			numIncorrect++;
		}
		runQuiz();
	}
	
	private class CancelButtonActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			endQuiz();
		}
	}
	private CancelButtonActionListener myCBAL = new CancelButtonActionListener();

	private class TimerActionRunnable implements Runnable
	{
		public void run()
		{
			try
			{
				startTime = System.currentTimeMillis();
				endTime = startTime + 30000;
				timer.start();
			}
			catch(Exception ex)
			{
				System.out.println("Exception in Timer");
			}
			startQuizButton.setEnabled(true);
			cancelButton.setEnabled(true);
		}
	}
	private TimerActionRunnable myTAR = new TimerActionRunnable();
	
	private class TimerActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			startQuizButton.setEnabled(false);
			cancelButton.setEnabled(true);
			new Thread(myTAR).start();
		}
	}
	private TimerActionListener myTAL = new TimerActionListener();
	
	private void endQuiz()
	{
		timer.stop();
		quizRunning = false;
		answerTextField.removeActionListener(myCAAL);
		getContentPane().remove(answerTextField);
		getContentPane().remove(timerTextArea);
		getContentPane().remove(scoreTextArea);
		cancelButton.removeActionListener(myCBAL);
		getContentPane().remove(cancelButton);
		mainTextArea.setText("Your final score is " + numCorrect + " correct and " + numIncorrect + " incorrect.\nThank you for playing!\nClick below to start a new quiz!");
		getContentPane().add(startQuizButton, BorderLayout.SOUTH);
		setVisible(true);
		repaint();
	}
	
	private class SaveActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			saveCurrentState();
		}
	}
	private SaveActionListener mySAL = new SaveActionListener();
	
	private JMenuBar getLab6MenuBar()
	{
		JMenuBar labMenuBar = new JMenuBar();
		
		JMenu gameMenu = new JMenu("Options");
		gameMenu.setMnemonic('O');
		labMenuBar.add(gameMenu);
		JMenuItem saveScore = new JMenuItem("Save Score");
		gameMenu.add(saveScore);
		saveScore.setMnemonic('S');
		saveScore.addActionListener(mySAL);
		JMenuItem newGame = new JMenuItem("New Game");
		gameMenu.add(newGame);
		newGame.setMnemonic('N');
		newGame.addActionListener(myCBAL);
		
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
			writeSave.write("Total Score\n\nCorrect: " + this.numCorrect + "\nIncorrect: " + this.numIncorrect + "\nThank you for playing!");
			writeSave.flush();
			writeSave.close();
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Unable to write file", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public Lab_Assignment_6()
	{
		super("Amino Acid Quiz: How Many do You Know?");
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
		getContentPane().add(startQuizButton, BorderLayout.SOUTH);
		startQuizButton.addActionListener(myBQAL);
		startQuizButton.addActionListener(myTAL);
		getContentPane().add(mainTextArea, BorderLayout.CENTER);
		mainTextArea.setEditable(false);
		mainTextArea.setLineWrap(true);
		mainTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		mainTextArea.setText(directionPrompt);
		setJMenuBar(getLab6MenuBar());
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new Lab_Assignment_6();
	}
}
