package com.cedric.fr;

/*
 * Class Utils
 * Contient les méthodes qui agissent sur l'affichage, la vérification, la copie et la mise à jour des cases du plateau.
 */


public class Utils {
	
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
	 * Vérifie si la solution actuel existe déjà dans la liste des solutions trouvées
	 * @param String[]tabChess, le plateau actuel avec les reines placées
	 * @param String[]tabMem, un tableau qui va contenir toutes les solutions touvées
	 * @return true, si une solution existe déjà
	 */
	public static boolean isAlreadyExists(String[] tabChess, String[] tabMem) {
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
	 * Permet de retirer toutes les cases bloquantes inutiles
	 * @param String[] tabChess, le plateau actuel avec les reines placées
	 * @return void
	 */
	public static void getRidOfInterdictions(String[] tabChess) {
		for(int i=0;i<tabChess.length;i++) {
			if(tabChess[i] == "b") {
				tabChess[i] = ".";
			}
		}
	}
}
