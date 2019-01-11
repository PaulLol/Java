Installation

Must be installed Java ver 1.8 

If need use local mongodb, need install and start mongod, 
else in program have connect to URI.

Start

-need choose URI or local;

Program realize such mongo-command:
 -use <dbs>;
 -show dbs;
 -show collections;
 
 And own:
 -restart (restart program, to the beginning);
 -shutdown (exit from the program);
 -local (switch to local);
 -URI (switch to URI);
 -sql (use sql query);
 
Command "sql" turns the entered sql-query to mongo-query and shows result.

The assembled program is in /out/artifacts/Project_main_jar/Project_main.jar.
 
For testing, I used the following parameters:
 
uri: mongodb+srv://admin:1234890@mongodb-oxtlk.mongodb.net/admin;

database: MongoDB;

sql: select *from Library where Sex=male order by name desc limit 1

