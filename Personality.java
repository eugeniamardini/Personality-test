//Yauheniya Zapryvaryna
//CS 210
//Instructor: William Iverson
//Bellevue College
//November 11, 2013
//Revisions made November 12-17, 2013

/*The interactive program Personality is created to process the existing Myers-Briggs 
 * personality test results, as well as to allow the user to complete the test 
 * and find out his/her personality type. 
 * While reading the existing personality.txt file, the program stores specified data in 
 * the output.txt file.*/
import java.io.*;
import java.util.*;
public class Personality {
	//creating a constant, so it can be accessed from anywhere in the program and used
	//multiple times
	public static final int DIMENSIONS = 4;
	
	public static void main(String[] args) 
	    //stating that main{} can generate this exception, if unable to locate the file
	        throws FileNotFoundException {
		intro();
		Scanner console = new Scanner (System.in);
		//constructing a PrintStream object to send the output to the file specified below.
        PrintStream out = new PrintStream(new File ("output.txt"));
        //calling menu() method to make the program interactive and let the user choose the next action
        int option = menu(console);
        //switch statement created to follow "the path", specified by user
		switch (option) {
		    case 1:
			    Scanner user = new Scanner(System.in);
			//"trying" the code for any exceptions
			    try {
			        Scanner survey = new Scanner(new File("survey.txt"));
				    takeSurvey(user, survey, out);
				  
			        System.out.println();
			    //catch block will execute if survey.txt is not found
			    } catch (FileNotFoundException e){
			        System.out.println("Error reading file"+e);
				}
			    break;
			case 2:
			    Scanner file = new Scanner (System.in);
			    //calling process() method to get user's input of the file he/she wants to access
		        String fileName = process(file);
		        //try-catch block to check for exceptions, while executing personalityTest() method
			    try {
				    Scanner input = new Scanner (new File(fileName));
				    personalityTest(input,console,out);
			    } catch (FileNotFoundException e){
				    System.out.println("Error reading file "+e);
				}
			    break;
		    default:
				System.out.println("Program exited");//exit program
				break;
		}
	}
	public static void intro() {//introduction for the user
		System.out.println("The classic Myers-Briggs personality test"
                + " measures four dimensions of your personality:"
                + "\n1. Extrovert versus Introvert (E vs. I): what energizes you"
                + "\n2. Sensation versus iNtuition (S vs. N): what you focus on"
                + "\n3. Thinking versus Feeling (T vs. F): how you interpret what you focus on"
                + "\n4. Judging versus Perceiving (J vs. P): how you approach life");
        System.out.println("There are a total of 70 questions, all can be answered"
                + " with A, B, or - (if there is no answer)."
        		+"\nIndividuals are categorized as being on one side or the other for each dimension."
                +"\nThe corresponding letters are put together to form a personality type.");
	}

