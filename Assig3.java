/**
 * CS 0445 Fall 17
 * @author Jonathan Chang
 * Assig 3
 * Khattab
 * 11/13/2017
 */
import java.io.*;
import java.util.*;
import java.util.ArrayList;
public class Assig3{
    public static void main (String[]args){
        new Assig3();
    }

    /**
     * File Reader
     * from example code given
     * plus some modifications
     * calls findWord
     */
    public Assig3(){
        {
            Scanner inScan = new Scanner(System.in);
            Scanner fReader;
            File fName;
            String fString = "", word = "";

            // Make sure the file name is valid
            while (true)
            {
                try
                {
                    System.out.println("Please enter grid filename:");
                    fString = inScan.nextLine();
                    fName = new File(fString);
                    fReader = new Scanner(fName);

                    break;
                }
                catch (IOException e)
                {
                    System.out.println("Problem " + e);
                }
            }

            // Parse input file to create 2-d grid of characters
            String [] dims = (fReader.nextLine()).split(" ");
            int rows = Integer.parseInt(dims[0]);
            int cols = Integer.parseInt(dims[1]);
            char [][] theBoard = new char[rows][cols];

            for (int i = 0; i < rows; i++)
            {
                String rowString = fReader.next();
                for (int j = 0; j < rowString.length(); j++)
                {
                    theBoard[i][j] = Character.toLowerCase(rowString.charAt(j));
                }
            }
            // Show user the grid
            for (int i = 0; i < rows; i++)
            {
                for (int j = 0; j < cols; j++)
                {
                    System.out.print(theBoard[i][j] + " ");
                }
                System.out.println();
            }

            System.out.println("Please enter the word to search for:");
            word = (inScan.nextLine()).toLowerCase();
            while (!(word.equals("")))
            {
                /**
                 * NUMBER COUNT OF WORDS
                 * DEPENDS ON SPACES
                 */
                int num = 1;
                for(char c : word.toCharArray()){
                    if(c == ' ')
                        num++;
                }
                int x = 0, y = 0;

                // Search for the word.  Note the nested for loops here.  This allows us to
                // start the search at any of the locations in the board.  The search itself
                // is recursive (see findWord method for details).  Note also the boolean
                // which allows us to exit the loop before all of the positions have been
                // tried -- as soon as one solution has been found we can stop looking.
                boolean found = false;
                System.out.println("Looking for: " + word);
                System.out.println("containing " + num + " words");

                for (int r = 0; (r < rows && !found); r++)
                {
                    for (int c = 0; (c < cols && !found); c++)
                    {
                        // Start search for each position at index 0 of the word
                        found = findWord(r, c, word, 0, theBoard);
                        if (found)
                        {
                            x = r;  // store starting indices of solution
                            y = c;
                        }
                    }
                }
                /**
                 * PRINT OUT COORDINATES FOR EACH WORD FOUND
                 */
                if (found)
                {
                    System.out.println("The phrase : " + word);
                    System.out.println("was found: ");
                    System.out.println(printer);
                    printer.clear();

                    for (int i = 0; i < rows; i++)
                    {
                        for (int j = 0; j < cols; j++)
                        {
                            System.out.print(theBoard[i][j] + " ");
                            theBoard[i][j] = Character.toLowerCase(theBoard[i][j]);
                        }
                        System.out.println();
                    }
                }
                else
                {
                    System.out.println("The phrase: " + word);
                    System.out.println("was not found");
                }

                System.out.println("Please enter the word to search for:");
                word = (inScan.nextLine()).toLowerCase();
            }

        }
    }

