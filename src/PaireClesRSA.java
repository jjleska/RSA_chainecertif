import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class PaireClesRSA {
	private KeyPair key;
	private KeyPairGenerator kpg;
	
	PaireClesRSA() {
	try {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(2048);
		KeyPair key = kpg.generateKeyPair();
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

		// Constructeur : g�n�ration d�une paire de cl� RSA.
	}
	
	public PublicKey Publique() {
		return key.getPublic();
		// Recuperation de la cl� publique.
	}
	
	public PrivateKey Privee() {
		return key.getPrivate();
		// Recuperation de la cl� priv�e.
	}
}
