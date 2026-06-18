# Rendu TP - Application Notes

## Resume

Le projet initial UserApp a ete refondu en application Android Notes sous le package
`com.example.notes`. L'application permet de creer, afficher, modifier, epingler et
supprimer des notes avec confirmation. Les donnees sont persistantes localement via
Room, exposees a l'interface par un Repository et des ViewModels Hilt utilisant
StateFlow. L'interface est construite en Jetpack Compose avec Navigation Compose et
des routes typees serialisables.

## Architecture

La structure suit une architecture MVVM simple et maintenable :

- `data/local` contient `NoteEntity`, `NoteDao` et `NoteDatabase`.
- `data/repository` centralise l'acces aux donnees avec `NoteRepository`.
- `di` fournit Room et le DAO via Dagger Hilt.
- `ui/navigation` declare les routes typees et le `NavHost`.
- `ui/notelist` gere l'etat et l'ecran de liste.
- `ui/noteedit` gere la creation et la modification.
- `ui/theme` contient le theme Material 3 de l'application.

Room gere la persistance SQLite et genere le code d'acces avec KSP. Hilt injecte la
base de donnees, le DAO, le repository et les ViewModels. Le repository evite de
coupler l'UI a Room. Les ViewModels exposent l'etat via StateFlow ou `mutableStateOf`
afin que Compose se recompose automatiquement quand les donnees changent. Navigation
Compose utilise des routes typees `NoteListRoute` et `NoteEditRoute`.

## Choix techniques

Le champ supplementaire retenu est `category: String = "Personnel"`. Les notes sont
triees dans le DAO avec les notes epinglees en premier, puis par `updatedAt DESC`.
La suppression passe par un `AlertDialog`. Les erreurs de suppression ou de mise a
jour sont affichees via Snackbar. La version Hilt a ete alignee sur `2.59.1`, plus
compatible avec AGP 9.2.1 que la version plus ancienne. KSP est aligne sur Kotlin
2.2.10 avec `2.2.10-2.0.2`.

## Points de vigilance

AGP 9.2 utilise le mode built-in Kotlin. Pour que KSP integre correctement ses
sources generees dans ce projet, `android.disallowKotlinSourceSets=false` est present
dans `gradle.properties`. Le SDK local est indique dans `local.properties`.

## Commandes

Depuis le dossier racine du projet :

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
.\gradlew.bat assembleDebug
```

Ouvrir le projet dans Android Studio depuis `UserApp\UserApp`, synchroniser Gradle,
puis lancer la configuration `app`.

## Captures a produire

L'emulateur n'a pas ete lance pendant ce rendu. Captures attendues :

1. Liste des notes : ecran `Mes notes`, plusieurs cartes visibles, une note epinglee
   en haut avec l'indication `Epinglee`, boutons epingler et supprimer.
2. Ajout/edition : ecran `Nouvelle note` ou `Modifier la note`, bouton retour,
   bouton enregistrer, champs `Titre`, `Categorie` et `Contenu`.
3. Suppression : boite de dialogue `Supprimer la note` avec les actions `Annuler`
   et `Supprimer`.

## Validation

- Creation de note implementee.
- Liste des notes reactive via Room Flow et StateFlow.
- Notes triees avec les notes epinglees en premier.
- Modification d'une note existante implementee.
- Suppression avec confirmation implementee.
- Persistance locale Room implementee.
- Ecran vide implemente.
- Rotation supportee par ViewModel, StateFlow et SavedStateHandle.
- Compilation verifiee avec `assembleDebug` : BUILD SUCCESSFUL.
