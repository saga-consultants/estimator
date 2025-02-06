/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator.secu;

import com.kingombo.slf5j.Logger;
import com.kingombo.slf5j.LoggerFactory;
import edu.lsu.estimator.AppReference;
import edu.lsu.estimator.Counselor;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.*;
import javax.naming.directory.*;
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author kwang
 * work with SSL ( with LDAPS)
 * work with authentication (remote LDAP or local SHA256 hash comp)
 * work with encryption and encryption ( before and after hessian)
 * pack and unpack DTO/DO used for Hession 
 */
//javax.inject.Singleton or  javax.ejb.Singleton
//@Singleton
@Stateful
@Named("darkPlain")
//@Dependent
public class DarkPlain {
    private  static final Logger log = LoggerFactory.getLogger();
    
    @Inject AppReference ref;
    
    /*If you encrypt your passwords using password-based encryption (a two-way technique) and an attacker gets to know your encryption password, 
     * all of your user passwords will be revealed (and, probably, all at a time). If you don't have such encryption password (or key) to be able 
     * to decrypt, this risk disappears, and the attacker will have to trust on brute force or similar strategies.
     * 
     * you should not even have a way to get to read/know/see your users' passwords, no matter if you are the system administrator! 
     * If one of your users loses his/her password, just reset it to a new value and send him/her a message to a verified email address with the new one, 
     * asking to change it as soon as possible.
     * 
     * match passwords by comparing digests, not unencrypted strings.
     * to make brute force (digest matching by dict etc) harder,  two concepts come in our help: the salt (random >8 bytes) and the iteration count (>1000).
     * 
     * passwds are character strings, but digest algorithms work at a byte level.
     * two identical character strings may be represented with different byte sequences depending on the encoding being applied for translation (ISO-8859-1, UTF-8, etc...).
     * 
     * MS windows use ISO-8859-1 by default, while Linux uses UTF8 by default.
     * Prior to digesting, perform string-to-byte sequence translation using a fixed encoding, preferably UTF-8.
     * If you are using Java, where String objects are encoding-independent (although backed by UTF-16), you won't have to worry whether your application uses ISO-8859-1 or 
     * any other encoding instead of   UTF-8 for its user interface,
     * 
     * We will normally want to manage and store the digested password as a character string, but the digest function will output a sequence of bytes which will not necessarily represent 
     * a valid character string in any encoding. This is where BASE64 encoding comes to the rescue. By encoding our digested sequence of bytes in BASE64, we will make sure that the 
     * output byte sequence represents a valid, displayable, US-ASCII character string. 
     * 
     * As an alternative to BASE64, you could also encode your output as hexadecimal strings, which would be an equally valid method (although you would get more lengthy digest strings).
     * 
     * The easiest way of encrypting passwords in Java using the explained techniques is using Jasypt, which already does all this processing transparently for you.
     * If we cannot use jasypt, or if for some reason we wish to develop encryption features ourselves, we will need the following tools for our task:

java.security.MessageDigest for creating digests. This class allows to specify the digest algorithm we wish to use.
java.security.SecureRandom for generating random salt in a secure manner, using algorithms like SHA1PRNG.
The java.lang.String.getBytes(String charsetName) method, for obtaining a byte sequence from the input String, specifying a fixed encoding ("UTF-8").
org.apache.commons.codec.binary.Base64, part of the Apache Commons-Codec library, for performing BASE64 translations on hash output.
java.text.Normalizer (only in Java SE 6) or com.ibm.icu.text.Normalizer (part of the International Components for Unicode package), for Unicode normalization operations.

     */
    public String genAuthenPWDDigest(String plainStr){ ////Encrypt passwords using one-way techniques, this is, digests.
        String digest = ref.getDigester().digest(plainStr);
        return digest;
    }
    
    public String digestPWD(String plainStr, String saveDigest){ 
        
        /**** save in ref as global
        PooledStringDigester digester = new PooledStringDigester();
        digester.setPoolSize(4);          // This would be a good value for a 4-core system 
        digester.setAlgorithm("SHA-1");
        digester.setIterations("50000");
        */         
        //String digest = ref.digester.digest(userPassword);         
        if (ref.getDigester().matches(plainStr, saveDigest)) {
            // correct!
        } else {
            // bad login!
        }
        return "";
    }
    
