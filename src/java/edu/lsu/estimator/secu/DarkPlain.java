/*     */ package edu.lsu.estimator.secu;

/*     */
 /*     */ import com.kingombo.slf5j.Logger;
/*     */ import com.kingombo.slf5j.LoggerFactory;

import edu.lsu.estimator.AppReference;
import edu.lsu.estimator.Counselor;

/*     */
 /*     */ import java.io.IOException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.InetAddress;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.Socket;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.Security;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import javax.ejb.Stateful;
/*     */ import javax.inject.Inject;
/*     */ import javax.inject.Named;
/*     */ import javax.naming.AuthenticationException;
/*     */ import javax.naming.AuthenticationNotSupportedException;
/*     */ import javax.naming.CommunicationException;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.Attribute;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.directory.InitialDirContext;
/*     */ import javax.naming.directory.SearchControls;
/*     */ import javax.naming.directory.SearchResult;

/*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */ @Stateful
/*     */ @Named("darkPlain")
/*     */ public class DarkPlain /*     */ {

    /*  42 */ private static final Logger log = LoggerFactory.getLogger();
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
 /*     */    @Inject
    /*     */ AppReference ref;

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
 /*     */ public String genAuthenPWDDigest(String plainStr) {
        /*  82 */ String digest = this.ref.getDigester().digest(plainStr);
        /*  83 */ return digest;
        /*     */    }

    /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */ public String digestPWD(String plainStr, String saveDigest) {
        /*  95 */ if (this.ref.getDigester().matches(plainStr, saveDigest));
        /*     */
 /*     */
 /*     */
 /*     */
 /* 100 */ return "";
        /*     */    }

    /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */ public int authLDAPS(String who, String token, Counselor user) {
        /* 109 */ int res = -1;
        /* 110 */ log.info("######################### authLDAPS() got user name %s", new Object[]{who});
        /* 111 */ if (who == null || who.trim().isEmpty() || token == null || token.trim().isEmpty()) {
            /* 112 */ return res;
            /*     */        }
        /*     */
 /*     */
 /* 116 */ Hashtable<String, Object> env = new Hashtable<>(11);
        /* 117 */ env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        /*     */
 /*     */
 /* 120 */ env.put("java.naming.provider.url", "ldaps://ldap.lasierra.edu:636");
 //env.put("java.naming.provider.url", "ldaps://ad.lasierra.edu:636");
        /*     */
 /* 122 */ env.put("java.naming.security.protocol", "ssl");
        /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /* 130 */ env.put("java.naming.ldap.version", "3");
        /*     */
 /*     */
 /* 133 */ env.put("java.naming.security.authentication", "simple");
        /*     */
 /*     */
 /*     */
 /*     *
 /*     */
 /*     */
 /*     */
 /* 143 */ env.put("java.naming.security.principal", "cn=" + who.trim() + ((user.getStdInd()==null ||  user.getStdInd().intValue() == 0) ? ",ou=employee," : ",ou=student,") + "ou=people,dc=lasierra,dc=edu");
        /* 144 */ env.put("java.naming.security.credentials", token);
        /*     */
 /*     */
 /*     */
 /* 148 */ env.put("com.sun.jndi.ldap.connect.timeout", "5000");
        /*     */
 /*     */
 /* 151 */ env.put("com.sun.jndi.ldap.read.timeout", "5000");
        /*     */
 /*     */ try {
            /* 154 */ DirContext ctx = new InitialDirContext(env);
            /*     */
 /*     */
 /* 157 */ res = 1;
            /*     */
 /*     */
 /* 160 */ log.info("################# authLDAPS() Context Sucessfully Initialized. Binded. User %s is authenticated.", new Object[]{who});
            /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /* 166 */ ctx.close();
            /* 167 */        } catch (AuthenticationException aex) {
            /* 168 */ log.info("############## authLDAPS() AuthenticationException: %s", new Object[]{aex.getMessage()});
            /* 169 */ log.info("############## authLDAPS() principal=%s", new Object[]{env.get("java.naming.security.principal")});
            /* 170 */ aex.printStackTrace();
            /* 171 */ res = 0;
            /* 172 */        } catch (AuthenticationNotSupportedException anse) {
            /* 173 */ log.info("############## authLDAPS() unsupported auth method: %s ", new Object[]{anse.getMessage()});
            /* 174 */ res = -2;
            /* 175 */        } catch (CommunicationException ste) {
            /* 176 */ log.info("################# authLDAPS() Communication Exception : %s", new Object[]{ste.getMessage()});
            /* 177 */ ste.printStackTrace();
            /*     */
 /* 179 */ res = -3;
            /* 180 */        } catch (NamingException e) {
            /* 181 */ log.info("################# authLDAPS() naming exception: %s", new Object[]{e.getMessage()});
            /* 182 */ e.printStackTrace();
            /* 183 */ res = 0;
            /* 184 */        } catch (Exception e) {
            /* 185 */ log.info("################# authLDAPS()  exception: %s", new Object[]{e.getMessage()});
            /* 186 */ e.printStackTrace();
            /* 187 */ res = -4;
            /*     */        }
        /*     */
 /*     */
 /* 191 */ return res;
        /*     */    }

    /*     */
 /*     */
 /*     */
 /*     */ public int authLdapsMD5(String who, String token) {
        /* 197 */ int res = -1;
        /* 198 */ if (who == null || who.trim().isEmpty() || token == null || token.trim().isEmpty()) {
            /* 199 */ return res;
            /*     */        }
        /* 201 */ Hashtable<String, Object> env = new Hashtable<>(11);
        /* 202 */ env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        /* 203 */ env.put("java.naming.provider.url", "ldaps://ldap.lasierra.edu:636");
        /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /* 210 */ env.put("java.naming.security.authentication", "DIGEST-MD5");
        /* 211 */ env.put("java.naming.security.sasl.realm", "dc=lasierra, dc=edu");
        /*     */
 /* 213 */ env.put("java.naming.security.principal", "uid=" + who.trim() + ",ou=employee,ou=people, dc=lasierra, dc=edu");
        /* 214 */ env.put("java.naming.security.credentials", token);
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
 /* 226 */ env.put("com.sun.jndi.ldap.trace.ber", System.out);
        /*     */
 /*     */
 /*     */ try {
            /* 230 */ DirContext ctx = new InitialDirContext(env);
            /* 231 */ res = 1;
            /* 232 */ ctx.close();
            /* 233 */        } catch (AuthenticationException aex) {
            /*     */
 /* 235 */ res = 0;
            /* 236 */        } catch (AuthenticationNotSupportedException anse) {
            /* 237 */ log.info("authLDAPS() unsupported auth method ");
            /* 238 */ res = -2;
            /* 239 */        } catch (NamingException e) {
            /* 240 */ e.printStackTrace();
            /* 241 */ res = 0;
            /*     */        }
        /* 243 */ return res;
        /*     */    }

    /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */ public void initLDAPSExtAuth() {
        /* 251 */ Hashtable<String, Object> env = new Hashtable<>(11);
        /* 252 */ env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        /*     */
 /* 254 */ env.put("java.naming.provider.url", "ldap://localhost:636/o=JNDITutorial");
        /*     */
 /*     */
 /* 257 */ env.put("java.naming.security.authentication", "EXTERNAL");
        /*     */
 /*     */
 /* 260 */ env.put("java.naming.security.protocol", "ssl");
        /*     */
 /*     */
 /*     */ try {
            /* 264 */ DirContext ctx = new InitialDirContext(env);
            /*     */
 /* 266 */ System.out.println(ctx.lookup("ou=NewHires"));
            /*     */
 /*     */
 /*     */
 /*     */
 /* 271 */ ctx.close();
            /* 272 */        } catch (NamingException e) {
            /* 273 */ e.printStackTrace();
            /*     */        }
        /*     */    }

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
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */ public void LDAPSSLquery() {
        /* 321 */ String INITCTX = "com.sun.jndi.ldap.LdapCtxFactory";
        /* 322 */ String MY_HOST = "ldap://KhooGP-Comp3:1389";
        /* 323 */ String MGR_DN = "cn=Directory Manager";
        /* 324 */ String MGR_PW = "password";
        /* 325 */ String MY_SEARCHBASE = "ou=User,o=IT,dc=QuizPortal";
        /* 326 */ String MY_FILTER = "uid=defaultuser";
        /* 327 */ String[] MY_ATTRS = {"cn", "telephoneNumber", "userPassword"};
        /*     */
 /*     */
 /* 330 */ Hashtable<Object, Object> env = new Hashtable<>();
        /* 331 */ env.put("java.naming.factory.initial", INITCTX);
        /* 332 */ env.put("java.naming.provider.url", MY_HOST);
        /*     */
 /* 334 */ env.put("java.naming.security.authentication", "simple");
        /* 335 */ env.put("java.naming.security.principal", MGR_DN);
        /* 336 */ env.put("java.naming.security.credentials", MGR_PW);
        /*     */
 /*     */
 /*     */ try {
            /* 340 */ InitialDirContext initialContext = new InitialDirContext(env);
            /* 341 */ DirContext ctx = initialContext;
            /*     */
 /* 343 */ System.out.println("Context Sucessfully Initialized");
            /*     */
 /* 345 */ SearchControls constraints = new SearchControls();
            /* 346 */ constraints.setSearchScope(2);
            /*     */
 /* 348 */ NamingEnumeration<SearchResult> results = ctx.search(MY_SEARCHBASE, MY_FILTER, constraints);
            /*     */
 /* 350 */ while (results != null && results.hasMore()) {
                /* 351 */ SearchResult sr = results.next();
                /* 352 */ String dn = sr.getName() + "," + MY_SEARCHBASE;
                /* 353 */ System.out.println("Distinguished Name is " + dn);
                /*     */
 /* 355 */ Attributes ar = ctx.getAttributes(dn, MY_ATTRS);
                /*     */
 /* 357 */ if (ar == null) {
                    /* 358 */ System.out.println("Entry " + dn);
                    /* 359 */ System.out.println(" has none of the specified attributes\n");
                    continue;
                    /*     */                }
                /* 361 */ for (int i = 0; i < MY_ATTRS.length; i++) {
                    /* 362 */ Attribute attr = ar.get(MY_ATTRS[i]);
                    /* 363 */ System.out.println(MY_ATTRS[i] + ":");
                    /*     */
 /* 365 */ for (Enumeration<?> vals = attr.getAll(); vals.hasMoreElements();) {
                        /* 366 */ System.out.println("\t" + vals.nextElement());
                        /*     */                    }
                    /*     */                }
                /*     */
 /*     */            }
            /* 371 */        } catch (Exception e) {
            /* 372 */ System.err.println(e);
            /*     */        }
        /*     */    }

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
 /*     */ public void bindLDAPSSLKeyStore() {
        /* 429 */ String principal = "user name";
        /* 430 */ String credentials = "password";
        /* 431 */ String baseDN = "ldap://ldaphost:636";
        /* 432 */ System.out.println("Starting to execute");
        /*     */
 /* 434 */ System.out.println("Binding...");
        /*     */
 /* 436 */ bindAs(principal, credentials, baseDN);
        /* 437 */ System.out.println("Fininished....!");
        /*     */    }

    /*     */
 /*     */ private void bindAs(String principal, String credentials, String baseDN) {
        /* 441 */ Hashtable<String, Object> env = new Hashtable<>();
        /* 442 */ env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        /*     */
 /* 444 */ env.put("java.naming.security.authentication", "simple");
        /*     */
 /* 446 */ env.put("java.naming.referral", "ignore");
        /* 447 */ env.put("java.naming.security.protocol", "ssl");
        /*     */

 /* 449 */ Security.addProvider(null);
        /* 450 */ System.setProperty("javax.net.ssl.keyStore", "c:\\j2sdk1.4.0_01\\jre\\lib\\security\\cacert s");
        /* 451 */ System.setProperty("javax.net.ssl.trustStore", "c:\\j2sdk1.4.0_01\\jre\\lib\\security\\cacert s");
        /* 452 */ System.setProperty("javax.net.ssl.trustStoreType", "jks");
        /* 453 */ env.put("java.naming.provider.url", baseDN);
        /* 454 */ env.put("java.naming.security.principal", principal);
        /* 455 */ env.put("java.naming.security.credentials", credentials);
        /* 456 */ DirContext ctx = null;
        /*     */
 /*     */ try {
            /* 459 */ ctx = new InitialDirContext(env);
            /* 460 */ System.out.println("bind Successful...");
            /* 461 */ ctx.close();
            /* 462 */        } catch (AuthenticationException aex) {
            /* 463 */ System.out.println("Invalid userid or password... Please try again");
            /* 464 */        } catch (Exception ex) {
            /* 465 */ ex.printStackTrace();
            /*     */        }
        /*     */    }

    /*     */
 /*     */
 /*     */
 /*     */ public void timeRemoteServer() {
        /* 472 */ String[] hosts = {"dorm.lasierra.edu", "dw2.lasierra.edu", "www.lasierra.edu", "ldap.lasierra.edu"};
        /* 473 */ String[] hosts2 = {"http://dorm.lasierra.edu", "http://dw2.lasierra.edu", "http://www.lasierra.edu", "http://ldap.lasierra.edu"};
        /* 474 */ long start = 0L, end = 0L;
        /* 475 */ for (String one : hosts2) {
            /*     */ try {
                /* 477 */ start = System.currentTimeMillis();
                /* 478 */ URLConnection uRLConnection = (new URL(one)).openConnection();
                /* 479 */ uRLConnection.connect();
                /* 480 */ end = System.currentTimeMillis() - start;
                /* 481 */ log.info("URLConnection Server %s available. time: %d", new Object[]{one, Long.valueOf(end)});
                /*     */            } /* 483 */ catch (MalformedURLException e) {
                /*     */
 /* 485 */ log.info("URLConnection Server %s Bad URL", new Object[]{one});
                /* 486 */            } catch (IOException e) {
                /* 487 */ log.info("URLConnection Server %s unavailable", new Object[]{one});
                /*     */            }
            /*     */        }
        /*     */
 /*     */
 /* 492 */ Socket socket = null;
        /* 493 */ boolean reachable = false;
        /* 494 */ for (String one : hosts) {
            /*     */
 /*     */
 /*     */
 /*     */ try {
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
 /* 511 */ start = System.currentTimeMillis();
                /*     */
 /*     */
 /*     */
 /*     */
 /* 516 */ socket = new Socket(one, 80);
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
 /* 553 */ end = System.currentTimeMillis() - start;
                /* 554 */ log.info("socket Server %s available. time: %d", new Object[]{one, Long.valueOf(end)});
                /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /* 561 */ if (socket != null) try {
                    socket.close();
                } catch (IOException iOException) {
                }
            } catch (UnknownHostException ex) {
                log.info("socket Server %s UnknownHost", new Object[]{one});
            } catch (IOException ex) {
                log.info("socket Server %s IOException", new Object[]{one});
            } finally {
                if (socket != null) try {
                    socket.close();
                } catch (IOException iOException) {
                }
                /*     */            }
            /*     */
 /*     */        }
        /* 565 */ reachable = false;
        /* 566 */ for (String one : hosts) {
            /*     */ try {
                /* 568 */ start = System.currentTimeMillis();
                /* 569 */ reachable = InetAddress.getByName(one).isReachable(1000);
                /* 570 */ end = System.currentTimeMillis() - start;
                /* 571 */ log.info("InetAddress Server %s available? %s.  time: %d", new Object[]{one, Boolean.valueOf(reachable), Long.valueOf(end)});
                /* 572 */            } catch (Exception e) {
                /* 573 */ log.info("InetAddress Server %s exception: %s", new Object[]{one, e.getMessage()});
                /*     */            }
            /*     */        }
        /*     */
 /* 577 */ for (String one : hosts2) {
            /*     */ try {
                /* 579 */ start = System.currentTimeMillis();
                /* 580 */ HttpURLConnection httpURLConnection = (HttpURLConnection) (new URL(one)).openConnection();
                /* 581 */ httpURLConnection.setRequestMethod("HEAD");
                /* 582 */ int responseCode = httpURLConnection.getResponseCode();
                /* 583 */ end = System.currentTimeMillis() - start;
                /* 584 */ log.info("HttpURLConnection Server %s code %d.  time: %d", new Object[]{one, Integer.valueOf(responseCode), Long.valueOf(end)});
                /* 585 */ if (responseCode != 200);
                /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */            } /* 594 */ catch (Exception e) {
                /* 595 */ log.info("HttpURLConnection Server %s exception: %s", new Object[]{one, e.getMessage()});
                /*     */            }
            /*     */        }
        /*     */
 /*     */
 /* 600 */ HttpURLConnection connection = null;
        /* 601 */ for (String one : hosts2) {
            /*     */
 /* 603 */ try {
                URL u = new URL(one);
                /* 604 */ start = System.currentTimeMillis();
                /* 605 */ connection = (HttpURLConnection) u.openConnection();
                /* 606 */ connection.setRequestMethod("HEAD");
                /* 607 */ int code = connection.getResponseCode();
                /* 608 */ end = System.currentTimeMillis() - start;
                /* 609 */ log.info("HttpURLConnection2 Server %s code %d.  time: %d", new Object[]{one, Integer.valueOf(code), Long.valueOf(end)});
                /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /*     */
 /* 618 */ if (connection != null) /* 619 */ {
                    connection.disconnect();
                }
            } catch (MalformedURLException malformedURLException) {
            } catch (IOException iOException) {
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            /*     */
 /*     */        }
        /*     */    }
    /*     */ }


/* Location:              D:\Projects\code\Estimator2\dist\estimator.war!\WEB-INF\classes\edu\lsu\estimator\secu\DarkPlain.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
