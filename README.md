#Carcassonne

## Grundsätzliches
### App
Gibt Spielzug von Spieler an Server.
Bekommt GameObject vom Server, wenn jemand einen Spielzug gemacht hat.
Library: LibGDX (https://ivanludvig.github.io/blog/2019/06/23/integrating-libgdx-into-android-project.html)

### Server
Managed die ganzen Sessions
Pro Session gibt es ein GameObject
GameObject enthält aktuelles Spielfeld, Spieler, Spielerposition, Punktestand, Status vom Spiel (Runde, etc.)
Kommunikation über Sockets

## Projektstruktur 
### App
Network
 - handled die ganze Kommunikation mit Server
Models
 - BaseModel
Storage (optional)
Utils
Viewmodels
Views
   - Button
     - BaseButton
     - PrimaryButton
     - SecondaryButton
   - BaseActivity
   - BaseFragment

### Server
GameLogic
Client
 - Interface für JavaRMI, für Kommunikation (zb. machSpielzug, update)
Session
Model (gleich wie in App)
 - BaseModel
Utils

## Git-Regeln
### App
Main: main-app
Dev: dev-app
Feature: issue-{feature-number}-app

### Server
Main: main-server
Dev: dev-server
Feature: issue-{feature-number}-server

### Initial Steps
#### git clone
    `git clone git@github.com:SE2-Gamma/carcassonne.git`
    https://jdblischak.github.io/2014-09-18-chicago/novice/git/05-sshkeys.html
#### git pull
    `git pull`

### Work on new issue
#### git pull
    `git pull`

#### create new branch
    `git checkout -b issue-x-app`
    `git push --set-upstream origin issue-x-app`

#### commit changes
    `git add .`
    `git commit -m "issue-x-app: my changes"`
    `git push`
   
## Definition of Done
- Der Code ist fertiggestellt und im Versionierungssystem eingespielt.
- Es gab einen Code Review bzw. der Code wurde im Pair Programming erarbeitet
- Vereinbarte Coding Guidelines und Standards sind eingehalten.
- Alle Akzeptanzkriterien sind erfüllt.
- Es sind kaum Bugs offen.

## Ready for Review
- Vereinbarte Coding Guidelines und Standards sind eingehalten.
- Alle Akzeptanzkriterien sind erfüllt.
- Es sind keine kritischen Bugs offen.