    ///javax.naming.CommunicationException: ldap.lasierra.edu:636 [Root exception is java.net.SocketTimeoutException: connect timed out
    //create an initial context to an LDAP server using SSL
    public int authLDAPS(String who, String token,    Counselor user){  /////////////////network timeput ????
        //for(int x=0; x<9; x++)
        //timeRemoteServer();
        
        int res=-1;
        log.info("######################### authLDAPS() got user name %s",who);
        if( who==null || who.trim().isEmpty() || token==null || token.trim().isEmpty()){
            return res;
        }
        
        // Set up environment for creating initial context
        Hashtable<String, Object> env = new Hashtable<String, Object>(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY,    "com.sun.jndi.ldap.LdapCtxFactory");
        
         
        env.put(Context.PROVIDER_URL, "ldaps://ldap.lasierra.edu:636"); // ldaps@port 389: javax.net.ssl.SSLHandshakeException: Remote host closed connection during handshake
        // request the use of SSL via the use of LDAPS URLs.  ldaps means using ldap protocol with ssl
        env.put(Context.SECURITY_PROTOCOL, "ssl"); //makes no diff for ldap and ldaps         
        //env.put(Context.PROVIDER_URL, "ldap://ldap.lasierra.edu:389");
        
        
        //trace ldap behavior to an instance of java.io.OutputStream
//        env.put("com.sun.jndi.ldap.trace.ber", System.err);
        
        //By default, the LDAP provider first uses version 3 to communicate with the specified LDAP server, then auto downgrade to 2 if v3 not work
        env.put("java.naming.ldap.version", "3"); 
        
        // Authenticate as  User and password  
        env.put(Context.SECURITY_AUTHENTICATION, "simple"); //simple auth req credential is not null or empty, otherwise the auth will be changed by ldap to "none", aka anonymous mode.
 
 //2012-06-06 Alex has two cn and two email and two uid in LDAP, but the DN is and should be only one.
        //so change to use uid ( maybe cn, or mail='')
        //######### NON of them worked!!! has to use DN to auth, which can search LDAP first by CN/UID/MAIL, then get entry's DN, then use that DN to auth       
        //env.put(Context.SECURITY_PRINCIPAL, "mail="+who.trim() + (user.getStdInd()==0 ? "@lasierra.edu":"@my.lasierra.edu") );
        //env.put(Context.SECURITY_PRINCIPAL, "uid="+who.trim());
        
        //2017-01-04 pestiamtor1617, which pickup estimator.war from NB proj built 08-2016, failed on dn="uid=xxx", so change to "cn=xxx"
        
        env.put(Context.SECURITY_PRINCIPAL,  "cn="+who.trim()+  (user.getStdInd()==0 ? ",ou=employee,":",ou=student,")+ "ou=people,dc=lasierra,dc=edu"); //cn=S. User, ou=NewHires, o=JNDITutorial
        env.put(Context.SECURITY_CREDENTIALS, token);
        
        //-Dcom.sun.jndi.ldap.connect.pool.timeout=300000
        //environment property com.sun.jndi.ldap.connect.timeout that sets the timeout for connecting to the server
         env.put("com.sun.jndi.ldap.connect.timeout", "5000");
                  
        //cutoff the LDAP response from the server after the initial connection is established with the server.
        env.put("com.sun.jndi.ldap.read.timeout", "5000"); //microseconds
        try {
            // Create initial context
            DirContext ctx = new InitialDirContext(env);  //InitialLdapContext context = new InitialLdapContext(env, null);
//########################### SEVERE: javax.naming.CommunicationException: ldap.lasierra.edu:636 [Root exception is java.net.SocketTimeoutException: connect timed out]            
//SEVERE: javax.naming.CommunicationException: ldap.lasierra.edu:636 [Root exception is java.net.SocketTimeoutException: connect timed out]            
            res = 1;
            //System.out.println("authLDAPS() Context Sucessfully Initialized. Binded. User "+who+" is authenticated.");
        
            log.info("################# authLDAPS() Context Sucessfully Initialized. Binded. User %s is authenticated.", who);
            
            //do sth with the conn or ctx
            //System.out.println(ctx.lookup("ou=NewHires"));
            
            // Close the context when we're done
            ctx.close();
        } catch(AuthenticationException aex) { //[LDAP: error code 49 - Invalid Credentials]
            log.info("############## authLDAPS() AuthenticationException: %s", aex.getMessage());   
            log.info("############## authLDAPS() principal=%s", env.get(Context.SECURITY_PRINCIPAL));
            aex.printStackTrace();
            res = 0;
        }catch( AuthenticationNotSupportedException anse){
            log.info("############## authLDAPS() unsupported auth method: %s ", anse.getMessage());
            res=-2;
        } catch(  CommunicationException  ste){
            log.info("################# authLDAPS() Communication Exception : %s", ste.getMessage());
            ste.printStackTrace();
//        }catch(SocketTimeoutException | ConnectException | CommunicationException  ste){
            res = -3;
        } catch (NamingException e) {//if no network: SEVERE: javax.naming.CommunicationException: ldap.lasierra.edu:636 [Root exception is java.net.UnknownHostException: ldap.lasierra.edu]
            log.info("################# authLDAPS() naming exception: %s", e.getMessage());
            e.printStackTrace();
            res = 0;        
        }catch( Exception e){//ConnectException ce){//: Connection timed out) 
            log.info("################# authLDAPS()  exception: %s", e.getMessage());
            e.printStackTrace();
            res = -4;
        }
        
        //CommunicationException  timeout
        return res;
    }
    
    
    //create an initial context to an LDAP server using DIGEST-MD5 authentication
    public int authLdapsMD5(String who, String token){
        int res=-1;        
        if( who==null || who.trim().isEmpty() || token==null || token.trim().isEmpty()){
            return res;
        }         
        Hashtable<String, Object> env = new Hashtable<String, Object>(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldaps://ldap.lasierra.edu:636");
        
        // Specify SSL ?????
        // request the use of SSL via the use of LDAPS URLs.  ldaps means using ldap protocol with ssl
        //env.put(Context.SECURITY_PROTOCOL, "ssl");
        
        // Authenticate as C. User and password "mysecret"
        env.put(Context.SECURITY_AUTHENTICATION, "DIGEST-MD5");
        env.put("java.naming.security.sasl.realm", "dc=lasierra, dc=edu");
        
        env.put(Context.SECURITY_PRINCIPAL, "uid="+who.trim()+",ou=employee,ou=people, dc=lasierra, dc=edu");
        env.put(Context.SECURITY_CREDENTIALS, token);

        /*The Digest-MD5 SASL mechanism also supports the establishment of a negotiated security layer after successful authentication
        // Request privacy protection
        env.put("javax.security.sasl.qop", "auth-conf");

        // Request medium-strength cryptographic protection
        env.put("javax.security.sasl.strength", "medium");
        
        // Request a maximum receive buffer size of 16384 bytes
        env.put("javax.security.sasl.maxbuf", "16384");
        */
        env.put("com.sun.jndi.ldap.trace.ber", System.out);

        try {
            // Create initial context
            DirContext ctx = new InitialDirContext(env);
            res = 1;            
            ctx.close();
        } catch(AuthenticationException aex) {
            //System.out.println("authLDAPS() Invalid userid or password... ");   
            res = 0;
        }catch( AuthenticationNotSupportedException anse){
            log.info("authLDAPS() unsupported auth method ");
            res=-2;
        } catch (NamingException e) {
            e.printStackTrace();
            res = 0;
        }
        return res;
    }
    
    
    
    //create an initial context to an LDAP server using External authentication and SSL
    public void initLDAPSExtAuth(){
        // Set up environment for creating initial context
        Hashtable<String, Object> env = new Hashtable<String, Object>(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://localhost:636/o=JNDITutorial");

        // Principal & credentials will be obtained from the connection
        env.put(Context.SECURITY_AUTHENTICATION, "EXTERNAL");

        // Specify SSL
        env.put(Context.SECURITY_PROTOCOL, "ssl");

        try {
            // Create initial context
            DirContext ctx = new InitialDirContext(env);

            System.out.println(ctx.lookup("ou=NewHires"));

            // do something useful with ctx

            // Close the context when we're done
            ctx.close();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    
          
    
    
    /*
To access the LDAP service, the LDAP client first must authenticate itself to the service. That is, it must tell the LDAP server who is going to be accessing the 
* data so that the server can decide what the client is allowed to see and do. If the client authenticates successfully to the LDAP server, then when the server 
* subsequently receives a request from the client, it will check whether the client is allowed to perform the request. This process is called access control.

The LDAP standard has proposed ways in which LDAP clients can authenticate to LDAP servers (RFC 2251 and RFC 2829). These are discussed in general in the LDAP 
* Authentication section and Authentication Mechanisms section. This lesson also contains descriptions of how to use the anonymous, simple and SASL authentication
* mechanisms.

Access control is supported in different ways by different LDAP server implementations. It is not discussed in this lesson.

Another security aspect of the LDAP service is the way in which requests and responses are communicated between the client and the server. Many LDAP servers
* support the use of secure channels to communicate with clients, for example to send and receive attributes that contain secrets, such as passwords and keys. 
* LDAP servers use SSL for this purpose. This lesson also shows how to use SSL with the LDAP service provider.
* 
* The LDAP v3 (RFC 2251) is designed to address some of the limitations of the LDAP v2 in the areas of internationalization, authentication, referral, and deployment. 
* It also allows new features to be added to the protocol without also requiring changes to the protocol. This is done by using extensions and controls.
* 
* The LDAP v3 uses the Simple Authentication and Security Layer (SASL) authentication framework (RFC 2222) to allow different authentication mechanisms to be used 
* with the LDAP. SASL specifies a challenge-response protocol in which data is exchanged between the client and the server for the purposes of authentication.

Several SASL mechanisms are currently defined: DIGEST-MD5, CRAM-MD5, Anonymous, External, S/Key, GSSAPI, and Kerberos v4. An LDAP v3 client can use any of these 
* SASL mechanisms, provided that the LDAP v3 server supports them. Moreover, new (yet-to-be defined) SASL mechanisms can be used without changes to the LDAP 
* having to be made.
* 
* 
* LDAP server must be set up with an X.509 SSL server certificate and have SSL enabled. Typically, you must first obtain a signed certificate for the server 
* from a certificate authority (CA). 
* Then, follow the instructions from your directory vendor on how to enable SSL. Different vendors have different tools for doing this.
* 
* Once the Client has JSSE  installed and configured, you need to ensure that the client trusts the LDAP server that you'll be using. 
* You must install the server's certificate (or its CA's certificate) in your JRE's database of trusted certificates. Here is an example.
# cd JAVA_HOME/lib/security
# keytool -import -file server_cert.cer -keystore jssecacerts
*/
    
    
    //generate self-sign certification
    //LDAP over SSL   using port 636
    //TLS   is harder, use 389, but with StartTLS it starts clear text, then converts to using encryption
    public void LDAPSSLquery(){
        String INITCTX = "com.sun.jndi.ldap.LdapCtxFactory";
        String MY_HOST = "ldap://KhooGP-Comp3:1389";
        String MGR_DN = "cn=Directory Manager";
        String MGR_PW = "password";
        String MY_SEARCHBASE = "ou=User,o=IT,dc=QuizPortal";
        String MY_FILTER = "uid=defaultuser";
        String MY_ATTRS[] = {"cn", "telephoneNumber", "userPassword"};

        //Identify service provider to use
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITCTX);
        env.put(Context.PROVIDER_URL, MY_HOST);

        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, MGR_DN);
        env.put(Context.SECURITY_CREDENTIALS, MGR_PW);

        try{
            // Create the initial directory context
            InitialDirContext initialContext = new InitialDirContext(env);
            DirContext ctx = (DirContext)initialContext;

            System.out.println("Context Sucessfully Initialized");

            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration results = ctx.search(MY_SEARCHBASE, MY_FILTER, constraints);

            while(results != null && results.hasMore()){
                SearchResult sr = (SearchResult) results.next();
                String dn = sr.getName() + "," + MY_SEARCHBASE;
                System.out.println("Distinguished Name is " + dn);

                Attributes ar = ctx.getAttributes(dn, MY_ATTRS);

                if(ar == null){
                    System.out.println("Entry " + dn);
                    System.out.println(" has none of the specified attributes\n");
                }else{
                    for(int i=0; i<MY_ATTRS.length; i++){
                        Attribute attr = ar.get(MY_ATTRS[i]);
                        System.out.println(MY_ATTRS[i] + ":");

                        for(Enumeration vals=attr.getAll(); vals.hasMoreElements();) {
                            System.out.println("\t" + vals.nextElement());
                        }
                    }
                }
            }
        }catch(Exception e){
            System.err.println(e);
        }
    }
    
    
    /*
    //SSL keytool and cert
    //Register ssl certificate using keytool:
    //keytool -import -alias <certname> -file <filename.crt> -keystore "..yourpath\java\jre\lib\security\cacerts"
    public void bindLDAPSKeyCert(){
        String keystore = System.getProperty("" + "/lib/security/cacerts");
        System.setProperty( LDAPConstants.LDAP_SSL_TRUST_STORE, keystore);

        try{
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldap://yourservername:636");
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PROTOCOL, "ssl");

            env.put(Context.SECURITY_PRINCIPAL, "yourusername");
            env.put(Context.SECURITY_CREDENTIALS, "yourpassword");

            LdapContext dirCtx = new InitialLdapContext(env, null);

            NamingEnumeration ne = null;
            SearchControls controls =  new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            ne = dirCtx.search("OU=Users,DC=yourcompany, DC=com, DC=au","userName="+userName, controls);

            if (ne != null) {
                if (ne.hasMore()) {
                    SearchResult item = (SearchResult) ne.next();
                        display(item.getAttributes());
                }
            }

        }catch(javax.naming.AuthenticationException e){
            e.printStackTrace();
        }catch(NamingException e) {
            e.printStackTrace();
        }
         
    }
    private static void display(Attributes attr) throws NamingException{
        NamingEnumeration ne = attr.getAll();
        while(ne.hasMore()){
                Attribute  obj = (Attribute)ne.next();
                System.out.println(obj.getID()+"\t"+(String)obj.get(0));
        }
    }       
    
    */
    
    
    //SSL
    public void bindLDAPSSLKeyStore(){
        String principal = "user name";
        String credentials = "password";
        String baseDN = "ldap://ldaphost:636";
        System.out.println("Starting to execute");
        //LDAPConn testUser = new LDAPConn();
        System.out.println("Binding...");
        //testUser.bindAs( principal, credentials, baseDN );
        bindAs( principal, credentials, baseDN );
        System.out.println( "Fininished....!");

    }
    private void bindAs ( String principal, String credentials, String baseDN) {
        Hashtable<String, Object> env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY,        "com.sun.jndi.ldap.LdapCtxFactory");

        env.put(Context.SECURITY_AUTHENTICATION, "simple");

        env.put(Context.REFERRAL, "ignore");
        env.put(Context.SECURITY_PROTOCOL, "ssl");
        //THE LOCATION OF THE CACERTS MUST BE SPECIFIED
        java.security.Security.addProvider(new        com.sun.net.ssl.internal.ssl.Provider());
        System.setProperty("javax.net.ssl.keyStore",        "c:\\j2sdk1.4.0_01\\jre\\lib\\security\\cacert s");
        System.setProperty("javax.net.ssl.trustStore",        "c:\\j2sdk1.4.0_01\\jre\\lib\\security\\cacert s");
        System.setProperty("javax.net.ssl.trustStoreType", "jks");
        env.put(Context.PROVIDER_URL, baseDN );
        env.put(Context.SECURITY_PRINCIPAL, principal);
        env.put(Context.SECURITY_CREDENTIALS, credentials);
        DirContext ctx = null;
        
        try {
            ctx = new InitialDirContext(env);
            System.out.println("bind Successful...");
            ctx.close();
        } catch(AuthenticationException aex) {
            System.out.println("Invalid userid or password... Please try again");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    } // bindAs
    
    
    public void timeRemoteServer(){
        String[] hosts = {"dorm.lasierra.edu" , "dw2.lasierra.edu", "www.lasierra.edu", "ldap.lasierra.edu"};
        String[] hosts2 = {"http://dorm.lasierra.edu" , "http://dw2.lasierra.edu", "http://www.lasierra.edu", "http://ldap.lasierra.edu"};
        long start=0, end=0;
        for( String one : hosts2 ){
            try{
                start = System.currentTimeMillis();
                final URLConnection connection = new URL(one).openConnection();
                connection.connect();
                end = System.currentTimeMillis() - start;
                log.info("URLConnection Server %s available. time: %d", one, end);
                //available = true;
            } catch(final MalformedURLException e){
                //throw new IllegalStateException("Bad URL: " + url, e);
                log.info("URLConnection Server %s Bad URL", one); //##########################################
            } catch(final IOException e){
                log.info("URLConnection Server %s unavailable", one);
                //available = false;
            }
        }
        
        Socket socket = null;
        boolean reachable = false;            
        for( String one : hosts ){            
            try {
                //java.net.Socket.setSoTimeout() --cause InterruptedIOException--- socket options to generate a timeout after a read operation blocks for a specified length of time
                
                /*
When a socket wishes to terminate a connection it can "linger", allowing unsent data to be transmitted, or it can "reset" which means that all unsent data will be lost. 
You can explicitly set a delay before a reset is sent, giving more time for data to be read, or you can specify a delay of zero, meaning a reset will be sent as 
the java.net.Socket.close() method is invoked.

The socket option SO_LINGER controls whether a connection will be aborted, and if so, the linger delay. Use the java.net.Socket.setSoLinger method, which accepts as parameters 
a boolean and an int. The boolean flag will activate/deactivate the SO_LINGER option, and the int will control the delay time.
* 
* 
socket option TCP_NODELAY, which allows applications to enable or disable Nagle's algorithm. Nagle's algorithm (described in RFC 896), conserves bandwidth by minimizing the number 
* of segments that are sent. When applications wish to decrease network latency and increase performance, they can disable Nagle's algorithm. Data will be sent earlier, at the cost of 
* an increase in bandwidth consumption.
                 */
                start = System.currentTimeMillis();
                
                //SO_TIMEOUT, by using the setSoTimeout() method. This method is supported by java.net.Socket, java.net.DatagramSocket, and java.net.ServerSocket. 
                // Set SO_TIMEOUT for five seconds
                //MyServerSocket.setSoTimeout(5000);
                socket = new Socket(one, 80);
                
/*
 System.out.println ("Starting timer.");
// Start timer
Timer timer = new Timer(3000);
timer.start();
  
// Connect to some host
Socket socket = new Socket ("localhost", 2000);
System.out.println ("Connected to localhost:2000");

// Reset timer - timeout can occur on connect
timer.reset();

// Create a print stream for writing
PrintStream pout = new PrintStream (         socket.getOutputStream() );

// Create a data input stream for reading
DataInputStream din = new DataInputStream(         socket.getInputStream() );

// Print hello msg
pout.println ("Hello world!");

// Reset timer - timeout is likely to occur during the read
timer.reset();

// Print msg from server
System.out.println (din.readLine());

// Shutdown timer
timer.stop();

// Close connection
socket.close();                 
 */                
                
                end = System.currentTimeMillis() - start;
                log.info("socket Server %s available. time: %d", one, end);
                //reachable = true;
            } catch (UnknownHostException ex) {
                log.info("socket Server %s UnknownHost", one );
            } catch (IOException ex) {
                log.info("socket Server %s IOException", one );
            }finally {            
                if (socket != null) try { socket.close(); } catch(IOException e) {}
            }
        }
        
        reachable =false;
        for( String one : hosts ){            
            try {
                start = System.currentTimeMillis();
                reachable = InetAddress.getByName(one).isReachable(1000); //ms?
                end = System.currentTimeMillis() - start;
                log.info("InetAddress Server %s available? %s.  time: %d", one, reachable,end);
            }catch(Exception e){
                log.info("InetAddress Server %s exception: %s", one, e.getMessage());
            }
        }
        
        for( String one : hosts2 ){            
            try {
                start = System.currentTimeMillis();
                HttpURLConnection connection = (HttpURLConnection) new URL(one).openConnection();
                connection.setRequestMethod("HEAD");
                int responseCode = connection.getResponseCode();
                end = System.currentTimeMillis() - start;
                log.info("HttpURLConnection Server %s code %d.  time: %d", one, responseCode,end);
                if (responseCode != 200) {
                    // Not OK.
                }
                // < 100 is undertermined.
                // 1nn is informal (shouldn't happen on a GET/HEAD)
                // 2nn is success
                // 3nn is redirect
                // 4nn is client error
                // 5nn is server error
            }catch(Exception e){
                log.info("HttpURLConnection Server %s exception: %s", one, e.getMessage());
            }
        }
        
        
        HttpURLConnection connection = null;
        for( String one : hosts2 ){ 
            try {
                URL u = new URL(one);
                start = System.currentTimeMillis();
                connection = (HttpURLConnection) u.openConnection();
                connection.setRequestMethod("HEAD");
                int code = connection.getResponseCode();
                end = System.currentTimeMillis() - start;
                log.info("HttpURLConnection2 Server %s code %d.  time: %d", one, code,end);
                 
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        /*
        Client client = new Client(Protocol.HTTP);
        Response response = client.get(url);
        if (response.getStatus().isError()) {
            // uh oh!
        } */
        
    }
    
}



