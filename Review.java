import java.util.Scanner;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;

/**
 * Class that contains helper methods for the Review Lab
 **/
public class Review {
  
  private static HashMap<String, Double> sentiment = new HashMap<String, Double>();
  private static ArrayList<String> posAdjectives = new ArrayList<String>();
  private static ArrayList<String> negAdjectives = new ArrayList<String>();
 
  
  private static final String SPACE = " ";
  
  static{
    try {
      Scanner input = new Scanner(new File("cleanSentiment.csv"));
      while(input.hasNextLine()){
        String[] temp = input.nextLine().split(",");
        sentiment.put(temp[0],Double.parseDouble(temp[1]));
        //System.out.println("added "+ temp[0]+", "+temp[1]);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing cleanSentiment.csv");
    }
  
  
  //read in the positive adjectives in postiveAdjectives.txt
     try {
      Scanner input = new Scanner(new File("positiveAdjectives.txt"));
      while(input.hasNextLine()){
        String temp = input.nextLine().trim();
        System.out.println(temp);
        posAdjectives.add(temp);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing postitiveAdjectives.txt\n" + e);
    }   
 
  //read in the negative adjectives in negativeAdjectives.txt
     try {
      Scanner input = new Scanner(new File("negativeAdjectives.txt"));
      while(input.hasNextLine()){
        negAdjectives.add(input.nextLine().trim());
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing negativeAdjectives.txt");
    }   
  }
  
  /** 
   * returns a string containing all of the text in fileName (including punctuation), 
   * with words separated by a single space 
   */
  public static String textToString( String fileName )
  {  
    String temp = "";
    try {
      Scanner input = new Scanner(new File(fileName));
      
      //add 'words' in the file to the string, separated by a single space
      while(input.hasNext()){
        temp = temp + input.next() + " ";
      }
      input.close();
      
    }
    catch(Exception e){
      System.out.println("Unable to locate " + fileName);
    }
    //make sure to remove any additional space that may have been added at the end of the string.
    return temp.trim();
  }
  
  /**
   * @returns the sentiment value of word as a number between -1 (very negative) to 1 (very positive sentiment) 
   */
  public static double sentimentVal( String word )
  {
    try
    {
      return sentiment.get(word.toLowerCase());
    }
    catch(Exception e)
    {
      return 0;
    }
  }
  
  /**
   * Returns the ending punctuation of a string, or the empty string if there is none 
   */
  public static String getPunctuation( String word )
  { 
    String punc = "";
    for(int i=word.length()-1; i >= 0; i--){
      if(!Character.isLetterOrDigit(word.charAt(i))){
        punc = punc + word.charAt(i);
      } else {
        return punc;
      }
    }
    return punc;
  }
  
   /**
   * Returns the word after removing any beginning or ending punctuation
   */
  public static String removePunctuation( String word )
  {
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(0)))
    {
      word = word.substring(1);
    }
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(word.length()-1)))
    {
      word = word.substring(0, word.length()-1);
    }
    
    return word;
  }
  
  /** 
   * Randomly picks a positive adjective from the positiveAdjectives.txt file and returns it.
   */
  public static String randomPositiveAdj()
  {
    int index = (int)(Math.random() * posAdjectives.size());
    return posAdjectives.get(index);
  }
  
  /** 
   * Randomly picks a negative adjective from the negativeAdjectives.txt file and returns it.
   */
  public static String randomNegativeAdj()
  {
    int index = (int)(Math.random() * negAdjectives.size());
    return negAdjectives.get(index);
    
  }
  
  /** 
   * Randomly picks a positive or negative adjective and returns it.
   */
  public static String randomAdjective()
  {
    boolean positive = Math.random() < .5;
    if(positive){
      return randomPositiveAdj();
    } else {
      return randomNegativeAdj();
    }
  }
  
  public static double totalSentiment(String fileName)
  {
      //open file
      //pick each word; when i find a space, all the stuff before was a word BUT I've got to remove punctuation.
         //Go throguh each character to look for spaces, add letters to word if not a space
         //If space
            //remove punctuation
            //get sentiment value nad add to total sentiment value
            //reset word to blank.
         //Else, add letter to word 
      //return value
   
      //Build Word
      String file = textToString(fileName);
      String word = "";
      String space = " ";
      double totalVal = 0.0;
      for(int i = 0; i < file.length(); i++)
         {
            String str = file.substring(i, i+1);
            if(str.equals(space) || i + 1 == file.length())
            {
               totalVal += sentimentVal(removePunctuation(word));
               word = "";
            }
            else
            {
               word += str;
            }
         } 
      return totalVal;
  }
  
  public static int starRating(String fileName)
  {
    // get total sentiment
    // if sentiment value is certain amount give star
     int totalSentiment = (int) totalSentiment(fileName);

     if(totalSentiment < 0)
     {
        return 1;
     }
     else if(totalSentiment < 5)
     {
        return 2;
     }
     else if(totalSentiment < 15)
     {
        return 3;
     }
     else
     {
        return 4;
     }

  }

  public static String fakeReview(String fileName)
  {
      //Turns file into string
      String review = textToString(fileName);
      //Index of start and end  of word to be replaced that will go into substring
      int replaceWordStart = 0;
      int replaceWordEnd = 0;

      String wordToReplace = "";

      //Iterates through whole string to find "*".
      wholeReview: for(int i = 0; i < review.length(); i++)
      {
         String str = review.substring(i, i+1);
         
         if(str.equals("*"))
         {
            //First index that will find substring.
            replaceWordStart = review.indexOf("*");
            
            //Iterates though rest of string until it finds the end of the word.
            starredAdj: for(int j = replaceWordStart; j < review.length(); j++)
            {
                  String strAdj = review.substring(j, j+1);
                  
                  //looks for space to signify end of word, or if it can't find a space, the end of the string. 
                  if(strAdj.equals(" "))
                  {
                     replaceWordEnd = j;
                     break;
                  }
                  else
                  {
                     replaceWordEnd = review.length();
                  }
            }
            //Takes splits string into part before word and after, and add random adjective between.
            review = review.substring(0, replaceWordStart) + randomAdjective() + review.substring(replaceWordEnd);
          }
      }
      return review;  
  }
  
  public static String fakeReviewStronger(String fileName)
  {
   String reveiw = textToString(fileName);
   String word = "";
   String newAdjective = "";
   String fakeReview = "";
   boolean asteriskDetected = false;
   
   for (int i = 0; i < reveiw.length(); i++)
   {
      String str = reveiw.substring(i, i+1);
      if (str.equals("*"))
      {
         asteriskDetected = true;
      }
      
      else if (str.equals(" ") && asteriskDetected)
      {
         while (true)
         {
            newAdjective = randomAdjective();
            if ((totalSentiment(fileName) > 0) && (sentimentVal(newAdjective) > sentimentVal(word)) )
            {
               break;
            }
            else if ((totalSentiment(fileName) < 0) && (sentimentVal(newAdjective) < sentimentVal(word)) )
            {
               break;
            }
            else if (totalSentiment(fileName) == 0)
            {
               break;
            }
         }
         
         fakeReview += newAdjective + " ";
         asteriskDetected = false;
         word = "";
      }
      //else if (asteriskDetected == true)
      //{
        // word += str;
      //}
      
      else if (asteriskDetected == false)
      {
         fakeReview += str;
      }
   }
   return fakeReview;
  }
}
