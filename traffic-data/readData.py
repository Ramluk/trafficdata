"""
FileName:   readData.py
Purpose:    reads the csv file, creates the database and inserts read data from csv
Authors:    Gabriel & Mark
Comments:   Have mysqlBench up and running, create user <root> and password <root> and assign all privilleges
            default ip address for mysql is 127.0.0.1, port 3306
            install mysql, mysqlconnector, pandas
            py -m pip install pandas

"""

import mysql.connector
from mysql.connector import Error
import csv
import pandas as pd

def createConnection(hostName, userName, userPassword):
    try:
        connection = mysql.connector.connect(host=hostName,user=userName,passwd=userPassword)
        print("Connection created")
    except Error as e:
        print(f"The error '{e}' occured")

    return connection

def createDatabase(connection, query):
    cursor = connection.cursor()
    try:
        cursor.execute(query)
        print("Database created")
    except Error as e:
        print(f"Error '{e}' occured")


def executeQuery(connection, query):
    cursor = connection.cursor()
    try:
        cursor.execute(query)
        connection.commit()
        print(f"Query executed successfully")
    except Error as e:
        print(f"Error '{e}' occured")

def createDatabaseConnection(hostname, userName, userPassword, database):
    try:
        connection = mysql.connector.connect(host=hostname,user=userName,passwd=userPassword, database = database)
        print("Connection created")
    except Error as e:
        print(f"The error '{e}' occured")
        return
    
    return connection


"""
Reads the csv file, prepares a dataframe, fills all null values,
loops through rows in the dataframe inserting records into the database 
"""

def readCsvFile(connection, filename):
    cursor = connection.cursor()
    count = 0
    data = pd.read_csv(filename)#reading csv file
    columns = ['STATION_ID','STN_NUMBER','COUNTY','LOCATION','RTE_NUMBER',
                'AADT_2018','AADT_2017','AADT_2016','AADT_2015','AADT_2014',
                'AADT_2013','AADT_2012','AADT_2011','AADT_2010','AADT_2009',
                'AADT_2008','AADT_2007','AADT_2006','AADT_2005','AADT_2004',
                'AADT_2003','AADT_2002','AADT_2001','AADT_2000','AADT_1999',
                'AADT_1998','AADT_1997','AADT_1996','AADT_1995','AADT_1994',
                'AADT_1993','AADT_1992','AADT_1991','AADT_1990','AADT_1989',
                'AADT_1988','AADT_1987','AADT_1986','AADT_1985','AADT_1984',
                'AADT_1983','X','Y']

    dataFrame = pd.DataFrame(data,columns = columns)#combining dataframe
    dataFrame = dataFrame.interpolate(method='linear', limit_direction='forward',axis=0) #takes care of nulls
    print(dataFrame)

    #dtraffic is name of database and trafficdata is the name table
    insertStatement = """INSERT INTO dtraffic.trafficdata(
        STATION_ID,STN_NUMBER,COUNTY,LOCATION,RTE_NUMBER,AADT_2018,AADT_2017,AADT_2016,
        AADT_2015,AADT_2014,AADT_2013,AADT_2012,AADT_2011,AADT_2010,AADT_2009,AADT_2008,
        AADT_2007,AADT_2006,AADT_2005,AADT_2004,AADT_2003,AADT_2002,AADT_2001,AADT_2000,
        AADT_1999,AADT_1998,AADT_1997,AADT_1996,AADT_1995,AADT_1994,AADT_1993,AADT_1992,
        AADT_1991,AADT_1990,AADT_1989,AADT_1988,AADT_1987,AADT_1986,AADT_1985,AADT_1984,AADT_1983,X,Y)
        VALUES (
            %s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,
            %s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s
        )"""

    for row in dataFrame.itertuples():
        try:
            cursor.execute(insertStatement,(
                row.STATION_ID,row.STN_NUMBER,row.COUNTY,row.LOCATION,row.RTE_NUMBER,row.AADT_2018,row.AADT_2017,row.AADT_2016,
                row.AADT_2015,row.AADT_2014,row.AADT_2013,row.AADT_2012,row.AADT_2011,row.AADT_2010,row.AADT_2009,row.AADT_2008,
                row.AADT_2007,row.AADT_2006,row.AADT_2005,row.AADT_2004,row.AADT_2003,row.AADT_2002,row.AADT_2001,row.AADT_2000,
                row.AADT_1999,row.AADT_1998,row.AADT_1997,row.AADT_1996,row.AADT_1995,row.AADT_1994,row.AADT_1993,row.AADT_1992,
                row.AADT_1991,row.AADT_1990,row.AADT_1989,row.AADT_1988,row.AADT_1987,row.AADT_1986,row.AADT_1985,row.AADT_1984,
                row.AADT_1983,row.X,row.Y
            ))
            connection.commit()
            count+=1
            print(count)
        except Error as e:
            print(f"Error '{e}' happened")
            #break


    
