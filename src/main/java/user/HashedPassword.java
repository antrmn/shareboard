package user;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class HashedPassword {
    private byte[] password;
    private byte[] salt;

    private static byte[] generateSalt(){
        SecureRandom ss = new SecureRandom();
        byte[] salt = new byte[16];
        ss.nextBytes(salt);
        return salt;
    }

    public static HashedPassword hash(String password){
        return hash(password, generateSalt());
    }

    public static HashedPassword hash(String password, byte[] salt){
        MessageDigest digest = null;
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();

            HashedPassword hp = new HashedPassword();
            hp.password = hash;
            hp.salt = salt;
            return hp;
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean check(String password){
        return this.equals(hash(password, this.salt));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashedPassword that = (HashedPassword) o;
        return Arrays.equals(password, that.password) && Arrays.equals(salt, that.salt);
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
}
