/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 *
 * @author kwang
 */
public class TestStringFormater {
    
  
    
    private NumberFormat fmt = new DecimalFormat("$#,###.##");
    
    
    public TestStringFormater(){}
    
      
    /*
     * ========================== Maximum Aid Calculation =============================
     */
    public static void main(String[] args){
         

// The 0 symbol shows a digit or 0 if no digit present
NumberFormat formatter = new DecimalFormat("000000");
String s = formatter.format(-1234.567);  // -001235
// notice that the number was rounded up
println(s);

// The # symbol shows a digit or nothing if no digit present
formatter = new DecimalFormat("##");
s = formatter.format(-1234.567);         // -1235
println(s);
s = formatter.format(0);                 // 0
println(s);
formatter = new DecimalFormat("##00");
s = formatter.format(0);                 // 00
println(s);

// The . symbol indicates the decimal point
formatter = new DecimalFormat(".00");
s = formatter.format(-0.567);       
println(s);     // -.57
formatter = new DecimalFormat("0.00");
s = formatter.format(-0.567);             // -0.57
println(s);
formatter = new DecimalFormat("#.#");
s = formatter.format(-1234.567);         // -1234.6
println(s);
formatter = new DecimalFormat("#.######");
s = formatter.format(-1234.567);         // -1234.567
println(s);
formatter = new DecimalFormat(".######");
s = formatter.format(-1234.567);         // -1234.567
println(s);
formatter = new DecimalFormat("#.000000");
s = formatter.format(-1234.567);         // -1234.567000
println(s);

// The , symbol is used to group numbers
formatter = new DecimalFormat("#,###,###");
s = formatter.format(-1234.567);         // -1,235
println(s);
s = formatter.format(-1234567.890);      // -1,234,568
//println s;

// The ; symbol is used to specify an alternate pattern for negative values
formatter = new DecimalFormat("#;(#)");
s = formatter.format(-1234.567);         // (1235)
println(s);

// The ' symbol is used to quote literal symbols
formatter = new DecimalFormat("'#'#");
s = formatter.format(-1234.567);         // -#1235
println(s);
formatter = new DecimalFormat("'abc'#");
s = formatter.format(-1234.567);         // -abc1235
println(s);

formatter = new DecimalFormat("'$'#,###");
s = formatter.format(-1234.567);         // -abc1235
println(s);
        

formatter = new DecimalFormat("$#,###");
s = formatter.format(-1234890.567);         // -abc1235
println(s);       



Locale locale = Locale.US;//.CANADA;
s = NumberFormat.getCurrencyInstance(locale).format(123.45);
println(s);

locale = Locale.GERMAN;
s = NumberFormat.getNumberInstance(locale).format(-1234.56);   // -1.234,56
println(s); 

// Format for the default locale
s = NumberFormat.getNumberInstance().format(-1234.56);
println(s); 

// Parse a GERMAN number
try {
    Number number = NumberFormat.getNumberInstance(locale.GERMAN).parse("-1.234,56");
    if (number instanceof Long) {
        // Long value
    } else {
        // Double value
    }
} catch (ParseException e) {
}

    }
    
    
     
private static void println(String s){
    System.out.println(s);
}

    
}