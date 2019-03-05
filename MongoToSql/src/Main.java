import com.mongodb.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


public class Main {

    public static int searchPosition(String main, String word, String word_2)
    {
        int position = main.indexOf(word);
        if(position == -1)
        {
            position= main.indexOf(word_2);
        }
        return position;
    }


    public static String signDetect(String main)
    {
        String sign = null;
        if(main.indexOf("<")!= -1) sign = "<";
        else if(main.indexOf(">")!= -1) sign = "<";
        else if(main.indexOf("<=")!= -1) sign = "<=";
        else if (main.indexOf(">=")!= -1) sign = ">=";
        else if (main.indexOf("<>")!= -1) sign = "<>";
        else if (main.indexOf("=")!= -1) sign = "=";
        return sign;
    }

    public static boolean testToInt(String main)
    {
        boolean test = false;
        if(main.indexOf("0") == -1) {}
        else if (main.indexOf("1") == -1) {}
        else if (main.indexOf("2") == -1) {}
        else if (main.indexOf("3") == -1) {}
        else if (main.indexOf("4") == -1) {}
        else if (main.indexOf("5") == -1) {}
        else if (main.indexOf("6") == -1) {}
        else if (main.indexOf("7") == -1) {}
        else if (main.indexOf("8") == -1) {}
        else if (main.indexOf("9") == -1) {}
        else { test = true;}
        return  test;
    }

    public static BasicDBObject signQuery(String Before, String after, String sign)
    {
        BasicDBObject QueryAdd = new BasicDBObject();
        int after_int;
        if(testToInt(after)==true)
        {
            after_int = Integer.parseInt(after);
            switch (sign)
            {
                case("<"): QueryAdd.append(Before, new BasicDBObject("$lt", after_int)); break;
                case(">"): QueryAdd.append(Before, new BasicDBObject("$gt", after_int)); break;
                case("<="): QueryAdd.append(Before, new BasicDBObject("$lte", after_int)); break;
                case(">="): QueryAdd.append(Before, new BasicDBObject("$gte", after_int)); break;
                case("<>"): QueryAdd.append(Before, new BasicDBObject("$ne", after_int)); break;
                case("="): QueryAdd.append(Before, after); break;
            }
        } else
        {
            switch (sign)
            {
                case("<"): QueryAdd.append(Before, new BasicDBObject("$lt", after)); break;
                case(">"): QueryAdd.append(Before, new BasicDBObject("$gt", after)); break;
                case("<="): QueryAdd.append(Before, new BasicDBObject("$lte", after)); break;
                case(">="): QueryAdd.append(Before, new BasicDBObject("$gte", after)); break;
                case("<>"): QueryAdd.append(Before, new BasicDBObject("$ne", after)); break;
                case("="): QueryAdd.append(Before, after); break;
            }
        }
        return QueryAdd;
    }

    //Show collections
    public static void showCollection(DB db)
    {
        Set<String> colls = db.getCollectionNames();
        System.out.print(": ");
        for (String s : colls) {
            System.out.print(s+" ");
        }
        System.out.println(" ");
    }

    //Test database name
    public static boolean testDatabaseName(String database, MongoClient mongoClient)
    {
        boolean testdb=false;
        List<String> dbs = mongoClient.getDatabaseNames();
        for (String dblist : dbs)
        {
            if(database.equals(dblist)) testdb = true;
        }
        if (testdb == false)
        {
            return testdb;
        }
        return testdb;
    }

    //Show databases
    public static void showDatabase(MongoClient mongoClient)
    {
        List<String> dbs = mongoClient.getDatabaseNames();
        System.out.print(": ");
        for(String dblist : dbs)
        {
            System.out.print(dblist+"|");
        }
        System.out.println(" ");
    }

    //Test collection name
    public static boolean testCollection(DB db, String collection)
    {
        boolean testCollection=false;
        Set<String> colls = db.getCollectionNames();
        for (String s : colls)
        {
            if(collection.equals(s)) testCollection = true;
        }
        if (testCollection == false)
        {
            return testCollection;
        }
        return testCollection;
    }


    public static void sqlToMongo(DB db)
    {

//        System.out.println("Enter path to mongod:");
//        String stringMongoPath = in.nextLine();
//        File mongodPosition = new File(stringMongoPath);
//        try{
//            Desktop.getDesktop().open(mongodPosition);
//        }catch (IOException e)
//        {
//            System.out.println("Wrong mongod path");
//        }
//
//        Thread.sleep(5000);

        String sql;
        int indexDot;
        Scanner in = new Scanner(System.in);

        //Enter SQL
        System.out.print("SQL: >");
        sql=in.nextLine();


        //Remove all spaces
        sql = sql.replaceAll("\\s","");


        //SearchPosition
        int selectPosition = searchPosition(sql, "SELECT", "select");
        int fromPosition = searchPosition(sql, "FROM", "from");
        int wherePosition = searchPosition(sql,"WHERE","where");
        int orderbyPosition= searchPosition(sql, "ORDERBY", "orderby");
        int skipPosition = searchPosition(sql,"SKIP","skip");
        int limitPosition =searchPosition(sql,"LIMIT","limit");

        BasicDBObject query = new BasicDBObject();
        BasicDBObject fields = new BasicDBObject();

        //FROM Target
        String collection = null;
        if(fromPosition != -1)
        {
            if(wherePosition !=-1)
            {
                collection = sql.substring(fromPosition + 4, wherePosition);
            } else if(wherePosition == -1)
            {
                collection = sql.substring(fromPosition + 4);
            }
            if(collection.length() == 0)
            {
                System.out.println("Wrong input FROM! String 'from' is empty!");
                System.exit(0);
            }
        } else if(fromPosition == -1)
        {
            System.out.println("Wrong input 'FROM'! String 'from' not found! ");
            System.exit(0);
        }
        if (testCollection(db, collection) == false)
        {
            System.out.println("Wrong input 'FROM'! This collection name not found!");
            System.exit(0);
        }
        DBCollection col = db.getCollection(collection);
//        System.out.println("Collection:"+collection);

        //SELECT Projections
        if(selectPosition != -1)
        {
            String stringSelect = sql.substring(selectPosition + 6,fromPosition);

            if(stringSelect.length() == 0)
            {
                System.out.println("Wrong input SELECT! String 'select' is empty!");
                System.exit(0);
            }

//            System.out.println("selectPosition:"+selectPosition);
//            System.out.println("stringSelect:"+stringSelect);

            List<String> selectList = new ArrayList<String>();
            indexDot = stringSelect.indexOf(",");

            if(indexDot != -1)
            {
                String stringSelectBefore = stringSelect.substring(0, indexDot);
                String stringSelectAfter = stringSelect.substring(indexDot+1);

//                System.out.println("stringSelectBefore:"+stringSelectBefore);

                while(indexDot != -1)
                {
//                    System.out.println("stringSelectBefore_2:"+stringSelectBefore);
                    selectList.add(stringSelectBefore);

                    indexDot = stringSelectAfter.indexOf(",");
                    if(indexDot == -1)
                    {
                        selectList.add(stringSelectAfter);
                        break;
                    }
                    stringSelectBefore = stringSelectAfter.substring(0,indexDot);
                    stringSelectAfter = stringSelectAfter.substring(indexDot+1);
                }
                for(String s: selectList)
                {
                    fields.put(s, 1);
                }
                for (String s: selectList)
                {
                    if(s.equals("_id")) {break;}
                    else fields.put("_id", 0);
                }
            } else if(indexDot == -1)
            {
//                System.out.println("stringSelect:"+stringSelect);
                if(stringSelect.equals("*"))
                { } else if(stringSelect.equals("_id"))
                {
                    fields.put(stringSelect, 1);
                } else
                {
                    fields.put(stringSelect, 1);
                    fields.put("_id", 0);
                }
            }
        } else if (selectPosition == -1)
        {
            System.out.println("Wrong input SELECT! String 'select' not found!");
            System.exit(0);
        }

//        System.out.println("fields:"+fields);


        //WHERE Condition
        String stringWhere = null;

//        System.out.println("wherePosition:"+wherePosition);

        if(wherePosition != -1)
        {
            if (orderbyPosition != -1) stringWhere = sql.substring(wherePosition + 5, orderbyPosition);
            else if (skipPosition != -1) stringWhere = sql.substring(wherePosition+ 5, skipPosition );
            else if (limitPosition != -1) stringWhere = sql.substring(wherePosition + 5, limitPosition );
            else stringWhere = sql.substring(wherePosition + 5);

            if(stringWhere.length() == 0)
            {
                System.out.println("Wrong input WHERE! String 'where' is empty!");
                System.exit(0);
            }

            int andPosition = searchPosition(stringWhere,"AND","and");
            int orPosition = searchPosition(stringWhere,"OR","or");

//            System.out.println("stringWhere:"+stringWhere);

            if (andPosition != -1) {
                String stringAndAfter = stringWhere.substring(andPosition+3);
                String stringAndBefore = stringWhere.substring(0, andPosition);

//                System.out.println("andPosition:"+andPosition);
//                System.out.println("stringAndBefore:"+stringAndBefore);
//                System.out.println("stringAndAfter:"+stringAndAfter);

                String signBefore = signDetect(stringAndBefore);
                String signAfter = signDetect(stringAndAfter);
                String stringAndBefore_first = stringAndBefore.substring(0, stringAndBefore.indexOf(signBefore));
                String stringAndBefore_second = stringAndBefore.substring(stringAndBefore.indexOf(signBefore) + 1);
                String stringAndAfter_first = stringAndAfter.substring(0, stringAndAfter.indexOf(signAfter));
                String stringAndAfter_second = stringAndAfter.substring(stringAndAfter.indexOf(signAfter) + 1);
                List<BasicDBObject> queryList = new ArrayList<BasicDBObject>();
                queryList.add(signQuery(stringAndBefore_first, stringAndBefore_second, signBefore));
                queryList.add(signQuery(stringAndAfter_first, stringAndAfter_second, signAfter));

//                for (BasicDBObject s:queryList)
//                {
//                    System.out.println("queryList:"+s);
//                }

                query.put("$and", queryList);

//                System.out.println("query:"+query);

            } else if (orPosition != -1) {
                String stringOrAfter = stringWhere.substring(orPosition+2);
                String stringOrBefore = stringWhere.substring(0, orPosition);

//                System.out.println("orPosition:"+orPosition);
//                System.out.println("stringOrBefore:"+stringOrBefore);
//                System.out.println("stringOrAfter:"+stringOrAfter);

                String signBefore = signDetect(stringOrBefore);
                String signAfter = signDetect(stringOrAfter);
                String stringOrBefore_first = stringOrBefore.substring(0, stringOrBefore.indexOf(signBefore));
                String stringOrBefore_second = stringOrBefore.substring(stringOrBefore.indexOf(signBefore) + 1);
                String stringOrAfter_first = stringOrAfter.substring(0, stringOrAfter.indexOf(signAfter));
                String stringOrAfter_second = stringOrAfter.substring(stringOrAfter.indexOf(signAfter) + 1);
                List<BasicDBObject> queryList = new ArrayList<BasicDBObject>();
                queryList.add(signQuery(stringOrBefore_first, stringOrBefore_second, signBefore));
                queryList.add(signQuery(stringOrAfter_first, stringOrAfter_second, signAfter));

//                for (BasicDBObject s:queryList)
//                {
//                    System.out.println("queryList:"+s);
//                }

                query.put("$or", queryList);

//                System.out.println("query:"+query);

            } else {
                String sign = signDetect(stringWhere);
                String stringWhere_first = stringWhere.substring(0, stringWhere.indexOf(sign));
                String stringWhere_second = stringWhere.substring(stringWhere.indexOf(sign) + 1);
//                System.out.println("Sign:"+sign+", where1:"+stringWhere_first+", where2:"+stringWhere_second);

                query = signQuery(stringWhere_first, stringWhere_second, sign);

//                System.out.println("query:"+query);
            }
        }

        BasicDBObject orderbyQuery = new BasicDBObject();

        //ORDERBY Fields
        String stringOrderby = null;
        if(orderbyPosition != -1)
        {
            if(limitPosition !=-1) stringOrderby = sql.substring(orderbyPosition+7, limitPosition);
            else if(skipPosition !=-1) stringOrderby = sql.substring(orderbyPosition+7, skipPosition);
            else stringOrderby = sql.substring(orderbyPosition +7);

            if(stringOrderby.length() == 0)
            {
                System.out.println("Wrong input ORDER BY! String 'order by' is empty!");
                System.exit(0);
            }

            int ascPosition = searchPosition(stringOrderby,"ASC","asc");
            int descPosition = searchPosition(stringOrderby,"DESC","desc");

//            System.out.println("orderby:"+stringOrderby);

            indexDot = stringOrderby.indexOf(",");
            if(indexDot != -1)
            {
                String stringOrderbyBefore = stringOrderby.substring(0,indexDot);
                String stringOrderbyAfter = stringOrderby.substring(indexDot+1);

                while(indexDot !=-1)
                {
                    ascPosition = searchPosition(stringOrderbyBefore, "ASC", "asc");
                    descPosition = searchPosition(stringOrderbyBefore, "DESC", "desc");

                    if(ascPosition !=-1)
                    {
                        orderbyQuery.put(stringOrderbyBefore.substring(0,ascPosition), 1);
                    } else if(descPosition != -1)
                    {
                        orderbyQuery.put(stringOrderbyBefore.substring(0,descPosition), -1);
                    } else
                    {
                        orderbyQuery.put(stringOrderbyBefore.substring(0, indexDot), 1);
                    }

                    indexDot = stringOrderbyAfter.indexOf(",");

                    if(indexDot == -1)
                    {
                        ascPosition = searchPosition(stringOrderbyAfter, "ASC", "asc");
                        descPosition = searchPosition(stringOrderbyAfter, "DESC", "desc");
                        if(ascPosition !=-1)
                        {
                            orderbyQuery.put(stringOrderbyAfter.substring(0,ascPosition ), 1);
                        } else if(descPosition != -1)
                        {
                            orderbyQuery.put(stringOrderbyAfter.substring(0,descPosition), -1);
                        } else
                        {
                            orderbyQuery.put(stringOrderbyAfter, 1);
                        }
                        break;
                    }
                    stringOrderbyBefore = stringOrderbyAfter.substring(0, indexDot);
                    stringOrderbyAfter = stringOrderbyAfter.substring(indexDot+1);
                }
            } else if(indexDot == -1)
            {
                if(ascPosition !=-1)
                {
                    orderbyQuery.put(stringOrderby.substring(0,ascPosition), 1);
                } else if(descPosition != -1)
                {
                    orderbyQuery.put(stringOrderby.substring(0,descPosition), -1);
                } else
                {
                    orderbyQuery.put(stringOrderby, 1);
                }
            }
        }

//        System.out.println("orderbyQuery:"+orderbyQuery);

        //SKIP SkipRecords
        int skip = 0;

        if(skipPosition !=-1)
        {
            String stringSkip = null;
            if(limitPosition !=-1) stringSkip = sql.substring(skipPosition+4, limitPosition -1);
            else stringSkip = sql.substring(skipPosition+4);

            if(stringSkip.length() == 0)
            {
                System.out.println("Wrong input SKIP! String 'skip' is empty!");
                System.exit(0);
            }

//            System.out.println("stringSkip:"+stringSkip);

            if(stringSkip.indexOf("-")== -1)
            {
                skip = Integer.parseInt(stringSkip);
            } else
            {
                System.out.println("Wrong SKIP input");
                System.exit(0);
            }
        }

//        System.out.println("skip:"+skip);

        //LIMIT MaxRecords
        int limit = 0;
        if(limitPosition !=-1)
        {
            String stringLimit = null;
            stringLimit = sql.substring(limitPosition+5);

            if(stringLimit.length() == 0)
            {
                System.out.println("Wrong input LIMIT! String 'limit' is empty!");
                System.exit(0);
            }
//            System.out.println("stringLimit"+stringLimit);

            if(stringLimit.indexOf("-") == -1)
            {
                limit = Integer.parseInt(stringLimit);

            } else
            {
                System.out.println("Wrong LIMIT input");
                System.exit(0);
            }
        }

//        System.out.println("limit:"+limit);

        //Show query
        DBCursor cursor = col.find(query, fields).sort(orderbyQuery).skip(skip).limit(limit);
//        System.out.println("Show query:");
        while(cursor.hasNext())
        {
            System.out.println(cursor.next());
        }
    }

    public static void main(String args[])
    {
        String uri;
        String choice;
        Scanner in = new Scanner(System.in);
        MongoClient mongoClient = new MongoClient();
        MongoClientURI clientURI;
        System.out.println("Initial settings. Please respond to:");
        System.out.println("URI or local ?");
        System.out.print( ">");
        choice = in.nextLine();
        choice=choice.replaceAll("\\s","");
        if(choice.equals("URI"))
        {
            System.out.print("URI: >");
            uri = in.nextLine();
            uri=uri.replaceAll("\\s","");
            //Test URI
            try
            {
                clientURI = new MongoClientURI(uri);
                mongoClient = new MongoClient(clientURI);

            }catch (IllegalArgumentException e)
            {
                System.out.println("Wrong input URI");
                System.exit(0);
            }
        } else if(choice.equals("local"))
        {
            System.out.println("host:localhost, port:27017");
            mongoClient = new MongoClient( "localhost" , 27017 );
        } else
        {
            System.out.print("Wrong input!");
            System.exit(0);
        }

        DB db = mongoClient.getDB("admin");
        int exitInt =10;

        for(int i=0;i<=exitInt;i++)
        {
            System.out.print(">");
            String command = in.nextLine();
            command=command.replaceAll("\\s","");
            if(command.equals("showdbs"))
            {
                showDatabase(mongoClient);
                exitInt++;
            }else if(command.indexOf("use") != -1)
            {
                String stringUse = command.substring(command.indexOf("use")+3);
                if(testDatabaseName(stringUse, mongoClient) == true)
                {
                    db = mongoClient.getDB(stringUse);
                    System.out.println("switched to db "+stringUse);
                }else
                {
                    System.out.println("Wrong input database name");
                }
                exitInt++;
            }else if(command.equals("showcollections"))
            {
                showCollection(db);
                exitInt++;
            }else if(command.equals("sql"))
            {
                sqlToMongo(db);
                exitInt++;

            }else if(command.equals("shutdown"))
            {
                System.exit(0);
            }else if(command.equals("URI"))
            {
                System.out.print("URI: >");
                uri = in.nextLine();
                try
                {
                    clientURI = new MongoClientURI(uri);
                    mongoClient = new MongoClient(clientURI);

                }catch (IllegalArgumentException e)
                {
                    System.out.println("Wrong input URI");
                    System.exit(0);
                }
                System.out.println("Switch to URI: " +uri);
            }else if(command.equals("local"))
            {

                mongoClient = new MongoClient("localhost", 27017);
                System.out.println("Switch to local: host: localhost, port:27017");
            }
            else if(command.equals("restart"))
            {
                System.out.println("Initial settings. Please respond to:");
                System.out.println("URI or local ?");
                System.out.print(">");
                choice = in.nextLine();
                choice = choice.replaceAll("\\s", "");
                if (choice.equals("URI")) {
                    System.out.print("URI: >");
                    uri = in.nextLine();
                    uri = uri.replaceAll("\\s", "");
                    //Test URI
                    try {
                        clientURI = new MongoClientURI(uri);
                        mongoClient = new MongoClient(clientURI);

                    } catch (IllegalArgumentException e) {
                        System.out.println("Wrong input URI");
                        System.exit(0);
                    }
                } else if (choice.equals("local")) {
                    mongoClient = new MongoClient("localhost", 27017);
                    System.out.println("host:localhost, port:27017");
                } else
                    {
                    System.out.print("Wrong input!");
                    System.exit(0);
                }
                exitInt++;
            }else
            {
                System.out.println("Wrong input!");
                exitInt++;
            }
        }
    }
}


//uri = mongodb+srv://admin:1234890@mongodb-oxtlk.mongodb.net/admin;
//database = MongoDB;
//select *from Library where Sex=male order by namedesc limit 1