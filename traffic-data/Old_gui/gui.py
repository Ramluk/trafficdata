from tkinter import *
from tkinter import ttk
import tkinter.messagebox
import mysql.connector
from mysql.connector import Error
from navigator import *


class GraphicalUserInterface:
    def __init__(self, root):
        self.root = root
        empty = " "
        self.root.title(200*empty+ "AADT Tennessee, US")
        self.root.geometry("1350x760+0+0")

        #Variables
        self.stationId = StringVar()
        self.county = StringVar()
        self.routeNumber = StringVar()
        self.year = StringVar()
        self.street = StringVar()
        self.stnNumber = IntVar()
        self.stationLocation = StringVar()
        self.aadt2018 = IntVar()
        self.aadt2017 = IntVar()
        self.aadt2016 = IntVar()
        self.aadt2015 = IntVar()
        self.aadt2014 = IntVar()

        #Functions
        def closeApp():
            close = tkinter.messagebox.askyesno("AADT TnState","Confirm exit?")
            if close > 0:
                root.destroy()
                return

        def resetEnries():
            self.stationId.set("")
            self.county.set("")
            self.routeNumber.set("")
            self.year.set("AADT_2018")
            self.street.set("")
            self.stationLocation.set("")

        def printRecords():
            for row in self.trafficRecords.get_children():
                print(self.trafficRecords.item(row)["values"])

        def addRecords():
            return

        def displayRecords():
            try:
                connection = mysql.connector.connect(host = "localhost",user="root",passwd="root",database="dtraffic")
                cursor = connection.cursor()
                print("debug->Connected")
                cursor.execute("""SELECT * FROM dtraffic.datatraffic""")
                results = cursor.fetchall()

                if len(results) !=0:
                    self.trafficRecords.delete(*self.trafficRecords.get_children())
                    for row in results:
                        self.trafficRecords.insert('',END,values=row)
                connection.commit()
                connection.close()
            except Error as e:
                print(f"Error '{e}' occured")
            
        def queryData():
            try:
                connection = mysql.connector.connect(host = "localhost",user="root",passwd="root",database="dtraffic")
                cursor = connection.cursor()
                print("debug->Connected")
                cursor.execute("SELECT STATION_ID,STN_NUMBER,COUNTY,LOCATION,RTE_NUMBER,AADT_2018,AADT_2017,AADT_2016,AADT_2015 FROM dtraffic.trafficdata where COUNTY='%s'", self.county.get())
                results = cursor.fetchall()

                if len(results) !=0:
                    self.trafficRecords.delete(*self.trafficRecords.get_children())
                    for row in results:
                        self.trafficRecords.insert('',END,values=row)
                connection.commit()
                connection.close()
            except Error as e:
                print(f"Error '{e}' occured")


        def tableData(env):
            viewInfo = self.trafficRecords.focus()
            record = self.trafficRecords.item(viewInfo)
            row= record['values'] 
            self.stationId.set(row[0])
            self.stnNumber.set(row[1])
            self.county.set(row[2])
            self.stationLocation.set(row[3])
            self.routeNumber.set(row[4])
            self.aadt2018.set(row[5])
            self.aadt2017.set(row[6])
            self.aadt2016.set(row[7])
            self.aadt2016.set(row[8])

        #Frames
        self.mainFrame = Frame(self.root, bd=10, width=1350, height=700, relief=RIDGE, bg="cadet blue")
        self.mainFrame.grid()
        

        self.topFrame = Frame(self.mainFrame, bd=5, width=1340, height=100, relief=RIDGE)#title frame
        self.topFrame.grid(row=0,column=0)
        self.topFrameA = Frame(self.mainFrame, bd=5, width=1340, height=50, relief=RIDGE)
        self.topFrameA.grid(row=2,column=0)
        self.topFrameB = Frame(self.mainFrame, bd=5, width=1340, height=450, relief=RIDGE)
        self.topFrameB.grid(row=1,column=0)
        
        self.leftFrame=Frame(self.topFrameB, bd=5, width=600, height=180,padx=2,bg="cadet blue", relief=RIDGE)
        self.leftFrame.pack(side=LEFT)
        self.leftFrameA=Frame(self.leftFrame, bd=5, width=600, height=180, relief=RIDGE,padx=2,pady=4)
        self.leftFrameA.pack(side=TOP,padx=0,pady=0)
        self.leftFrameB=Frame(self.leftFrame, bd=5, width=600, height=180, relief=RIDGE)
        self.leftFrameB.pack(side=TOP,pady=4)
        self.leftFrameBLeft=Frame(self.leftFrameB, bd=5, width=300, height=170, relief=RIDGE,padx=2)
        self.leftFrameBLeft.pack(side=LEFT,pady=4)
        self.leftFrameBRight=Frame(self.leftFrameB, bd=5, width=300, height=170, relief=RIDGE,padx=2)
        self.leftFrameBRight.pack(side=RIGHT)

        self.rightFrame = Frame(self.topFrameB, bd=5, width=600, height=400, relief=RIDGE,padx=2, bg="cadet blue")
        self.rightFrame.pack(side=RIGHT)
        self.rightFrameA = Frame(self.rightFrame, bd=5, width=540, height=300, relief=RIDGE,padx=2,pady=2)
        self.rightFrameA.pack(side=TOP)

        #titles
        self.labelTitle = Label(self.topFrame, bd=7, font=('new times roman',56,UNDERLINE,'bold'),text="AADT State of Tennessee")
        self.labelTitle.grid(row=0,column=0, padx=132)

        #labels and entries
        self.stationIdLbl = Label(self.leftFrameA, bd=7, font=('new times roman',12,'bold'),text="Station ID",anchor=W)
        self.stationIdLbl.grid(row=0,column=0)
        self.stationIdEntry = Entry(self.leftFrameA, bd=2, font=('new times roman',12),width=40,justify='left', textvariable=self.stationId)
        self.stationIdEntry.grid(row=0,column=1)

        self.countyNameLbl = Label(self.leftFrameA, bd=7, font=('new times roman',12,'bold'),text="County",anchor=W)
        self.countyNameLbl.grid(row=1,column=0)
        self.countyNameEntry = Entry(self.leftFrameA, bd=2, font=('new times roman',12),width=40,justify='left', textvariable=self.county)
        self.countyNameEntry.grid(row=1,column=1)

        self.rteNumberLbl = Label(self.leftFrameA, bd=7, font=('new times roman',12,'bold'),text="Route Number",anchor=W)
        self.rteNumberLbl.grid(row=2,column=0)
        self.rteNumberEntry = Entry(self.leftFrameA, bd=2, font=('new times roman',12),width=40,justify='left',textvariable=self.routeNumber)
        self.rteNumberEntry.grid(row=2,column=1)

        self.yearEntryLbl = Label(self.leftFrameA, bd=7, font=('new times roman',12,'bold'),text="Year",anchor=W)
        self.yearEntryLbl.grid(row=3,column=0)
        self.yearEntry = ttk.Combobox(self.leftFrameA, font=('arial',12),width=38,state='readonly',textvariable=self.year)
        self.yearEntry['values'] = ('','AADT_2018','AADT_2017','AADT_2016','AADT_2015','AADT_2014',
                'AADT_2013','AADT_2012','AADT_2011','AADT_2010','AADT_2009',
                'AADT_2008','AADT_2007','AADT_2006','AADT_2005','AADT_2004',
                'AADT_2003','AADT_2002','AADT_2001','AADT_2000','AADT_1999',
                'AADT_1998','AADT_1997','AADT_1996','AADT_1995','AADT_1994',
                'AADT_1993','AADT_1992','AADT_1991','AADT_1990','AADT_1989',
                'AADT_1988','AADT_1987','AADT_1986','AADT_1985','AADT_1984')
        self.yearEntry.current(0)
        self.yearEntry.grid(row=3,column=1)

        self.streetLabel = Label(self.leftFrameA, bd=7, font=('new times roman',12,'bold'),text="Location",anchor=W)
        self.streetLabel.grid(row=4,column=0)
        self.streetEntry = Entry(self.leftFrameA, bd=2, font=('new times roman',12),width=40,justify='left', textvariable=self.stationLocation)
        self.streetEntry.grid(row=4,column=1)

        #nvigations


        #buttons
        self.trfcStatisticsBtn = Button(self.topFrameA, pady=1,padx=24,bd=4,font=('arial',8,'bold'),width=8,text="Traffic Statistics",command=self.gotoTrafficStats)
        self.trfcStatisticsBtn.grid(row=0,column=0,padx=1)
        self.customReportsBtn = Button(self.topFrameA, pady=1,padx=24,bd=4,font=('arial',8,'bold'),width=8,text="Custom reports", command=self.gotoCustomReports)
        self.customReportsBtn.grid(row=0,column=1,padx=1)
        self.adminModeBtn = Button(self.topFrameA, pady=1,padx=24,bd=4,font=('arial',8,'bold'),width=8,text="Admin mode",command=self.gotoAdminMode)
        self.adminModeBtn.grid(row=0,column=2,padx=1)
        self.downloadDataBtn = Button(self.topFrameA, pady=1,padx=24,bd=4,font=('arial',8,'bold'),width=8,text="Search", command=self.gotoSearchMode)
        self.downloadDataBtn.grid(row=0,column=3,padx=1)
        self.settingsBtn = Button(self.topFrameA, pady=1,padx=24,bd=4,font=('arial',8,'bold'),width=8,text="Settings")
        self.settingsBtn.grid(row=0,column=4,padx=1)
        self.inspectDataBtn = Button(self.topFrameA, pady=1,padx=24,bd=4,font=('arial',8,'bold'),width=8,text="Inspect This",command=displayRecords)
        self.inspectDataBtn.grid(row=0,column=5,padx=1)
        self.optionsBtn = Button(self.topFrameA, pady=1,padx=24,bd=4,font=('arial',8,'bold'),width=8,text="Options")
        self.optionsBtn.grid(row=0,column=6,padx=1)
        self.resetBtn = Button(self.topFrameA, pady=1,padx=24,bd=4,font=('arial',8,'bold'),width=8,text="Reset",command=resetEnries)
        self.resetBtn.grid(row=0,column=7,padx=1)
        self.printBtn = Button(self.topFrameA, pady=1,padx=24,bd=4,font=('arial',8,'bold'),width=8,text="Print",command=printRecords)
        self.printBtn.grid(row=0,column=8,padx=1)
        self.exitBtn = Button(self.topFrameA, pady=1,padx=24,bd=4,font=('arial',8,'bold'),width=8,text="Exit",command=closeApp)
        self.exitBtn.grid(row=0,column=9,padx=1)
        


        #Scrollbars
        self.scrollY = Scrollbar(self.rightFrameA, orient=VERTICAL)
        self.trafficRecords = ttk.Treeview(self.rightFrameA,height=10,columns=("STATION_ID","STN_NUMBER","COUNTY","LOCATION","RTE_NUMBER",
                "AADT_2018","AADT_2017","AADT_2016","AADT_2015"), yscrollcommand=self.scrollY.set)

        self.scrollY.pack(side=RIGHT, fill=Y)
        self.trafficRecords.heading("STATION_ID", text="Station Id")
        self.trafficRecords.heading("STN_NUMBER", text="Station No")
        self.trafficRecords.heading("COUNTY", text="County")
        self.trafficRecords.heading("LOCATION", text="Location")
        self.trafficRecords.heading("RTE_NUMBER", text="Route Number")
        self.trafficRecords.heading("AADT_2018", text="2018")
        self.trafficRecords.heading("AADT_2017", text="2017")
        self.trafficRecords.heading("AADT_2016", text="2016")
        self.trafficRecords.heading("AADT_2015", text="2015")

        self.trafficRecords['show'] = 'headings'

        self.trafficRecords.column("STATION_ID", width=40)
        self.trafficRecords.column("STN_NUMBER", width=40)
        self.trafficRecords.column("COUNTY", width=40)
        self.trafficRecords.column("LOCATION", width=40)
        self.trafficRecords.column("RTE_NUMBER", width=40)
        self.trafficRecords.column("AADT_2018", width=40)
        self.trafficRecords.column("AADT_2017", width=40)
        self.trafficRecords.column("AADT_2016", width=40)
        self.trafficRecords.column("AADT_2015", width=40)

        
        self.trafficRecords.pack(fill=BOTH,expand=1)
        self.trafficRecords.bind("<ButtonRelease-1>", tableData)
        displayRecords()


      
    #open statistics page
    def gotoTrafficStats(self):
        self.statsPage = Toplevel(self.root)
        self.app = TrafficStatistics(self.statsPage)

    def gotoCustomReports(self):
        self.statsPage = Toplevel(self.root)
        self.app = CustomReports(self.statsPage)

    def gotoAdminMode(self):
        self.statsPage = Toplevel(self.root)
        self.app = AdminMode(self.statsPage)

    def gotoSearchMode(self):
        self.statsPage = Toplevel(self.root)
        self.app = Search(self.statsPage)
    





if __name__ == "__main__":
    root = Tk()
    gui = GraphicalUserInterface(root)
    root.mainloop()