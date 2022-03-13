package com.cedric.fr;

/*
 * Class Services, regroupe toutes les m�thodes qui agissent directement sur les dames
 */

public class Services extends Utils{

	
	
	//TODO Arranger la fonction pour obtenir les 92 solutions, il faut pouvoir tester toutes les positions des reines sur leur ligne
	
	//TODO Attention, on rentre syst�matiquement le param�tre String[] tabChess dans les m�thodes
	
	/**
	 * @Author, GABETTE C�dric
	 * Fonction principale qui apporte une solution, en appelant les autres fonctions.
	 * @param String[] tabChess, le plateau actuel avec les reines plac�es
	 * @param square, on force le placement d'une dame sur la case donn�e
	 * @return void
	 * 
	 */
	public static void solution(String[] tabChess) {
		boolean foundSolution = false;
		boolean alreadyExists = false;
		boolean loop = true;
		boolean loop2 = true;
		int nbrSolution = 0;
		int i = 0;
		String tabMem[][] = new String[100][64];

		while(i<94) {

			while(loop) {				
				actionQueen(tabChess, "addQueen");		//On essaye de poser toutes les reines
				foundSolution = isSuccess(tabChess);	//Retourne true si on trouve les 8 reines sur le plateau
				if(foundSolution) {
					loop = false;
				}
			}
			getRidOfInterdictions(tabChess);		//Retire toutes les cases bloquantes inutiles
			
			for(int k=0;k<nbrSolution && loop2;k++) {
				if(isAlreadyExists(tabChess, tabMem[k])) {
					tabChess = findRoomOnRow(tabChess);
					loop2 = false;
					alreadyExists = true;
				} 		
			}
			
			if(!alreadyExists) {				
				tabMem[nbrSolution] = memorize(tabChess);
				nbrSolution++;
			}
			
			alreadyExists = false;			//On reinitialise pour rentrer � nouveau dans la boucle d'actionQueen et de isAlreadyExists
			loop = true;
			loop2 = true;
			i++;
		}
		for(int j=0;j<=92;j++) {
			System.out.println("Solution " + (j+1));
			display(tabMem[j]);
		}
	}

	



	
	/**
	 * @Author, GABETTE C�dric
	 * Donner le comportement aux reines, capturant en ligne, en colonne et en diagonale, on peut leur donner ou retirer leur comportement
	 * @param int square, la case o� l'on veut mettre ou retirer une dame 
	 * @param String[] tabChess, le plateau actuel avec les reines plac�es
	 * @param String mode, pour ajouter ou retirer une reine
	 */
	
	public static String[] queenModel(int square, String[] tabChess, String mode) {

		String UNDERATTACK = null;
		String EMPTY = null;

		if(mode == "addQueen") {
			UNDERATTACK = "."; 			//"." repr�sente les cases sous la capture des reines
			EMPTY = "o";				//"o" rep�sente une case vide, o� l'on peut placer une reine
		}
		if(mode == "removeQueen") {
			UNDERATTACK = "o";
			EMPTY = ".";	
		}
		if(mode == "refresh") {
			UNDERATTACK = ".";
			EMPTY = "o";				
		}

		//Capture en ligne
		for(int i=0;i<=square%8;i++) {			//vers la gauche
			if(tabChess[(square-i)] == EMPTY)
				tabChess[(square-i)] = UNDERATTACK;
		}
		for(int i=0;i<(8-square%8);i++) {		//vers la droite
			if(tabChess[(i+square)] == EMPTY)
				tabChess[(i+square)] = UNDERATTACK;
		}

		//Capture en colonne
		for(int i=0;i<=square/8;i++) { 		//vers le haut
			if(square/8 == 0) i=0; 
			if(tabChess[(square-i*8)] == EMPTY)
				tabChess[(square-i*8)] = UNDERATTACK;
		}
		for(int i=0;i<(8-square/8);i++) { 		//vers le bas	
			if(tabChess[(i*8+square)] == EMPTY)
				tabChess[(i*8+square)] = UNDERATTACK;
		}	

		//Capture en diagonale decroissante (D'en haut � gauche vers en bas � droite)
		for(int i=0;i<=(square%8);i++) {			//Dans le sens
			if((square-(i+i*8)) < 0) {
				i=0;
				break;
			}
			if(tabChess[(square-(i+i*8))] == EMPTY)
				tabChess[(square-(i+i*8))] = UNDERATTACK;
		}
		for(int i=0;i<(8-square%8);i++) {			//Dans le sens contraire
			if((square+(i+i*8)) >= 64 || square == 7) {
				i=0;
				break;
			}
			if(tabChess[((i+i*8)+square)] == EMPTY)
				tabChess[((i+i*8)+square)] = UNDERATTACK;
		}

		//Capture en diagonale croissante (De en bas � gauche vers en haut � droite)
		for(int i=0;i<=(8-square%8);i++) {			//Dans le sens
			if((square-(i+i*6)) < 0) {
				i=0;
				break;
			}
			if(tabChess[(square-(i+i*6))] == EMPTY)
				tabChess[(square-(i+i*6))] = UNDERATTACK;
		}
		for(int i=0;i<=(square%8);i++) {			//Dans le sens contraire
			if((square+(i+i*6)) >= 64) {
				i=0;
				break;
			}
			if(tabChess[((i+i*6)+square)] == EMPTY)
				tabChess[((i+i*6)+square)] = UNDERATTACK;
		}
		return tabChess;
	}

