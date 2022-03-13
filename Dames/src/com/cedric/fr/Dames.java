package com.cedric.fr;

/**
 * @Author, GABETTE C�dric
 * 
 * Placer 8 dames sur un plateau de jeu d'�chec de mani�re � ce qu'aucune dame ne puisse prendre une autre et trouver toutes les solutions
 * possibles.
 */

//TODO Rentre le code dynamique et offrir une possibilit� d'�volutivit�e

//TODO Adapter la code pour n'importe quelle taille de tableau

//TODO Trouver les 92 solutions distinctes

public class Dames extends Services {

	final static int CASE_X = 8;
	final static int CASE_Y = 8;
	
	public static void main(String[] args) {
	
		String tabChess[] = Grid.generateGrid(CASE_X, CASE_Y);
		
		for(int i=-1;i<15;i++) {	
			System.out.println("Solution " + (i+2));
			solution(tabChess, i);
			tabChess = Grid.generateGrid(CASE_X, CASE_Y);
		}
	}	
}
