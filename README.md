# wynk

### Swagger UI
* http://localhost:8080/swagger-ui.html#/
* http://localhost:8080/v2/api-docs

### H2 databse Url
* http://localhost:8080/h2-console/login.jsp
* url=jdbc:h2:mem:testdb
* username=sa
* password=password

### Schema Design
* drop table if exists artist CASCADE 
* drop table if exists playlist CASCADE 
* drop table if exists song CASCADE 
* drop table if exists user CASCADE 
* drop sequence if exists hibernate_sequence
* create sequence hibernate_sequence start with 1 increment by 1
* create table artist (id varchar(255) not null, primary key (id))
* create table playlist (id bigint not null, artist_id varchar(255), song_id varchar(255), user_id varchar(255), primary key (id))
* create table song (id varchar(255) not null, primary key (id))
* create table user (id varchar(255) not null, primary key (id))
* alter table playlist add constraint FKp3w4thsg0rjbrlmxj8u01agrp foreign key (artist_id) references artist
* alter table playlist add constraint FKi46m9yffq6oehh5wmndp9du4h foreign key (song_id) references song
* alter table playlist add constraint FKlbi6wsq41356go2ki0yirfixo foreign key (user_id) references user

### Running application
* mvn spring-boot:run

### Create docker image
* mvn spring-boot:build-image
* docker login --username=<dockerid> --password=<password>
* docker run --publish 8080:8080 --name wynk ashishmit69/wynk:0.0.1-SNAPSHOT
* docker rm --force wynk
* docker images
* docker pull ashishmit69/wynk