	/**
	 * @Author, GABETTE C�dric
	 * Action qu'on va donner � la dame
	 * @param String[] tabChess, le plateau actuel avec les reines plac�es
	 * @param String mode, l'action que l'on donne
	 * @return, le tableau avec la reine plac�e ou retir�e
	 */
	public static String[] actionQueen(String[] tabChess, String mode) {

		int square;
		String SET = null;
		String FIND = null;

		if(mode == "addQueen") {		//Dans le cas o� l'on veut placer une reine sur une case vide
			if(isNextRowFull(tabChess)) { 
				return tabChess;
			}
			FIND = "o";
			SET = "D";
		}
		if(mode == "removeQueen") {		//Dans le cas o� l'on veut retirer une reine et lib�rer la case
			FIND = "b";
			SET = "b";
		}

		for(square=0;square<tabChess.length;square++) {		//On parcourt l'ensemble des cases et on cherche le FIND
			if(tabChess[square] == FIND) {
				tabChess[square] = SET;
				queenModel(square, tabChess, mode);		//Appel de la m�thode queenModel pour modifier le plateau
				if(mode == "addQueen")		//Pour rajouter une dame sur la premi�re case qu'on recontre, soit la case la plus � gauche de
					break;					//chaque ligne
			}
		}
		return tabChess;
	}





	/**
	 * @Author, GABETTE C�dric
	 * V�rifie si la ligne suivante par rapport � la ligne courante de la dame poss�de une case vide accessible
	 * @param String[] tabChess, le plateau actuel avec les reines plac�es
	 * @return, true si la ligne suivante est innaccessible pour une reine
	 */
	public static boolean isNextRowFull(String[] tabChess) {
		boolean ret = false;
		int startRow = 0;
		int count = 0;
		int MAX = tabChess.length-1-8;  //On retire 8 pour ne pas prendre en compte la derni�re ligne du plateau


		for(int i=MAX;i>=0;i--) { 		//On vient trouver la derni�re dame plac�e sur le plateau
			if(tabChess[i] == "D") {
				startRow = i - (i%8) + 8;		//On se place le plus � gauche de la ligne suivante
				for(int squarePosition=0;squarePosition<8;squarePosition++) {		
					if(tabChess[startRow+squarePosition] == "." || tabChess[startRow+squarePosition] == "b" ) {   	//On parcourt et on cherche
						count++;																					//une case pouvant accueillir
					}																								//une reines
				}
				if(count == 8) {		//Si toutes les cases de la ligne suivante ne sont pas accessibles
					ret = true;
					count = 0;
					backTracking(tabChess);		//On se replace sur la derni�re reine jou�e
					break;
				}
			}
		}
		return ret;
	}		

	/**
	 * @Author, GABETTE C�dric
	 * Permet de retirer une reine sur une case non-pertinente et de se placer sur la reine pr�c�dement plac�e
	 * @param, String[] tabChess le plateau actuel avec les reines plac�es
	 * @return, le tableau avec la reine courante retir�e et l'index plac�e sur la dame pr�c�dente
	 */
	public static String[] backTracking(String[] tabChess) {

		boolean boucle = true;
		int startRow = 0;
		int MAX = tabChess.length-1;

		for(int curr=MAX;curr>=0 && boucle;curr--) {		//curr = position de la derni�re reine
			if(tabChess[curr] == "D") {
				tabChess[curr] = "b";					//On interdit (freeze) la case de la reine courante
				startRow = (curr - (curr%8) + 8);		//On se place le plus � gauche sur la ligne suivante par rapport � la ligne actuelle
				actionQueen(tabChess, "removeQueen");	//On retire la reine trouv�e
				refreshBoard(tabChess);					//On met � jours les cases vides
				endFreeze(tabChess, MAX, startRow);		//On enl�ve l'interdiction des cases se trouvant en dessous de la ligne courante
				boucle = false;
			}
		}
		return tabChess;
	}

	/**
	 * @Author, GABETTE C�dric
	 * Rafraich�t les captures sur les cases vides suite au retirement d'une reine
	 * @param String[] tabChess, le plateau actuel avec les reines plac�es
	 * @return void
	 */
	public static void refreshBoard(String[] tabChess) {
		int square = 0;

		for(square=0;square<tabChess.length;square++) {		//On parcourt toutes les cases du plateau pour trouver une reine
			if(tabChess[square] == "D") {
				queenModel(square, tabChess, "refresh");	//Appel de la m�thode queenModel pour modifier le plateau
			}
		}
	}
	


	

	/**
	 * @Author, GABETTE C�dric
	 * Permet de remonter les lignes du plateau et de trouver une autre case vide sur la m�me ligne o� se trouve d�j� une reine
	 * @param String[] tabChess, le plateau actuel avec les reines plac�es
	 * @return, le tableau avec le prochain coups � jouer sur la m�me ligne o� une dame a �t� jou�e
	 */

	public static String[] findRoomOnRow(String[] tabChess) {

		int MAX = tabChess.length-1;
		int startRow = 0;
		boolean boucle = true;

		while(boucle) {				
			backTracking(tabChess);
			for(int curr=MAX;curr>=0;curr--) {		//curr = position de la derni�re reine
				if(tabChess[curr] == "b") {
					startRow = (curr - (curr%8));		//On se place le plus � gauche sur la ligne actuelle
					for(int i=startRow;i<startRow+8;i++) {
						if(tabChess[i] == "o") {
							boucle = false;
							break;
						}
					}
				}
			}
			MAX = MAX - 8;
		}

		return tabChess;
	}

	
	


}

