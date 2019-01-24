========================
UTILISATION DU CHECKER
========================

1. Format du fichier d'instance

Il est imperatif que le fichier d'instance respecte le format suivant :
- premiere ligne : capacite des vehicules (type entier)
- deuxieme ligne : coordonnes du depot (type double) separees par une tabulation
- lignes suivantes : coordonnes du client (type double) puis la demande (type entier), le tout separe par des tabulations



2. Format du fichier de solution

Ce fichier doit decrire les tournees.
Vous pouvez mettre autant de lignes de commentaire que vous le souhaitez.
Un commentaire est une ligne qui ne commence pas par '0'.

Chacune des tournees doit etre ecrite sur une seule ligne de la manière suivante :
- commencer par '0' (represente le depot)
- puis contenir les id des clients a visiter dans cette tournee (dans l'ordre) 
- et enfin se terminer par le chiffre '0' (retour au depot) 
Tous les ids doivent etre separes par des espaces ou des tabulations. 
Les id des clients doivent commencer a 1 et etre incrementes de 1 en 1.



3. Nommage des fichiers

Si le fichier d'instance se nomme 'nomInstance.txt', alors le fichier de solution doit se nommer 'nomInstance_sol.txt'.



4. Utilisation du checker

- Dans un meme repertoire mettez le checker (CheckerRouting.jar), l'instance et la solution avec les bons formats.
- Ouvrez une fenêtre de ligne de commande
- Tapez
	java -jar CheckerRouting.jar nomInstance
	
Notez bien que dans ce cas, il est impératif que le fichier d'instance se nomme 'nomInstance.txt', et le fichier de solution se nomme 'nomInstance_sol.txt'.