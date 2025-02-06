/*     */ package edu.lsu.estimator;
/*     */ 
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.NumberFormat;
/*     */ import java.text.ParseException;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TestStringFormater
/*     */ {
/*  20 */   private NumberFormat fmt = new DecimalFormat("$#,###.##");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/*  33 */     NumberFormat formatter = new DecimalFormat("000000");
/*  34 */     String s = formatter.format(-1234.567D);
/*     */     
/*  36 */     println(s);
/*     */ 
/*     */     
/*  39 */     formatter = new DecimalFormat("##");
/*  40 */     s = formatter.format(-1234.567D);
/*  41 */     println(s);
/*  42 */     s = formatter.format(0L);
/*  43 */     println(s);
/*  44 */     formatter = new DecimalFormat("##00");
/*  45 */     s = formatter.format(0L);
/*  46 */     println(s);
/*     */ 
/*     */     
/*  49 */     formatter = new DecimalFormat(".00");
/*  50 */     s = formatter.format(-0.567D);
/*  51 */     println(s);
/*  52 */     formatter = new DecimalFormat("0.00");
/*  53 */     s = formatter.format(-0.567D);
/*  54 */     println(s);
/*  55 */     formatter = new DecimalFormat("#.#");
/*  56 */     s = formatter.format(-1234.567D);
/*  57 */     println(s);
/*  58 */     formatter = new DecimalFormat("#.######");
/*  59 */     s = formatter.format(-1234.567D);
/*  60 */     println(s);
/*  61 */     formatter = new DecimalFormat(".######");
/*  62 */     s = formatter.format(-1234.567D);
/*  63 */     println(s);
/*  64 */     formatter = new DecimalFormat("#.000000");
/*  65 */     s = formatter.format(-1234.567D);
/*  66 */     println(s);
/*     */ 
/*     */     
/*  69 */     formatter = new DecimalFormat("#,###,###");
/*  70 */     s = formatter.format(-1234.567D);
/*  71 */     println(s);
/*  72 */     s = formatter.format(-1234567.89D);
/*     */ 
/*     */ 
/*     */     
/*  76 */     formatter = new DecimalFormat("#;(#)");
/*  77 */     s = formatter.format(-1234.567D);
/*  78 */     println(s);
/*     */ 
/*     */     
/*  81 */     formatter = new DecimalFormat("'#'#");
/*  82 */     s = formatter.format(-1234.567D);
/*  83 */     println(s);
/*  84 */     formatter = new DecimalFormat("'abc'#");
/*  85 */     s = formatter.format(-1234.567D);
/*  86 */     println(s);
/*     */     
/*  88 */     formatter = new DecimalFormat("'$'#,###");
/*  89 */     s = formatter.format(-1234.567D);
/*  90 */     println(s);
/*     */ 
/*     */     
/*  93 */     formatter = new DecimalFormat("$#,###");
/*  94 */     s = formatter.format(-1234890.567D);
/*  95 */     println(s);
/*     */ 
/*     */ 
/*     */     
/*  99 */     Locale locale = Locale.US;
/* 100 */     s = NumberFormat.getCurrencyInstance(locale).format(123.45D);
/* 101 */     println(s);
/*     */     
/* 103 */     locale = Locale.GERMAN;
/* 104 */     s = NumberFormat.getNumberInstance(locale).format(-1234.56D);
/* 105 */     println(s);
/*     */ 
/*     */     
/* 108 */     s = NumberFormat.getNumberInstance().format(-1234.56D);
/* 109 */     println(s);
/*     */ 
/*     */     
/*     */     try {
/* 113 */       Number number = NumberFormat.getNumberInstance(Locale.GERMAN).parse("-1.234,56");
/* 114 */       if (number instanceof Long);
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 119 */     catch (ParseException parseException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void println(String s) {
/* 127 */     System.out.println(s);
/*     */   }
/*     */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\TestStringFormater.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */