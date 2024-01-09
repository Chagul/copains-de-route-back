# Copains de route - Back-end

## Kesraoui Nassima - Plancke Aurélien - Plé Lucas

Ce projet est le back-end du projet platine Copains de route. Ce dépôt va de paire avec la partie [front-end](https://github.com/Chagul/copains-de-route-front).
Il s'agit d'un projet Spring-Boot.

### Pré-requis
Pour pouvoir lancer ce projet localement, vous avez besoin de :

- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)
- [Docker](https://www.docker.com/get-started/)

#### Variables d'environnements

Pour pouvoir lancer le projet, vous avez besoin de créer un fichier `.env` contenant différentes variables :
```
DATABASE_DOCKER_URL=jdbc:postgresql://database:5432/postgres
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=motdepasse
DATABASE_MODE=update
EMAIL=votreemail@gmail.com
PASSWORD=motdepasse
```

Les 4 premières variables sont liées à la base de données. Elles permettent de spécifier l'url de la base pour que Spring puisse
se connecter, le nom d'utilisateur ainsi que le mot de passe de l'utilisateur de la base et le mode de création du schéma.

Vous pouvez vous référer à [cette page](https://docs.spring.io/spring-boot/docs/1.1.0.M1/reference/html/howto-database-initialization.html) pour choisir le mode qui vous convient (update ou create-drop).

Les deux dernières variables concernent l'envoi de mail depuis le serveur. Vous avez ici besoin d'un compte Google ayant l'authentification à deux 
facteurs activée. Cela vous permettra d'obtenir un mot de passe pour pouvoir vous connecter au smtp Google et ainsi envoyer des mails depuis le serveur avec cette adresse.
Vous pouvez suivre la documentation officielle [ici](https://support.google.com/mail/answer/7104828?hl=en&rd=3&visit_id=638404336585712973-2508612431) et [ici](https://support.google.com/accounts/answer/185833).

### Lancer le projet 
Si vous êtes sur linux et que vous avec un accès sudo, vous pouvez directement exécuter :
```bash
./build.sh
```
Le script va compiler le projet et en construire une image Docker. Il va ensuite créer une base de données Postgres et exécuter le projet.

Si vous êtes sur Windows, il vous faut lancer les commandes à la main : 
```
mvn clean package -DskipTests
docker compose down
docker rmi docker-spring-boot-postgres:latest
docker compose up
```
Vous pouvez lancer directement le script si vous avez un shell bash installé via WSL.