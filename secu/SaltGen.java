/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.lsu.estimator.secu;

import org.jasypt.salt.SaltGenerator;

/**
 *
 * @author kwang
 */
public class SaltGen implements SaltGenerator {
    byte[] salt8 = {
                        (byte)0xc3, (byte)0x5e, (byte)0x23, (byte)0xa5,
                        (byte)0x7f, (byte)0xf9, (byte)0xe9, (byte)0x99
                    };
    byte[] salt16 = {
                        (byte)0xc3, (byte)0x5e, (byte)0x23, (byte)0xa5,
                        (byte)0x7f, (byte)0xf9, (byte)0xe9, (byte)0x99,
                        (byte)0xf7, (byte)0x13, (byte)0x86, (byte)0x62,
                        (byte)0x41, (byte)0x37, (byte)0xd4, (byte)0xb0                        
                    };
    @Override
    public byte[] generateSalt(int lengthBytes) {
        //throw new UnsupportedOperationException("Not supported yet.");
        if( lengthBytes==8)return salt8;
        else return salt16;
    }

    @Override
    public boolean includePlainSaltInEncryptionResults() {
        //throw new UnsupportedOperationException("Not supported yet.");
        return false;
    }
    
}
