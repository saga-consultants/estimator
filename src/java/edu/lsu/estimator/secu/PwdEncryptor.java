/*     */ package edu.lsu.estimator.secu;
/*     */ 
/*     */ import com.kingombo.slf5j.Logger;
/*     */ import com.kingombo.slf5j.LoggerFactory;
/*     */ 
/*     */ import java.security.AlgorithmParameters;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.KeyGenerator;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.crypto.SecretKeyFactory;
/*     */ import javax.crypto.spec.PBEKeySpec;
/*     */ import javax.crypto.spec.PBEParameterSpec;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import org.jasypt.digest.PooledStringDigester;
/*     */ import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
/*     */ import org.jasypt.salt.SaltGenerator;
/*     */ import org.jasypt.util.password.StrongPasswordEncryptor;
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
/*     */ public class PwdEncryptor
/*     */ {
/*  30 */   private static Logger log = LoggerFactory.getLogger();
/*     */   
/*  32 */   private static final StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
/*  33 */   private static final PooledStringDigester digester = new PooledStringDigester();
/*  34 */   SaltGenerator saltgen = (SaltGenerator)new SaltGen();
/*     */   
/*  36 */   private static final StandardPBEStringEncryptor pbencryptor = new StandardPBEStringEncryptor();
/*     */   
/*  38 */   private String pbeway = "PBEWithMD5AndDES";
/*  39 */   byte[] salt = new byte[] { -61, 94, 35, -91, Byte.MAX_VALUE, -7, -23, -103 };
/*     */ 
/*     */   
/*     */   private static final String PWD = "SK{=[9SAw]0=ketp)(&u$@JQ32}7`1/.,'`1|34>ZC'+,2`50qkd3&%@$17.<?5-21LJ^&()!)*%@QA(%&+`0-A0~@!AY9827J!*(!)LAJSD#$(*%&KJNF%($!!^%~)@oih@^ ~.>>~@~^%jhp)(@&*$()&~!+s-";
/*     */   
/*  44 */   private String PBEpassword = "SK{=[9SAw]0=ketp)(&u$@JQ32}7`1/.,'`1|34>ZC'+,2`50qkd3&%@$17.<?5-21LJ^&()!)*%@QA(%&+`0-A0~@!AY9827J!*(!)LAJSD#$(*%&KJNF%($!!^%~)@oih@^ ~.>>~@~^%jhp)(@&*$()&~!+s-";
/*     */   
/*  46 */   int count = 300;
/*     */   
/*     */   private static final String aeskeystr = "S#{=[9!A-|0~k*,p)>&u$@J'3^}7`1/.";
/*     */ 
/*     */   
/*     */   public PwdEncryptor() {
/*  52 */     digester.setPoolSize(2);
/*     */     
/*  54 */     digester.setSaltSizeBytes(16);
/*  55 */     digester.setSaltGenerator(this.saltgen);
/*     */     
/*  57 */     digester.setStringOutputType("base64");
/*  58 */     digester.setAlgorithm("SHA-512");
/*  59 */     digester.setIterations(3000);
/*  60 */     if (!digester.isInitialized()) digester.initialize();
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String hashString(String plain) {
/*  68 */     return digester.digest(plain).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(String rawPass, String encPass) {
/*  73 */     if (digester.matches(rawPass, encPass)) {
/*  74 */       return true;
/*     */     }
/*  76 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean cmpPwdWithEncrypted(String inputPassword, String encryptedPassword) {
/*  82 */     String digestedPassword = passwordEncryptor.encryptPassword(inputPassword);
/*     */     
/*  84 */     if (passwordEncryptor.checkPassword(digestedPassword, encryptedPassword)) {
/*  85 */       return true;
/*     */     }
/*  87 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String genEncryptedPwd(String inputPassword) {
/*  93 */     return passwordEncryptor.encryptPassword(inputPassword);
/*     */   }
/*     */ 
/*     */   
/*     */   public String aesEncrypt(String plainstr) {
/*     */     try {
/*  99 */       SecretKeyFactory skf = null;
/* 100 */       skf = SecretKeyFactory.getInstance("AES");
/* 101 */       byte[] rawKey = "S#{=[9!A-|0~k*,p)>&u$@J'3^}7`1/.".getBytes();
/*     */       
/* 103 */       SecretKeySpec keySpec = null;
/* 104 */       keySpec = new SecretKeySpec(rawKey, "AES");
/*     */       
/* 106 */       KeyGenerator keyGen = KeyGenerator.getInstance("AES");
/* 107 */       keyGen.init(256);
/* 108 */       SecretKey key = keyGen.generateKey();
/* 109 */       byte[] raw = key.getEncoded();
/*     */       
/* 111 */       Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
/*     */ 
/*     */       
/* 114 */       cipher.init(1, key, AlgorithmParameters.getInstance("AES"));
/*     */ 
/*     */       
/* 117 */       byte[] encBytes = null;
/* 118 */       encBytes = cipher.doFinal(plainstr.getBytes("UTF-8"));
/*     */ 
/*     */       
/* 121 */       return "";
/* 122 */     } catch (Exception ex) {
/* 123 */       log.info("", ex);
/*     */       
/* 125 */       return "";
/*     */     } 
/*     */   }
/*     */   
/*     */   public String aesDecrypt(String darkstr) {
/* 130 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String pbeTest(String plain) {
/* 138 */     pbencryptor.setAlgorithm("PBEWithMD5AndTripleDES");
/* 139 */     pbencryptor.setPassword("SK{=[9SAw]0=ketp)(&u$@JQ32}7`1/.,'`1|34>ZC'+,2`50qkd3&%@$17.<?5-21LJ^&()!)*%@QA(%&+`0-A0~@!AY9827J!*(!)LAJSD#$(*%&KJNF%($!!^%~)@oih@^ ~.>>~@~^%jhp)(@&*$()&~!+s-");
/*     */     
/* 141 */     System.out.println(pbencryptor.encrypt(plain));
/* 142 */     return pbencryptor.encrypt(plain);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] pbeEncrypt(String clearStr) {
/* 147 */     char[] charArrayPassword = new char[256];
/* 148 */     String sPassword = this.PBEpassword;
/*     */ 
/*     */     
/*     */     try {
/* 152 */       charArrayPassword = sPassword.toCharArray();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 171 */       PBEParameterSpec pbeParamSpec = new PBEParameterSpec(this.salt, this.count);
/*     */ 
/*     */       
/* 174 */       PBEKeySpec pbeKeySpec = new PBEKeySpec(charArrayPassword);
/* 175 */       SecretKeyFactory keyFac = SecretKeyFactory.getInstance(this.pbeway);
/* 176 */       SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
/*     */ 
/*     */       
/* 179 */       Cipher pbeCipher = Cipher.getInstance(this.pbeway);
/*     */ 
/*     */ 
/*     */       
/* 183 */       pbeCipher.init(1, pbeKey, pbeParamSpec);
/*     */ 
/*     */       
/* 186 */       String cleartextString = clearStr;
/* 187 */       byte[] cleartext = cleartextString.getBytes();
/*     */ 
/*     */       
/* 190 */       byte[] ciphertext = pbeCipher.doFinal(cleartext);
/* 191 */       System.out.println("javaxEncrypt() ciphered text=" + new String(ciphertext));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 196 */       pbeCipher.init(2, pbeKey, pbeParamSpec);
/* 197 */       byte[] deciphertext = pbeCipher.doFinal(ciphertext);
/* 198 */       System.out.println("javaxEncrypt() deciphered text=" + new String(deciphertext) + " or " + deciphertext.toString());
/*     */ 
/*     */ 
/*     */       
/* 202 */       return ciphertext;
/*     */     }
/* 204 */     catch (Exception e) {
/*     */       
/* 206 */       e.printStackTrace();
/*     */       
/* 208 */       String s = "Not Working";
/* 209 */       return s.getBytes();
/*     */     } 
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String pbeDecrypt(byte[] secret) {
/* 232 */     char[] charArrayPassword = new char[128];
/* 233 */     String sPassword = this.PBEpassword;
/*     */ 
/*     */     
/*     */     try {
/* 237 */       charArrayPassword = sPassword.toCharArray();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 256 */       PBEParameterSpec pbeParamSpec = new PBEParameterSpec(this.salt, this.count);
/*     */ 
/*     */       
/* 259 */       PBEKeySpec pbeKeySpec = new PBEKeySpec(charArrayPassword);
/*     */       
/* 261 */       SecretKeyFactory keyFac = SecretKeyFactory.getInstance(this.pbeway);
/*     */       
/* 263 */       SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
/*     */ 
/*     */       
/* 266 */       Cipher pbeCipher = Cipher.getInstance(this.pbeway);
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
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 282 */       pbeCipher.init(2, pbeKey, pbeParamSpec);
/*     */       
/* 284 */       byte[] deciphertext = pbeCipher.doFinal(secret);
/*     */ 
/*     */ 
/*     */       
/* 288 */       System.out.println("javaxDecrypt() deciphered text=" + new String(deciphertext));
/* 289 */       return new String(deciphertext);
/*     */     }
/* 291 */     catch (Exception e) {
/*     */       
/* 293 */       e.printStackTrace();
/*     */       
/* 295 */       String s = "Not Working";
/* 296 */       return s;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\secu\PwdEncryptor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */