# ProblemeDesHuitsDames
Résolution du problème des huits en Java en utilisant une technique de backtracking.

## Objectif

>Sur un plateau d'échec 8x8, placer 8 dames de manière à ce qu'aucune d'entre-elle ne puisse en capturer une autre, il existe 92 solutions, l'exercice est de les trouver et de les afficher.

- [x] Résoudre le problème des huits dames 
- [ ] Trouver toutes les solutions
- [x] Utiliser une technique de backtracking
- [ ] Coder en JAVA
- [x] Projet documenté
- [x] Générer javadoc

### Points pertinents

* Résoudre par moyen de backtracking pour résoudre le problème
* Nommage compréhensif des méthodes
* Bonne utilisation des commentaires


### Points à améliorer

* Faire des méthodes plus claires et moins liées aux autres
* Synthétiser le code, éviter les répétitions
* Faire une approche différente, moins complexe pour traiter le problème
* Adapater le code pour N cases
* Rendre le code évolutif
* Utiliser d'avantage les bonnes pratiques de programmation
* Vérifier la complexité algorithmique
* Commenter en anglais


### Commentaire du développeur

>J'ai perçu ce problème à résoudre comme un challenge, et j'étais déterminer à résoudre le problème des huits dames, de trouver les 92 solutions distinctes en utilisant une
technique de backtracking, je pense que ma démarche logique était bonne peut-être même pertinante au niveau de la compléxité algorithmique car on réalise un bruteforce partiel. Cependant je savais que l'implémentation allait être complexe pour plusieurs raisons : 
>- Je voulais commencer par mettre en place le comportement des reines, de cette manière je pouvais déterminer les cases disponibles
>- Ajouter les reines le plus à gauche possible des lignes sur les cases disponibles
>- Détecter que la ligne suivante ne peut pas acueillir de reine et donc réaliser un backtracking
>- Réaliser un backtracking fonctionnel
>- Ajouter de nouveau des reines sur les nouvelles cases disponibles découvertes par le backtracking
>- Garder en mémoire chaque solution
>- Trouver toutes les solutions en faisant attention de ne pas tomber sur des solutions existantes déjà
>
>La difficulté s'est retrouvée dans ce dernier points, j'étais capable de générer n'importe quelle solution existante, cependant je n'arrivais pas à ne pas tomber 2 fois sur le même pattern. J'ai laissé une méthode vestige qui selon moi m'aurait permis de trouver les 92 solutions.


