//-----------------------------------------
// 
// CLASS		: MinimHelper.java
//
// REMARKS		: Class that helps loading sounds
//					
// CODE SOURCE	: Lab 10 IAT 265
//
//-----------------------------------------
package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MinimHelper 
{

    public String sketchPath( String fileName ) 
    {
        return "assets/"+fileName;
    }

    public InputStream createInput(String fileName) 
    {
        InputStream is = null;
        try
        {
            is = new FileInputStream(sketchPath(fileName));
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            //additional try-catch for adding default sound to avoid not having sound at all
            try 
            {
				is = new FileInputStream(sketchPath("default.wav"));
			} 
            catch (FileNotFoundException e1) 
            {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        return is;
    }
}