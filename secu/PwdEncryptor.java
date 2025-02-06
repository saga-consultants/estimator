/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator.secu;

import com.kingombo.slf5j.LoggerFactory;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec; 


import org.jasypt.digest.PooledStringDigester;
import org.jasypt.digest.config.SimpleDigesterConfig;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.salt.SaltGenerator;
import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 *
 * @author kwang
 */
public class PwdEncryptor {
    private static com.kingombo.slf5j.Logger log = LoggerFactory.getLogger();
    
    private final static StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
    private final static PooledStringDigester digester = new PooledStringDigester();  
    SaltGenerator saltgen = new SaltGen();
    
    private final static StandardPBEStringEncryptor pbencryptor = new StandardPBEStringEncryptor();
    
    private String pbeway = "PBEWithMD5AndDES"; //PBEWithHmacSHA1AndDESede PBEWithMD5AndTripleDES PBEWithMD5AndDES
    byte[] salt = {
                        (byte)0xc3, (byte)0x5e, (byte)0x23, (byte)0xa5,
                        (byte)0x7f, (byte)0xf9, (byte)0xe9, (byte)0x99
                    };
    private static final String PWD = "SK{=[9SAw]0=ketp)(&u$@JQ32}7`1/.,'`1|34>ZC'+,2`50qkd3&%@$17.<?5-21LJ^&()!)*%@QA(%&+`0-A0~@!AY9827J!*(!)LAJSD#$(*%&KJNF%($!!^%~)@oih@^ ~.>>~@~^%jhp)(@&*$()&~!+s-";
    private String PBEpassword = PWD;
    // Iteration count 
    int count = 300;
    
    private static final String aeskeystr = "S#{=[9!A-|0~k*,p)>&u$@J'3^}7`1/."; //DES key length=8, DESede=24 AES=16/24/32
    
    public PwdEncryptor(){
        //digester = new PooledStringDigester();
        digester.setPoolSize(2);          // This would be a good value for a 4-core system 
        
        digester.setSaltSizeBytes(16);        
        digester.setSaltGenerator(saltgen);
        
        digester.setStringOutputType("base64");        
        digester.setAlgorithm("SHA-512"); //MD5   SHA-256, SHA-384, and SHA-512
        digester.setIterations(3000); 
        if( !digester.isInitialized()) digester.initialize();
        
        //saltgen.generateSalt(16);
        //SimpleDigesterConfig cfg = new SimpleDigesterConfig();
        
    }
    
    public String hashString(String plain){
        return digester.digest(plain).toString();
    }
    
     
    public boolean matches(String rawPass, String encPass) {
        if (digester.matches(rawPass, encPass)) {
                return true;
        }else {
                return false;
        }
    }

    public boolean cmpPwdWithEncrypted(String inputPassword, String encryptedPassword){
        //StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String digestedPassword = passwordEncryptor.encryptPassword(inputPassword);
        
        if (passwordEncryptor.checkPassword(digestedPassword, encryptedPassword)) {
            return true;
        } else {
            return false;// bad login!
        }
    }
    
    public String genEncryptedPwd(String inputPassword){ //the same password will get diff enctypted string every time. NOT DESIRED
       // return new StrongPasswordEncryptor().encryptPassword(inputPassword);
        return passwordEncryptor.encryptPassword(inputPassword);
    }
    
    public String aesEncrypt(String plainstr){
        try {
            
            SecretKeyFactory skf = null;
            skf = SecretKeyFactory.getInstance("AES");
            byte[] rawKey = aeskeystr.getBytes();
             
            SecretKeySpec keySpec = null;
            keySpec = new SecretKeySpec(rawKey, "AES");
            
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            SecretKey key = keyGen.generateKey();
            byte[] raw = key.getEncoded();
                        
            Cipher cipher =  Cipher.getInstance("AES/CBC/PKCS5Padding");//ReorderEmailDecrypter.getCipher(key, Cipher.ENCRYPT_MODE);
            //byte[] zeros = { 0, 0, 0, 0, 0, 0, 0, 0 };
            //IvParameterSpec iv = new IvParameterSpec(zeros);
            cipher.init(Cipher.ENCRYPT_MODE, key, AlgorithmParameters.getInstance("AES")   );//, iv);
            //cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            
            byte[] encBytes = null;
            encBytes = cipher.doFinal( plainstr.getBytes("UTF-8"));
             
            
            return "";
        } catch (Exception ex) {
            log.info("", ex);
        }
        return "";
    }
    
    public String aesDecrypt(String darkstr){
        
        return "";
    }
    
    
    
    public String pbeTest(String plain){
        //Password-Based Encryption (PBE) as defined in PKCS #5
        //   StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            pbencryptor.setAlgorithm("PBEWithMD5AndTripleDES"); // org.jasypt.exceptions.EncryptionInitializationException: java.security.spec.InvalidKeySpecException: Password is not ASCII
            pbencryptor.setPassword(PWD);

            System.out.println(pbencryptor.encrypt(plain));
            return pbencryptor.encrypt(plain);
    }
    
    
    public byte[] pbeEncrypt( String clearStr){//byte[] password){
        char[] charArrayPassword = new char[256];
        String sPassword = PBEpassword;//clearStr;// new String(password);

        try{
            // the javax.crypto.spec.PBEKeySpec class takes (and returns) a password as a char array 
            charArrayPassword = sPassword.toCharArray(); 

            //Begin Crypto code 
            javax.crypto.spec.PBEKeySpec pbeKeySpec;
            javax.crypto.spec.PBEParameterSpec pbeParamSpec;
            SecretKeyFactory keyFac;

            // Create PBE parameter set
            //In order to use Password-Based Encryption (PBE) as defined in PKCS5, we have to specify a salt and an iteration count.
            // The same salt and iteration count that are used for encryption must be used for decryption:
/*                // Salt
            byte[] salt = {
                (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
                (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
            };
            // Iteration count
            int count = 20;
*/                
            // Create PBE parameter set
            pbeParamSpec = new javax.crypto.spec.PBEParameterSpec(salt, count);

            // encryption password, convert it into a SecretKey object, using a PBE key factory.
            pbeKeySpec = new javax.crypto.spec.PBEKeySpec(charArrayPassword);
            keyFac = SecretKeyFactory.getInstance(pbeway); //SEVERE: java.security.NoSuchAlgorithmException: PBEWithHmacSHA1AndDESede SecretKeyFactory not available
            SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

            // Create pbeCipher Cipher
            Cipher pbeCipher = Cipher.getInstance(pbeway);
            //pbeCipher

            // Initialize pbeCipher Cipher with key and parameters
            pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec); //java.security.InvalidKeyException: Illegal key size
                    
            // Our cleartext
            String cleartextString = clearStr;//new String(password);
            byte[] cleartext = cleartextString.getBytes();

            // Encrypt the cleartext
            byte[] ciphertext = pbeCipher.doFinal(cleartext);            
            System.out.println("javaxEncrypt() ciphered text=" +new String(ciphertext) );

            
            
            // Decrypt to cleartext
            pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);
            byte[] deciphertext = pbeCipher.doFinal(ciphertext);  //Input length must be multiple of 8 when decrypting with padded cipher
            System.out.println("javaxEncrypt() deciphered text="+new String(deciphertext)+" or "+deciphertext.toString());
                        
            
            // return new String(ciphertext); 
            return ciphertext;

        }catch(Exception e){
            //exceptionCatcher = e;   
            e.printStackTrace();
        }
        String s = "Not Working";  
        return s.getBytes(); 
    }
        
    /*
SecretKeyFactory Algorithms:  AES DES, or PBEWith<digest>And<encryption algorithm>,  PBEWith<pseodu random func>And<encryption algorithm>     
e.g.: PBEWithMD5AndDES (PKCS5, v 1.5),PBEWithHmacSHA1AndDESede (PKCS5, v 2.0)
* 
* MessageDigest Algorithms: MD5, SHA-1 SHA-256 SHA-384 SHA-512
* 
* KeyGenerator Algorithms: AES DES blowfish HmacSHA1 HmacSHA256 HmacSHA384 HmacSHA512
* 
* KeyFactory Algorithms: DSA RSA EC
* 
* Cipher Algorithm Padding: NoPadding PKCS5Padding SSL3Padding
* 
* Cipher Algorithm Names: AES DES RSA Blowfish, PBEWith<digest>And<encryption> , PBEWith<prf>And<encryption>
* e.g.: PBEWithMD5AndDES (fixed CBC as the cipher mode and PKCS5Padding as the padding scheme), PBEWithHmacSHA1AndDESede ,
* Sun Unlimited Strength Jurisdiction Policy Files is needed to use PBEWithMD5AndTripleDES (DESEDE)
* Java Cryptology Extension (JCE) security provider can give additional encryption and decryption algorithms
     */
    
    ////javax.crypto.IllegalBlockSizeException: Input length must be multiple of 8 when decrypting with padded cipher
    public String pbeDecrypt(byte[] secret){
        char[] charArrayPassword = new char[128];
        String sPassword = PBEpassword;//clearStr;// new String(password);

        try{
            // the javax.crypto.spec.PBEKeySpec class takes (and returns) a password as a char array 
            charArrayPassword = sPassword.toCharArray(); 

            //Begin Crypto code 
            javax.crypto.spec.PBEKeySpec pbeKeySpec;
            javax.crypto.spec.PBEParameterSpec pbeParamSpec;
            SecretKeyFactory keyFac;

            // Create PBE parameter set
            //In order to use Password-Based Encryption (PBE) as defined in PKCS5, we have to specify a salt and an iteration count.
            // The same salt and iteration count that are used for encryption must be used for decryption:
/*                  // Salt
            byte[] salt = {
                (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
                (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
            };
            // Iteration count
            int count = 20;
*/                 
            // Create PBE parameter set
            pbeParamSpec = new javax.crypto.spec.PBEParameterSpec(salt, count);

            // encryption password, convert it into a SecretKey object, using a PBE key factory.
            pbeKeySpec = new javax.crypto.spec.PBEKeySpec(charArrayPassword);

            keyFac = SecretKeyFactory.getInstance(pbeway);

            SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

            // Create pbeCipher Cipher
            Cipher pbeCipher = Cipher.getInstance(pbeway);

/*                    
            // Initialize pbeCipher Cipher with key and parameters
            pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);


            // Our cleartext
            String cleartextString = clearStr;//new String(password);
            byte[] cleartext = cleartextString.getBytes();

            // Encrypt the cleartext
            byte[] ciphertext = pbeCipher.doFinal(cleartext);            
            System.out.println("javaxDecrypt() ciphered text=" +new String(ciphertext) );
*/                   
            // Decrypt the cleartext
            pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

            byte[] deciphertext = pbeCipher.doFinal(secret);//.getBytes());  
            //Input length must be multiple of 8 when decrypting with padded cipher
//javax.crypto.IllegalBlockSizeException: Input length must be multiple of 8 when decrypting with padded cipher
            
            System.out.println("javaxDecrypt() deciphered text="+new String(deciphertext));
            return new String(deciphertext); 

        }catch(Exception e){
            //exceptionCatcher = e;   
            e.printStackTrace();
        }
        String s = "Not Working";  
        return s; 
    }
    
}




/*
 Easiest use: the BasicPasswordEncryptor util class:

...
BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
String encryptedPassword = passwordEncryptor.encryptPassword(userPassword);
...
if (passwordEncryptor.checkPassword(inputPassword, encryptedPassword)) {
  // correct!
} else {
  // bad login!
}
...
More security: the StrongPasswordEncryptor util class with a much more secure (but slower!) algorithm:

...
StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
String encryptedPassword = passwordEncryptor.encryptPassword(userPassword);
...
if (passwordEncryptor.checkPassword(inputPassword, encryptedPassword)) {
  // correct!
} else {
  // bad login!
}
...
A little bit more control on encryptor configuration: the ConfigurablePasswordEncryptor class:

...
ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
passwordEncryptor.setAlgorithm("SHA-1");
passwordEncryptor.setPlainDigest(true);
String encryptedPassword = passwordEncryptor.encryptPassword(userPassword);
...
if (passwordEncryptor.checkPassword(inputPassword, encryptedPassword)) {
  // correct!
} else {
  // bad login!
}
...
All these util classes are in fact pre-configured, easy-to-use versions of StandardStringDigester, so let's use the original class for total control:

...
StandardStringDigester digester = new StandardStringDigester();
digester.setAlgorithm("SHA-1");   // optionally set the algorithm
digester.setIterations("50000");  // increase security by performing 50000 hashing iterations
...
String digest = digester.digest(userPassword);
...
if (digester.matches(inputPassword, digest)) {
  // correct!
} else {
  // bad login!
}
...
And we can even use a pooled version for higher performance in multi-processor/multi-core systems:

...
PooledStringDigester digester = new PooledStringDigester();
digester.setPoolSize(4);          // This would be a good value for a 4-core system 
digester.setAlgorithm("SHA-1");
digester.setIterations("50000");
...
String digest = digester.digest(userPassword);
...
if (digester.matches(inputPassword, digest)) {
  // correct!
} else {
  // bad login!
}
...
 */