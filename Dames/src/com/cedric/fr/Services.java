package com.cedric.fr;

/*
 * Class Services, regroupe toutes les méthodes qui agissent directement sur les dames
 */

public class Services extends Utils{

	
	
	//TODO Arranger la fonction pour obtenir les 92 solutions, il faut pouvoir tester toutes les positions des reines sur leur ligne
	
	//TODO Attention, on rentre systématiquement le paramètre String[] tabChess dans les méthodes
	
	/**
	 * @Author, GABETTE Cédric
	 * Fonction principale qui apporte une solution, en appelant les autres fonctions.
	 * @param String[] tabChess, le plateau actuel avec les reines placées
	 * @param square, on force le placement d'une dame sur la case donnée
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
			
			alreadyExists = false;			//On reinitialise pour rentrer à nouveau dans la boucle d'actionQueen et de isAlreadyExists
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
	 * @Author, GABETTE Cédric
	 * Donner le comportement aux reines, capturant en ligne, en colonne et en diagonale, on peut leur donner ou retirer leur comportement
	 * @param int square, la case où l'on veut mettre ou retirer une dame 
	 * @param String[] tabChess, le plateau actuel avec les reines placées
	 * @param String mode, pour ajouter ou retirer une reine
	 */
	
	public static String[] queenModel(int square, String[] tabChess, String mode) {

		String UNDERATTACK = null;
		String EMPTY = null;

		if(mode == "addQueen") {
			UNDERATTACK = "."; 			//"." représente les cases sous la capture des reines
			EMPTY = "o";				//"o" repésente une case vide, où l'on peut placer une reine
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

		//Capture en diagonale decroissante (D'en haut à gauche vers en bas à droite)
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

		//Capture en diagonale croissante (De en bas à gauche vers en haut à droite)
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
	 * @Author, GABETTE Cédric
	 * Action qu'on va donner à la dame
	 * @param String[] tabChess, le plateau actuel avec les reines placées
	 * @param String mode, l'action que l'on donne
	 * @return, le tableau avec la reine placée ou retirée
	 */
	public static String[] actionQueen(String[] tabChess, String mode) {

		int square;
		String SET = null;
		String FIND = null;

		if(mode == "addQueen") {		//Dans le cas où l'on veut placer une reine sur une case vide
			if(isNextRowFull(tabChess)) { 
				return tabChess;
			}
			FIND = "o";
			SET = "D";
		}
		if(mode == "removeQueen") {		//Dans le cas où l'on veut retirer une reine et libérer la case
			FIND = "b";
			SET = "b";
		}

		for(square=0;square<tabChess.length;square++) {		//On parcourt l'ensemble des cases et on cherche le FIND
			if(tabChess[square] == FIND) {
				tabChess[square] = SET;
				queenModel(square, tabChess, mode);		//Appel de la méthode queenModel pour modifier le plateau
				if(mode == "addQueen")		//Pour rajouter une dame sur la première case qu'on recontre, soit la case la plus à gauche de
					break;					//chaque ligne
			}
		}
		return tabChess;
	}





	/**
	 * @Author, GABETTE Cédric
	 * Vérifie si la ligne suivante par rapport à la ligne courante de la dame possède une case vide accessible
	 * @param String[] tabChess, le plateau actuel avec les reines placées
	 * @return, true si la ligne suivante est innaccessible pour une reine
	 */
	public static boolean isNextRowFull(String[] tabChess) {
		boolean ret = false;
		int startRow = 0;
		int count = 0;
		int MAX = tabChess.length-1-8;  //On retire 8 pour ne pas prendre en compte la dernière ligne du plateau


		for(int i=MAX;i>=0;i--) { 		//On vient trouver la dernière dame placée sur le plateau
			if(tabChess[i] == "D") {
				startRow = i - (i%8) + 8;		//On se place le plus à gauche de la ligne suivante
				for(int squarePosition=0;squarePosition<8;squarePosition++) {		
					if(tabChess[startRow+squarePosition] == "." || tabChess[startRow+squarePosition] == "b" ) {   	//On parcourt et on cherche
						count++;																					//une case pouvant accueillir
					}																								//une reines
				}
				if(count == 8) {		//Si toutes les cases de la ligne suivante ne sont pas accessibles
					ret = true;
					count = 0;
					backTracking(tabChess);		//On se replace sur la dernière reine jouée
					break;
				}
			}
		}
		return ret;
	}		

	/**
	 * @Author, GABETTE Cédric
	 * Permet de retirer une reine sur une case non-pertinente et de se placer sur la reine précédement placée
	 * @param, String[] tabChess le plateau actuel avec les reines placées
	 * @return, le tableau avec la reine courante retirée et l'index placée sur la dame précédente
	 */
	public static String[] backTracking(String[] tabChess) {

		boolean boucle = true;
		int startRow = 0;
		int MAX = tabChess.length-1;

		for(int curr=MAX;curr>=0 && boucle;curr--) {		//curr = position de la dernière reine
			if(tabChess[curr] == "D") {
				tabChess[curr] = "b";					//On interdit (freeze) la case de la reine courante
				startRow = (curr - (curr%8) + 8);		//On se place le plus à gauche sur la ligne suivante par rapport à la ligne actuelle
				actionQueen(tabChess, "removeQueen");	//On retire la reine trouvée
				refreshBoard(tabChess);					//On met à jours les cases vides
				endFreeze(tabChess, MAX, startRow);		//On enlève l'interdiction des cases se trouvant en dessous de la ligne courante
				boucle = false;
			}
		}
		return tabChess;
	}

	/**
	 * @Author, GABETTE Cédric
	 * Rafraichît les captures sur les cases vides suite au retirement d'une reine
	 * @param String[] tabChess, le plateau actuel avec les reines placées
	 * @return void
	 */
	public static void refreshBoard(String[] tabChess) {
		int square = 0;

		for(square=0;square<tabChess.length;square++) {		//On parcourt toutes les cases du plateau pour trouver une reine
			if(tabChess[square] == "D") {
				queenModel(square, tabChess, "refresh");	//Appel de la méthode queenModel pour modifier le plateau
			}
		}
	}
	


	

	/**
	 * @Author, GABETTE Cédric
	 * Permet de remonter les lignes du plateau et de trouver une autre case vide sur la même ligne où se trouve déjà une reine
	 * @param String[] tabChess, le plateau actuel avec les reines placées
	 * @return, le tableau avec le prochain coups à jouer sur la même ligne où une dame a été jouée
	 */

	public static String[] findRoomOnRow(String[] tabChess) {

		int MAX = tabChess.length-1;
		int startRow = 0;
		boolean boucle = true;

		while(boucle) {				
			backTracking(tabChess);
			for(int curr=MAX;curr>=0;curr--) {		//curr = position de la dernière reine
				if(tabChess[curr] == "b") {
					startRow = (curr - (curr%8));		//On se place le plus à gauche sur la ligne actuelle
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

