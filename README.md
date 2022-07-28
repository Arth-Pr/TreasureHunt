### Arthur Perié

## Quelques remarques :)

Le présent programme est une implémentation des sprints 1 et 2 de la chasse aux trésors.

La carte de jeu doit être définie dans le fichier map.txt.
Les aventuriers et leurs séquences d'actions sont à définir dans le fichier adventurers.txt.
Le formalisme des fichiers d'input doit être identique à celui du PDF, une mauvaise syntaxe 
(même un saut de ligne) empêchera le programme de fonctionner.
Les consignes ne mentionnant pas le blocage en bordures de map, celle-ci ne doivent pas être franchies lors de
la définition d'une séquence de mouvements.

Le résultat de l’ensemble des positions des aventuriers, ainsi que le total des trésors ramassés à la fin des 
déplacements est disponible dans le fichier results.txt en fin d'exécution.

Les coordonnées de la map démarrent en 0-0 dans le programme, mais correspondent au 1-1 dans le pdf et dans les 
fichiers d'input/output (3-0 -> 4-1, etc).


## Réflexion sur le sprint 3

La solution envisagée pour le développement du 3ème sprint consiste à implémenter le multi-threading :
Chaque aventurier est désormais géré indépendamment des autres via son propre thread.

Afin de gérer le timing de début de séquence, on ajoute des inputs clavier.
Par exemple, lorsque la touche espace est pressée, un nouveau thread est lancé et le prochain aventurier 
de la liste lui est assigné.