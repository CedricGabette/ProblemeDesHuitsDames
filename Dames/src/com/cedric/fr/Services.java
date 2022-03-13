package com.cedric.fr;

import java.util.ArrayList;
import java.util.List;

public class Services {

	//TODO Arranger la fonction pour obtenir les 92 solutions, il faut pouvoir tester toutes les positions des reines sur leur ligne
	
	//TODO Attention, répétitions de code pour parcourir le tableau afin d'identifier des élements
	
	//TODO Séparer les méthodes dans des classes adaptés
	
	//TODO Attention, on rentre systématiquement le paramètre String[] tabChess dans les méthodes
	
	/**
	 * @Author, GABETTE Cédric
	 * Fonction principale qui apporte la solution, en appelant les autres fonctions.
	 * @param String[] tabChess, le plateau actuel avec les reines placées
	 * @param square, on force le placement d'une dame sur la case donnée
	 * @return void
	 * 
	 */
	public static void solution(String[] tabChess, int square) {
		boolean foundSolution = false;
		boolean boucle = true;
		boolean disable = false;

		if(square < 0) {		//Pour laisser la fonction actionQueen placer une reine sur la case 0
			disable = true;
		} 
		else if(square >= 0)
		{
			disable = false;
		}
		
		if(!disable) {						//On bloque les cases qui précèdent la case que l'on cible seulement sur sa ligne
			for(int i=0;i<=square;i++) {
				tabChess[i] = "b";
			}					
		}

		if(square > 6 && square < 17) {		//Dans le cas ou on agit seulement sur la seconde ligne

			for(int i=0;i<=7;i++) {
				tabChess[i] = "o";
			}		
			for(int i=12;i<=square;i++) {
				tabChess[i] = "b";
			}				
		}

		while(boucle) {				
			actionQueen(tabChess, "addQueen");		//On essaye de poser toutes les reines
			foundSolution = isSuccess(tabChess);	//Retourne true si on trouve les 8 reines sur le plateau
			if(foundSolution) {
				boucle = false;
			}
		}
		getRidOfb(tabChess);		//Retire toutes les cases bloquantes inutiles
		display(tabChess);			//Affiche le plateau d'échec
	}


	/**
	 * @Author, GABETTE Cédric
	 * Vérifie si la solution actuel existe déjà dans la liste des solutions trouvées
	 * @param String[]tabChess, le plateau actuel avec les reines placées
	 * @param String[]tabMem, un tableau qui va contenir toutes les solutions touvées
	 * @return true, si une solution existe déjà
	 */
	public static boolean alreadyExists(String[] tabChess, String[] tabMem) {
		boolean ret = false;
		int count = 0;

		for(int i=0;i<tabChess.length;i++) {	//On parcourt toutes les cases du plateau et on comptabilise à chaque fois qu'il y a une
			if(tabChess[i] == tabMem[i]) {		//similitude
				count++;
			}
		}

		if(count == tabChess.length) {		//Si toutes les cases du plateau sont identiques
			ret = true;
		}

		return ret;
	}

	/**
	 * @Author, GABETTE Cédric
	 * Permet de mémoriser un plateau avec toutes les positions des dames dans un tableau
	 * @param String[]tabChess, le plateau actuel avec les reines placées
	 * @return, un plateau copié du tableau donné en paramètre
 	 */
	public static String[] memorize(String[] tabChess) {

		String[] tabMem = new String[tabChess.length];

		for(int i=0;i<64;i++) {
			tabMem[i] = tabChess[i];		//On copie toutes les cases du plateau
		}
		return tabMem;
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
	 * Vérifie si il y a les 8 reines de placée sur le plateau
	 * @param, String[] tabChess, le plateau actuel avec les reines placées
	 * @return true, si on a 8 reines
	 */
	public static boolean isSuccess(String[] tabChess){
		int success = 0;
		boolean ret = false;

		for(String elem : tabChess) {
			if(elem == "D") {		//On comptabilise à chaque fois que l'on trouve une reine
				success++;	
			}
		}

		if(success == 8) {
			ret = true;
		}
		return ret;
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
	 * Enlève les interdictions des cases sur toutes les cases en dessous de la ligne courant pour pouvoir placer une dame dessus
	 * @param String[] tabChess, le plateau actuel avec les reines placées
	 * @param int MAX, 
	 * @param int startRow, la case la plus à gauche de la ligne courante
	 * @return void
	 */
	public static void endFreeze(String[] tabChess, int MAX, int startRow) {

		for(int i=0;i<(MAX-startRow);i++) {		//On retire les interdictions sur les cases sauf sur la ligne courante
			if(tabChess[(startRow+i)] == "b") {
				tabChess[(startRow+i)] = "o";
			}
		}
	}

	/**
	 * @Author, GABETTE Cédric
	 * Permet de retirer toutes les cases bloquantes inutiles
	 * @param String[] tabChess, le plateau actuel avec les reines placées
	 * @return void
	 */
	public static void getRidOfb(String[] tabChess) {
		for(int i=0;i<tabChess.length;i++) {
			if(tabChess[i] == "b") {
				tabChess[i] = ".";
			}
		}
	}
	

//	//TODO Utiliser cette méthode pour trouver les 92 solutions
//	/**
//	 * @Author, GABETTE Cédric
//	 * Permet de remonter les lignes de plateau et de trouver une autre case vide sur la même ligne où se trouve déjà une reine
//	 * @param String[] tabChess, le plateau actuel avec les reines placées
//	 * @return, le tableau avec le prochain coups à jouer sur la même ligne où une dame a été jouée
//	 */
//
//	public static String[] findAgain(String[] tabChess) {
//
//		int MAX = tabChess.length-1;
//		int startRow = 0;
//		boolean boucle = true;
//
//		while(boucle) {				
//			backTracking(tabChess);
//			for(int curr=MAX;curr>=0;curr--) {		//curr = position de la dernière reine
//				if(tabChess[curr] == "b") {
//					startRow = (curr - (curr%8));		//On se place le plus à gauche sur la ligne actuelle
//					for(int i=startRow;i<startRow+8;i++) {
//						if(tabChess[i] == "o") {
//							boucle = false;
//							break;
//						}
//					}
//				}
//			}
//			MAX = MAX - 8;
//		}
//		System.out.println("New start");
//
//		return tabChess;
//	}

	/**
	 * @Author, GABETTE Cédric
	 * Retire les cases vides inutiles
	 * @param String[] tabChess, le plateau actuel avec les reines placées
	 * @return, le tableau sans les cases vides inutiles
	 */
	public static String[] cleanBoard(String[] tabChess) {
		for(int i=0;i<tabChess.length;i++) {
			tabChess[i] = "o";
		}
		return tabChess;
	}
	
	/**
	 * @Author, GABETTE Cédric
	 * Affiche le plateau
	 * @param String[] tabChess, le plateau actuel avec les reines placées
	 * @return void
	 */
	
	public static void display(String[] tabChess) {
		int k = 0;
	
		for(int i=0;i<8;i++) {
			System.out.println("");

			for(int j=0;j<8;j++) {
				System.out.print("  " + tabChess[(k+j)]);
			}
			k+=8;
		}
		System.out.println("");
	}

}

