package kuvaldis.play.jca;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;

import static org.junit.Assert.*;

public class JcaTest {

    @Test
    public void testDifferentProvider() throws Exception {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final MessageDigest bcMd = MessageDigest.getInstance("MD5", new BouncyCastleProvider());
        assertEquals("MD5", md.getAlgorithm());
        assertEquals("MD5", bcMd.getAlgorithm());
        assertNotSame(md.getClass(), bcMd.getClass());
        assertArrayEquals(md.digest("1".getBytes("UTF-8")), bcMd.digest("1".getBytes("UTF-8")));
    }

    @Test
    public void testDigitalSignature() throws Exception {
        final byte[] bytes = "1".getBytes("UTF-8");
        final KeyPairGenerator generator = KeyPairGenerator.getInstance("DSA");
        generator.initialize(1024);
        final KeyPair keyPair = generator.generateKeyPair();

        // client has public key for instance
        final PublicKey publicKey = keyPair.getPublic();
        // server has private and public key stored
        final PrivateKey privateKey = keyPair.getPrivate();

        final Signature serverSideSignature = Signature.getInstance("SHA1withDSA");
        serverSideSignature.initSign(privateKey);
        serverSideSignature.update(bytes);
        final byte[] digitalSignature = serverSideSignature.sign();

        // client has public key, data and signature and is able to verify it
        final Signature clientSideSignature = Signature.getInstance("SHA1withDSA");
        clientSideSignature.initVerify(publicKey);
        clientSideSignature.update(bytes);
        assertTrue(clientSideSignature.verify(digitalSignature));
    }

    @Test
    public void testSymmetricEncryption() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        final String algorithm = "Blowfish";
        final KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        final SecretKey secretKey = keyGenerator.generateKey();
        final byte[] key = secretKey.getEncoded();
        encodeDecodeCheck(algorithm, key);
    }

    @Test
    public void testGenerateSecretKeyFromPassword() throws Exception {
        final byte[] password = "123456".getBytes("UTF-8");
        final MessageDigest sha = MessageDigest.getInstance("SHA-1");
        final byte[] shaPassword = sha.digest(password);
        final SecretKeySpec secretKeySpec = new SecretKeySpec(shaPassword, "AES");
        // just to show we can use SecretKeyFactory to convert keys for different algorithms
        final SecretKey secretKey = SecretKeyFactory.getInstance("DES").generateSecret(secretKeySpec);
        final byte[] key = secretKey.getEncoded();
        encodeDecodeCheck("DES", key);
    }

    @Test
    public void testDiffieHellmanKeyAgreement() throws Exception {
        final KeyPairGenerator bobGenerator = KeyPairGenerator.getInstance("DH");
        final KeyPair bobKeys = bobGenerator.generateKeyPair();
        final KeyPairGenerator aliceGenerator = KeyPairGenerator.getInstance("DH");
        final KeyPair aliceKeys = aliceGenerator.generateKeyPair();
        final KeyPairGenerator carolGenerator = KeyPairGenerator.getInstance("DH");
        final KeyPair carolKeys = carolGenerator.generateKeyPair();

        final KeyAgreement aliceSide = KeyAgreement.getInstance("DH");
        aliceSide.init(aliceKeys.getPrivate());
        final KeyAgreement bobSide = KeyAgreement.getInstance("DH");
        bobSide.init(bobKeys.getPrivate());
        final KeyAgreement carolSide = KeyAgreement.getInstance("DH");
        carolSide.init(carolKeys.getPrivate());

        // to establish secret key between three parties we should pass intermediate keys
        final Key aliceAndCarol = aliceSide.doPhase(carolKeys.getPublic(), false); // means not the last phase
        final Key bobAndAlice = bobSide.doPhase(aliceKeys.getPublic(), false);
        final Key carolAndBob = carolSide.doPhase(bobKeys.getPublic(), false);

        aliceSide.doPhase(carolAndBob, true);
        bobSide.doPhase(aliceAndCarol, true);
        carolSide.doPhase(bobAndAlice, true);

        final byte[] aliceSecretKey = aliceSide.generateSecret();
        final byte[] bobSecretKey = bobSide.generateSecret();
        final byte[] carolSecretKey = carolSide.generateSecret();

        assertArrayEquals(aliceSecretKey, bobSecretKey);
        assertArrayEquals(aliceSecretKey, carolSecretKey);
    }

    private void encodeDecodeCheck(String algorithm, byte[] key) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(key, algorithm);
        final byte[] data = "1".getBytes("UTF-8");
        final Cipher encipher = Cipher.getInstance(algorithm);
        encipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        final byte[] encryptedData = encipher.doFinal(data);

        final Cipher decipher = Cipher.getInstance(algorithm);
        decipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        final byte[] decryptedData = decipher.doFinal(encryptedData);
        assertEquals("1", new String(decryptedData));
    }
}