    /**
     * Determines whether word entered is valid
     * @param r
     * @param c
     * @param word
     * @param loc
     * @param bo
     * @return TRUE IF WORD CORRECTLY FOUND
     */
    public boolean findWord(int r, int c, String word, int loc, char [][] bo)
    {
        /**
         * CHECK BOUNDARIES
         * @return false if goes off board
         */
        if(r >= bo.length || c >= bo[0].length || r<0||c<0) {
            return false;
        }
        /**
         * CHECK FIRST LETTER MATCH
         * @return false if no match
         */
        if(bo[r][c]!=word.charAt(loc)){
            return false;
        }
        else {
            bo[r][c] = Character.toUpperCase(bo[r][c]);
            /**
             * LAST LETTER END OF WORD
             * @return true
             */
            if(loc==word.length()-1){
                printer.add(word.substring(loc)+": ("+r+", "+c+")\n");
                match=false;
                goRight=true; goDown=true; goLeft=true; goUp=true;
                x=0; y=0;
                return true;
            }
            else{
                //GET CURRENT WORD TO LOOK FOR
                int wordLength = 0;
                if(word.substring(loc).contains(" ")){
                    String temp= word.substring(loc);
                    wordLength=temp.substring(0,temp.indexOf(" ")).length()-1;
                    temp="";
                }
                else
                    wordLength=word.substring(loc).length()-1;
                /**
                 * 1.) CHECK NEXT 2 LETTERS PER DIRECTION
                 *      CONTINUE IN DIRECTION IF MATCHES
                 */
                if(word.charAt(loc+1)==' '){
                    match=true;
                    x=r;
                    y=c;
                }
                boolean righty=false, downy=false, lefty=false, upy=false;
                //RIGHT
                if(goRight){
                    if(!match && c+1<bo[0].length && bo[r][c+1]==word.charAt(loc+1) ) {
                        righty = true;
                        if (wordLength > 1 && c + wordLength >= bo[0].length) {
                            righty = false;
                        }
                        /**
                         * 2 LETTERS LONG WORD MATCHED
                         */
                        if(righty && wordLength==1){
                            match=true;
                            x=r;
                            y=c+1;
                            bo[x][y] = Character.toUpperCase(bo[x][y]);
                        }
                        /**
                         * TWO LETTERS ARE MATCHED, CHECK NEXT
                         * KEEP LOOKING IF NO MATCH
                         */
                        if(righty && !match){
                            bo[r][c+1] = Character.toUpperCase(bo[r][c+1]);
                            match=consec(r,c+2,loc+2,word,bo,righty,downy,lefty,upy);
                            if(!match)
                                righty=false;
                        }
                    }
                }
                //DOWN
                if(goDown){
                    if(!match && r+1<bo.length && bo[r+1][c]==word.charAt(loc+1)) {
                        downy = true;
                        if (wordLength > 1 && r + wordLength >= bo.length) {
                            downy = false;
                        }
                        if(downy && wordLength==1){
                            match=true;
                            x=r+1;
                            y=c;
                            bo[x][y] = Character.toUpperCase(bo[x][y]);
                        }
                        if(downy && !match){
                            bo[r+1][c] = Character.toUpperCase(bo[r+1][c]);
                            match=consec(r+2,c,loc+2,word,bo,righty,downy,lefty,upy);
                            if(!match)
                                downy=false;
                        }
                    }
                }

               //LEFT
                if(goLeft){
                    if(!match && c-1>=0 && bo[r][c-1]==word.charAt(loc+1)) {
                        lefty = true;
                        if (wordLength > 1 && c - wordLength < 0) {
                            lefty = false;
                        }
                        if(lefty && wordLength==1){
                            match=true;
                            x=r;
                            y=c-1;
                            bo[x][y] = Character.toUpperCase(bo[x][y]);
                        }
                        if(lefty && !match){
                            bo[r][c-1] = Character.toUpperCase(bo[r][c-1]);
                            match=consec(r,c-2,loc+2,word,bo,righty,downy,lefty,upy);
                            if(!match)
                                lefty=false;
                        }
                    }
                }

                //UP
                if(goUp){
                    if(!match && r-1>=0 && bo[r-1][c]==word.charAt(loc+1) ) {
                        upy = true;
                        if (wordLength > 1 && r - wordLength < 0) {
                            upy = false;
                        }
                        if(upy && wordLength==1){
                            match=true;
                            x=r-1;
                            y=c;
                            bo[x][y] = Character.toUpperCase(bo[x][y]);
                        }
                        if(upy && !match){
                            bo[r-1][c] = Character.toUpperCase(bo[r-1][c]);
                            match=consec(r-2,c,loc+2,word,bo,righty,downy,lefty,upy);
                            if(!match)
                                upy=false;
                        }
                    }
                }

                //IF CURRENT WORD SUCCESSFULLY MATCHED
                if(match){
                    match=false;
                    /**
                     * RECURSION:
                     * KEEP LOOKING FOR NEXT WORD
                     */
                    boolean ans=false;
                    while(word.substring(loc).contains(" ") && !ans){
                        int prevLoc=loc; //SAVE THIS IN CASE WE NEED TO BACKTRACK
                        String temp=word.substring(loc);
                        loc = prevLoc + temp.indexOf(" ");
                        loc++;
                        printer.add(temp.substring(0,temp.indexOf(" "))+": ("+r+", "+c+") to ("+x+", "+y+")\n");
                        //r and c are start coordinates, x and y are end coordinates
                        r=x;
                        c=y;
                        if(!ans){
                            goRight=true;
                            goDown=false;
                            goLeft=false;
                            goUp=false;
                            ans=findWord(r,c+1,word,loc,bo);
                        }
                        if (!ans){
                            goRight=false;
                            goDown=true;
                            goLeft=false;
                            goUp=false;
                            ans = findWord(r+1, c, word, loc, bo);  // Down
                        }
                        if (!ans){
                            goRight=false;
                            goDown=false;
                            goLeft=true;
                            goUp=false;
                            ans = findWord(r, c-1, word, loc, bo);  // Left
                        }
                        if (!ans){
                            goRight=false;
                            goDown=false;
                            goLeft=false;
                            goUp=true;
                            ans = findWord(r-1, c, word, loc, bo);  // Up
                        }
                        /**
                         * BACKTRACK:
                         * IF NO DIRECTIONS WORD, RESTART SEARCH FOR LAST WORD
                         */
                        if (!ans){
                            printer.remove(printer.size()-1);
                            temp=temp.substring(0,temp.indexOf(" "));
                            bo[r][c] = Character.toLowerCase(bo[r][c]);
                            clearIt(r,c,temp.length(),bo,righty,downy,lefty,upy);
                            loc=prevLoc;
                            goRight=true; goDown=true; goLeft=true; goUp=true;
                            return false;
                        }
                        /**
                         * Ans is true for a direction
                         * @return True
                         */
                        match=false;
                        goRight=true; goDown=true; goLeft=true; goUp=true;
                        x=0; y=0;
                        return true;
                    }
                    /**
                     * LAST WORD
                     * @return True
                     */
                    printer.add(word.substring(loc)+": ("+r+", "+c+") to ("+x+", "+y+")\n");
                    ans=true;
                    return true;
                }
                /**
                 * NO MATCH
                 * @return False
                 */
                else{
                    bo[r][c] = Character.toLowerCase(bo[r][c]);
                    return false;
                }
            }
        }
    }