	public static int menu (Scanner console) {
		//checking if user enters an integer 
		try{
			System.out.println("1. New Survey\n2. Process existing personality test\n3. Exit the program");
			int option = console.nextInt();
			return option;
			//if input is not an integer - Error message shows and program exits.
		} catch (InputMismatchException e) {
			System.out.println("Error!"+e);
		}
	//if input is something other than 1,2,3, the method returns 3 to exit program
	return 3;
	}
	//method takeSurvey() created to get user's name and answers to all 70 questions of the survey,
	//as well as to output the received data to a file output.txt
	public static void takeSurvey (Scanner user, Scanner survey, PrintStream out){
		System.out.println("Enter your name, please:");
		String userName = user.next();
		String userAnswer ="";
		while(survey.hasNextLine()) {//testing for further data content in a file survey.txt
		    for (int k=0;k<5;k++){//4 lines for survey question, 1 line for user input
				System.out.println(survey.nextLine());
			}
		    Scanner answer = new Scanner(System.in);
	        userAnswer += answer.next().toUpperCase();//concatenating string: 
	        //adding answer by answer to the initially empty string (turning each to Uppercase)
		}
	    System.out.println(userName + ":");
		out.println(userName+":");
		System.out.print(userAnswer);
        out.print(userAnswer);
        //calling count(), which does the computation for the user's input
		count(userAnswer);
		menu(user);
	}
	//creating count() method to do all necessary computations
	public static void count (String userAnswer){
        String interval; 
		int start = 0;
		int []countA = new int [DIMENSIONS];//initializing 2 arrays with four indexes for each personality dimension
		int []countB = new int [DIMENSIONS];
        char [] typesA = {'E','S', 'T','J'};//arrays to hold values (representing characters) for each personality type
		char [] typesB = {'I', 'N','F','P'};
		//user's input is a 70-character long string, so we can't start a substring with a value bigger than 70
		while (start<70) {
			 interval =userAnswer.substring(start,start+7);
			 //questions are organized like:1223344, therefore the A and B count can be done by going through each char of substring
			 for (int i = 0; i<interval.length();i++) {
				 switch (interval.charAt(i)) {
				     case 'A':
					     countA [(i+1)/2]++;//formula, i came up with for calculating A's and B's
					     break;
				     case 'B':
					     countB [(i+1)/2]++;
					     break;
				     default://if answer is not A or B - program simply skips it in computations
						 break;
						 }
				 }  start+=7;
	    }
		System.out.print("\t");
		for (int j=0; j<DIMENSIONS;j++)//for-loop to print all counts for A's and B's for each question category
				System.out.print(countA[j]+"A-"+countB[j]+"B ");
		int [ ] percents = new int [DIMENSIONS]; //4 index long array for percents of B's in each category
		int total=0;
		char type;
		try {
			total=countA[0]	+ countB[0];
			percents[0]=(countB[0]*100/total);
			System.out.print("\n\t["+percents[0]+"%");//"fence post"
			for (int i =1;i<DIMENSIONS;i++) {//looping to calculate and print the percentage of B's for each category
				total=countA[i]	+ countB[i];
				percents[i]=(countB[i]*100/total);
				System.out.print(", "+percents[i]+"%");
			}
			System.out.print("]=");
			
			for (int i =0;i<DIMENSIONS;i++) {//for-loop to print the right personality type, based on the number of A's and B's 
				if(countA[i]>countB[i]) { //if #of A's bigger than #ofB's, it's guaranteed that it's more than 50%
					type=typesA[i];
					}
				else if (countB[i]>countA[i]) {//if # of B's larger than #ofA's, it's guaranteed that #ofB's more than 50%
					//therefore, no need for a longer loop using percentages
					type=typesB[i];
					}
				else {// # of A's = # of B's - 
					type='X';
				}
				System.out.print(type);
			}
			System.out.println();
			} catch(ArithmeticException e){//if all answers are invalid 
				System.out.println("\nError calculating the result."
						+"(You have not answered the quesions, using A, B or -(dash))"+e);
		}
	}
	public static String process (Scanner file) {
		System.out.println("Enter the desired file name for processing:");
		String fileName = file.nextLine();
		// checking if the user entered .txt extension or not, and returning
		//the complete name of the file user wants to access
		if(fileName.endsWith(".txt")) {
			File f = new File (fileName);
		}
		else {
			fileName+=".txt";
		}
		return fileName;
	}
	public static void personalityTest (Scanner input, Scanner console, PrintStream out){
		String userName, userAnswer;
		while(input.hasNextLine()) {//testing for further data content in a file survey.txt
		    for (int h=0;h<9;h++){	
                userName = input.nextLine();
			    System.out.print(userName + ": \n");
			    out.println(userName + ":");
                userAnswer = input.nextLine().toUpperCase();
			    out.print(userAnswer+"\n");
			    //calling count(), which does the computation for the personality.txt 
			    count(userAnswer);
			}
		}
		menu(console);//calling menu() for user to choose next step of action
	}
}
	
	
		
			
		
			
	


	
	
	
	




  