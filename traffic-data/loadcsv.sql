LOAD DATA INFILE 'pathname' --path enclosed by single quotes
INTO TABLE stations
FIELDS TERMINATED BY ',' --this may be different for you based on excel version
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS; --ignoring labeling field 

/* If you're getting errors, try LOAD DATA LOCAL INFILE instead. However,
to do this you have to add the local-infile=1 option when you login to MySQL.
Like mysql --local-infile -u -p. Alternatively, you can edit your /etc/mysql/my.cnf file.
If you edit the file, below [mysqld] add "local-infile" */