    /**
     * CLEARS ROW IN ONE DIRECTION (FOR ONE WORD)
     * @param r
     * @param c
     * @param count
     * @param bo
     * @param right
     * @param down
     * @param left
     * @param up
     */
    public void clearIt(int r, int c, int count, char [][] bo, boolean right, boolean down, boolean left, boolean up){
        //ERASE DEPENDING ON WHICH DIRECTION WAS LAST USED
        if(right){
            while(count>1){
                bo[r][c-1] = Character.toLowerCase(bo[r][c-1]);
                c--;
                count--;
            }
        }
        else if(down){
            while(count>1){
                bo[r-1][c] = Character.toLowerCase(bo[r-1][c]);
                r--;
                count--;
            }
        }
        else if(left){
            while(count>1){
                bo[r][c+1] = Character.toLowerCase(bo[r][c+1]);
                c++;
                count--;
            }
        }
        else if(up){
            while(count>1){
                bo[r+1][c] = Character.toLowerCase(bo[r+1][c]);
                r++;
                count--;
            }
        }

    }

    /**
     * IF WORD IS CONSECUTIVE IN ONE DIRECTION (GREATER THAN 2 LETTERS)
     * @param r
     * @param c
     * @param loc
     * @param word
     * @param bo
     * @param right
     * @param down
     * @param left
     * @param up
     * @return TRUE IF WORD LETTERS ARE CONSECUTIVE IN ONE DIRECTION
     */
    public boolean consec(int r, int c, int loc, String word, char [][]bo, boolean right, boolean down, boolean left, boolean up){
        int count=2; //had to match at least 2 letters
        boolean border=false;
        //GET CURRENT WORD LOOKING FOR
        if(word.substring(loc).contains(" ")){
            word= word.substring(loc);
            word=word.substring(0,word.indexOf(" "));
        }
        else
            word = word.substring(loc);
        StringBuilder sb = new StringBuilder(word);

        if(!right && !down && !left && !up)
            return false;

        //BORDER DIFFERENT DEPENDING ON WHICH DIRECTION WE ARE LOOKING IN
        if(right){
            border = c<bo[0].length;
        }
        if(down){
            border=r<bo.length;
        }
        if(left){
            border=c>=0;
        }
        if(up){
            border=r>=0;
        }
        //LOOP THROUGH WORD
        //EACH TIME THE LETTER MATCHES, DELETE THAT CHAR
        //IF THE STRINGBUILDER IS NOW EMPTY, WORD IS FOUND
        for(int i=0;i<word.length() && border ;i++,count++){
            if(bo[r][c]==sb.charAt(0)){
                bo[r][c] = Character.toUpperCase(bo[r][c]);
                sb.deleteCharAt(0);
                if(sb.length()==0){
                    x=r;
                    y=c;
                    return true;
                }
                if(right){
                    c++;
                    border = c<bo[0].length;
                }
                if(down){
                    r++;
                    border=r<bo.length;
                }
                if(left){
                    c--;
                    border=c>=0;
                }
                if(up){
                    r--;
                    border=r>=0;
                }
            }
            //IF ONE LETTER DOESNT MATCH, ERASE WORD FOUND SO FAR
            else{
                clearIt(r,c,count, bo, right, down, left, up);
                return false;
            }
        }
        return false;
    }

    /**
     * INSTANCE VAR
     */
    public int x=0,y=0;
    public boolean match=false;
    public boolean goRight=true, goDown=true, goLeft=true, goUp=true;
    ArrayList printer = new ArrayList( );
}