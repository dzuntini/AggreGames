# AggreGames

> Lanceur de jeux unifié pour Windows : une seule fenêtre pour retrouver et lancer les jeux installés sur Steam, Epic Games Store, GOG Galaxy, Origin et d'autres plateformes.

Projet réalisé en 2019 pendant ma formation **Concepteur Développeur Informatique (AFPA)**.

## Le problème

Un joueur sur PC jongle souvent entre cinq ou six launchers différents, chacun avec sa propre bibliothèque. AggreGames les détecte automatiquement et regroupe tous les jeux dans une interface unique, avec une recherche et un lancement en un clic.

## Fonctionnalités

- **Détection automatique des plateformes installées** grâce à la lecture du registre Windows (Steam, Epic Games Store, GOG Galaxy, Origin, Uplay, My.com…).
- **Assistant de premier lancement** : choix des plateformes à utiliser. Les plateformes non détectées sont signalées et un lien de téléchargement de leur installeur est proposé.
- **Inventaire des jeux Steam** grâce à l'analyse des fichiers `appmanifest`, y compris pour les bibliothèques réparties sur plusieurs disques.
- **Lancement des jeux** par les protocoles des plateformes (`steam://rungameid/…`) et lancement direct des clients.
- **Bibliothèque unifiée** sous forme d'arborescence (JTree), avec barre de recherche et accès rapide à chaque plateforme.
- **Alias de noms de jeux** stockés en base MySQL (DAO), pour harmoniser les titres entre plateformes.

## Stack technique

- **Java 8**, interface **Swing** (JFrame, JTree, GridBagLayout)
- **JDBC / MySQL** avec un pattern DAO / DTO
- Accès au **registre Windows** par réflexion sur `java.util.prefs`
- Fichiers de configuration plats (`ressources/Platlist` : nom de la plateforme, clé de registre, URL de l'installeur)

## Architecture

```
src/fr/zuntini/
├── main/         Point d'entrée : premier lancement ou fenêtre principale
├── platform/     Classe abstraite Platform + une implémentation par launcher
│                 (SteamPlat, EpicStorePlat, GoGPlat, OriginPlat…)
├── game/         Modèle Game (id, nom, emplacement, commande de lancement)
├── fenetres/     Fenêtres Swing (assistant de démarrage, fenêtre principale)
├── factory/      Fabriques de composants (boutons, cases à cocher, panneaux)
├── listmaking/   Construction de l'arborescence des jeux
├── database/     DAO / DTO pour les alias de jeux
└── traitement/   Chargement et traitement des données
```

Pour ajouter une plateforme, il suffit d'écrire une sous-classe de `Platform` qui implémente `makelist()` et `getinstallerlink()`, puis de l'inscrire dans `Platlist`.

## Lancer le projet

Prérequis : Windows, JDK 8, une base MySQL locale `AggreGames` avec une table `Game`.

1. Ouvrir le projet dans IntelliJ IDEA (le module `AggreGames.iml` est fourni) ou dans Eclipse.
2. Ajouter les JAR du dossier `lib/` au classpath.
3. Créer la base : `CREATE DATABASE AggreGames;` puis une table `Game` pour les alias. Les identifiants de connexion se trouvent dans `src/fr/zuntini/database/`.
4. Lancer `fr.zuntini.main.Debut`. Au premier démarrage, un assistant propose de choisir les plateformes à utiliser.

## État du projet

C'est un prototype pédagogique. Steam est la plateforme la plus aboutie. Les autres sont détectées et lançables, mais l'inventaire de leurs jeux reste à compléter (voir `TODO-List.txt`).

## Ce que je ferais autrement aujourd'hui

Avec le recul de mes années en développement back-end :

- **Maven ou Gradle** pour les dépendances, au lieu de JAR versionnés dans le dépôt
- **Configuration externalisée** : identifiants de base de données dans des variables d'environnement
- **Tests unitaires** sur l'analyse des manifestes et la détection des plateformes
- Remplacer l'accès au registre par réflexion, qui ne fonctionne plus depuis Java 9+, par une bibliothèque dédiée comme JNA
- Une interface plus moderne (JavaFX) ou une architecture client/serveur