def createTable(connection):
    createStatement = """
        CREATE TABLE IF NOT EXISTS trafficdata(
            STATION_ID INT NOT NULL,
            STN_NUMBER INT NOT NULL,
            COUNTY VARCHAR(255) NOT NULL,
            LOCATION VARCHAR(255) NOT NULL,
            RTE_NUMBER VARCHAR(255) NOT NULL,
            AADT_2018 INT NOT NULL,
            AADT_2017 INT NOT NULL,
            AADT_2016 INT NOT NULL,
            AADT_2015 INT NOT NULL,
            AADT_2014 INT NOT NULL,
            AADT_2013 INT NOT NULL,
            AADT_2012 INT NOT NULL,
            AADT_2011 INT NOT NULL,
            AADT_2010 INT NOT NULL,
            AADT_2009 INT NOT NULL,
            AADT_2008 INT NOT NULL,
            AADT_2007 INT NOT NULL,
            AADT_2006 INT NOT NULL,
            AADT_2005 INT NOT NULL,
            AADT_2004 INT NOT NULL,
            AADT_2003 INT NOT NULL,
            AADT_2002 INT NOT NULL,
            AADT_2001 INT NOT NULL,
            AADT_2000 INT NOT NULL,
            AADT_1999 INT NOT NULL,
            AADT_1998 INT NOT NULL,
            AADT_1997 INT NOT NULL,
            AADT_1996 INT NOT NULL,
            AADT_1995 INT NOT NULL,
            AADT_1994 INT NOT NULL,
            AADT_1993 INT NOT NULL,
            AADT_1992 INT NOT NULL,
            AADT_1991 INT NOT NULL,
            AADT_1990 INT NOT NULL,
            AADT_1989 INT NOT NULL,
            AADT_1988 INT NOT NULL,
            AADT_1987 INT NOT NULL,
            AADT_1986 INT NOT NULL,
            AADT_1985 INT NOT NULL,
            AADT_1984 INT NOT NULL,
            AADT_1983 INT NOT NULL,
            X FLOAT NOT NULL,
            Y FLOAT NOT NULL,
            PRIMARY KEY (STATION_ID)
        )ENGINE=InnoDB;
    """
    executeQuery(connection, createStatement)
    print("Table created")


def main():
    fileName = "TrfcHist.csv"
    databseName = "DTraffic"
    #database is hosted in my local machine, username is root,password is root
    #can also use IP 127.0.0.1 instead of localhost
    connection = createConnection("Localhost","newuser","Newuser;1")

    #DTraffic will be database name
    queryOne = "CREATE DATABASE DTraffic"
    createDatabase(connection, queryOne)#creates database
    connection.close()
    
    connection = createDatabaseConnection("Localhost","newuser","Newuser;1",databseName)
    #useDatabase = "USE DTraffic"
    #executeQuery(connection, useDatabase)#our connection will be using Dtraffic database

    #create table
    createTable(connection)

    #read csv
    readCsvFile(connection, fileName)

    #close connection
    connection.close()

if __name__ == "__main__":
    main()

