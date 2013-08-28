/**
 * 
 */
package spellcheck;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author abil
 *
 */
class  ConfusionMatrix implements Serializable {
	private static final String MATRIX_PATH = "/home/abil/workspace/NLP/data/";
	private static final String DEL_PATH = MATRIX_PATH + "del.txt";
	private static final long serialVersionUID = 1L;
	private static final String ADD_PATH = MATRIX_PATH + "add.txt";
	private static final String SUB_PATH = MATRIX_PATH + "sub.txt";
	private static final String REV_PATH = MATRIX_PATH + "rev.txt";
	
    int del[][];
    int add[][];
    int sub[][];
    int rev[][];
    
    public ConfusionMatrix() {
    	del=new int[27][27];
        add=new int[27][27];
        sub=new int[26][26];
        rev=new int[26][26];
        buildMatrices(DEL_PATH,del);
        buildMatrices(ADD_PATH,add);
        buildMatrices(SUB_PATH,sub);
        buildMatrices(REV_PATH,rev);	
	}
    
    
    private static void buildMatrices(String file, int [][] arr) 
    {
    	BufferedReader buffer;
        try
        {
            buffer = new BufferedReader(new FileReader(file));   
            String line, temp[];
            int i=0,j;
            
            while ((line = buffer.readLine())!= null)
            { 
                temp = line.split(" "); //split spaces
                for(j = 0; j<temp.length; j++)
                {    
                    arr[i][j] =Integer.parseInt(temp[j]);
                //    System.out.print(arr[i][j]);
                }
                i++;
            }
            buffer.close();
        } catch(IOException | NumberFormatException ex){
            ex.printStackTrace();
        }     
    }
    
    private static int indexOf(char c)
    {
        if(c=='@')
            return 26;
        else
            return (int)(Character.toUpperCase(c))-65;
    }
            
    int del(char x,char y) {
		return del[indexOf(x)][indexOf(y)];
	}
    
    int add(char x,char y) {
		return add[indexOf(x)][indexOf(y)];
	}
    
    int sub(char x,char y) {
		return sub[indexOf(x)][indexOf(y)];
	}
    
    int rev(char x,char y) {
		return rev[indexOf(x)][indexOf(y)];
	}
    
	
}
