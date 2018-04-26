
Table
1) coordi
   -> _id            integer primary key
   -> name           char(10) 
   -> Top_clothes    char(10) foreign key from closet.id
   -> buttom_clothes char(10) foreign key from closet.id
   -> shoes          char(10) foreign key from closet.id

2) Clothes_type
   -> _id            integer primary key
   -> category       char(10) 
   -> section        char(10) 
   
3) closet
   -> _id            integer primary key
   -> type           integer foreign key from Clothes_type._id
   -> pattern        char(10)
   -> color          char(10)
   -> image          BLOB
   
class
1) SQLOpenHelper

2) DBcontroller


## version : 1.1

+ comment 
+ return curser -> structure
+ image insert and return 

## version : 1.2

   table closet
   type : char -> int , foreign from Clothes_type.section -> Clothes_type._id
+  pattern

   table Clothes_type 
+  id primary key
-  primary key (category, section)

   table coordi
+  shoes  char(10) foreign key from closet.id
