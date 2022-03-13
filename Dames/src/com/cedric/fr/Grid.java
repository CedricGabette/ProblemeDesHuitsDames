package com.cedric.fr;

public class Grid {

	
	/**
	 * @Author, GABETTE C�dric
	 * On g�n�re un tableau puis on le remplit de "o" repr�sentant une case vide
	 * @param int CASE_X, nombre de ligne
	 * @param int CASE_Y, nombre de colonne
	 * @return, un tableau de String
	 */
	public static String[] generateGrid(int CASE_X, int CASE_Y) {
		
		String tab[] = new String[(CASE_X * CASE_Y)];
		int k = 0;
		
		for(k=0;k<64;k++) {
			tab[k] = "o"; 									
		}
		return tab;
	}
}
