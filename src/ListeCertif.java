import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ListeCertif extends HashMap <PublicKey,Certificat> {
	public void afficheCA() {
		System.out.println("Equipements en liaison directe :"+"\n" );
		for (PublicKey key : this.keySet()) {
			System.out.println("Nom :" + this.get(key).getIssuer() );
			System.out.println("cle publique : " + key.toString() );
			System.out.println("cle protegee : " + this.get(key).pubkey.toString() );
			
			System.out.println("Signature : "+this.get(key).getSignature()+"\n");
		}
	}
	public void afficheDA(){
		System.out.println("Les certificats de notre r�seau domestique :" );
		for (PublicKey key : this.keySet()) {
			System.out.println(this.get(key).getIssuer() +"=>"+this.get(key).getDest());
			System.out.println("Signature :"+this.get(key).getSignature()+"\n");

		}
	}

	//assuming A is in DA of the source equipment 
	public ArrayList<Certificat> certifChain(ListeCertif CA_Dest, ListeCertif CA_this)
	{
		ArrayList<PublicKey> sommets_visites = new ArrayList<PublicKey>();
		ArrayList<Certificat> chemin = new ArrayList<Certificat>();
		ArrayList<ArrayList<Certificat>> liste_chemins = new ArrayList<ArrayList<Certificat>>();
		
		for (PublicKey key : CA_Dest.keySet())
		{			
			liste_chemins.add(aux(CA_Dest.get(key).pubkey, CA_this, sommets_visites, chemin));
		}
		for (int i = 0; i<CA_Dest.size();i++)
		{
			System.out.print("On renvoie le resultat du parcours : ");
			System.out.println(liste_chemins.get(i));
			if(liste_chemins.get(i) != null)
				return liste_chemins.get(i);
		}
		System.out.println("topis");
		return null;
		
		
		
	}
	
	private ArrayList<Certificat> aux (PublicKey pubkey, ListeCertif CA_this, ArrayList<PublicKey> sommets_visites, ArrayList<Certificat> chemin){
		sommets_visites.add(pubkey);
		
		if(!this.containsKey(pubkey)){
			System.out.println("wrong key, problem on first entry");
			return null;
		}
		int has_next = 0;
		for(PublicKey key2 : this.keySet()) //explore all DA keys
		{
			//existe pa
			if(pubkey.equals(key2)&& this.get(key2).verifCertif(key2)) //if we find the next element  
			{
				has_next += 1;
				if(CA_this.containsKey(this.get(key2).pubkey)){
					ArrayList<Certificat> temp_way = new ArrayList<Certificat>();
					Collections.copy(temp_way, chemin);
					temp_way.add(this.get(key2));
					return temp_way;
				}
				if(!this.containsKey(this.get(key2).pubkey)){
					System.out.println("fail 1");
					return null;
				}
				

				if(!sommets_visites.contains(key2)){
					sommets_visites.add(key2);
					ArrayList<Certificat> temp_way = new ArrayList<Certificat>();
					Collections.copy(temp_way, chemin);
					temp_way.add(this.get(key2));
					
					aux(key2, CA_this, sommets_visites, temp_way);
					
				}
			}
		}
		if (has_next == 0){
			System.out.println("fail 2");
			return null;
		}
		else{
			System.out.println("fail 3");
			return null;
		}
	}
	
	/*	public ArrayList<Certificat> certifChain( ListeCertif CA_Dest, ListeCertif CA_this)
	{		
		ArrayList<ArrayList<Certificat>> ways = new ArrayList<ArrayList<Certificat>>();
		for(int i = 0; i<CA_this.size();i++){
			ways.add(new ArrayList<Certificat>());
		}
		
		ArrayList<Integer> right_ways = new ArrayList<Integer>();


		int i = 0;
		for(PublicKey key : CA_this.keySet()) //compute one way for each CA element
		{
			ways.get(i).add(CA_this.get(key)); //add first element
			boolean no_more_next = false;

			PublicKey temp_key = key;

			while(!(CA_Dest.containsKey(temp_key)) && !no_more_next) //we get out if the key is in CA_Dest or there is no more next
			{
				no_more_next = true;
				for(PublicKey key2 : this.keySet()) //explore all DA keys
				{
					if(this.get(temp_key).pubkey == key2 && this.get(key2).verifCertif(key2)) //if we find the next element
					{
						ways.get(i).add(this.get(temp_key)); //we add it to the way
						temp_key = key2; 
						no_more_next = false; //we just found a next
						break; //no need to continue exploration
					}
				}
			}
			if(!no_more_next) //if we got out of the loop the right way
			{	
				right_ways.add(i); //then it is a right way !
				ways.get(i).add(CA_Dest.get(temp_key)); //we can add the last element
			}

			i++;
		}


		//check valid chains
		if (right_ways.isEmpty()){
			System.out.println("Error");
			return null;
		}	

		else{
			int right_index = 0;

			for(i = 1; i<right_ways.size();i++){
				if(ways.get(i).size() < ways.get(right_index).size()){
					right_index = i;
				}
			}
			return ways.get(right_ways.get(right_index));
		}
	}*/
}
