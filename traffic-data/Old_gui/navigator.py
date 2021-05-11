from tkinter import *
import tkinter.messagebox
from tkinter import ttk
import mysql.connector
from mysql.connector import Error

#custom reports window
class CustomReports:
    def __init__(self,root):
        self.root = root
        self.root.title("AADT United States --> Customized Reports")
        self.root.geometry("1360x770+0+0")#+0+0 = coordinates
        self.root.resizable(width=False,height=False)


        #frames
        self.mainFrame = Frame(self.root, bd=10, width=1350, height=750, relief=RIDGE, bg="cadet blue")
        self.mainFrame.grid()
        self.topFrame = Frame(self.mainFrame, bd=5, width=1340, height=70, relief=RIDGE)#title frame
        self.topFrame.grid(row=0,column=0)
        self.topFrameA = Frame(self.mainFrame, bd=5, width=1340, height=180, relief=RIDGE)
        self.topFrameA.grid(row=1,column=0)
        self.topFrameB = Frame(self.mainFrame, bd=5, width=1340, height=360, relief=RIDGE)
        self.topFrameB.grid(row=2,column=0)
        self.topFrameC = Frame(self.mainFrame, bd=5, width=1340, height=40, relief=RIDGE)
        self.topFrameC.grid(row=3,column=0)

#traffic stats window
class TrafficStatistics:
    def __init__(self,root):
        self.root = root
        self.root.title("AADT United States --> Traffic Statistics")
        self.root.geometry("1360x770+0+0")#+0+0 = coordinates
        self.root.resizable(width=False,height=False)


        #frames
        self.mainFrame = Frame(self.root, bd=10, width=1350, height=750, relief=RIDGE, bg="cadet blue")
        self.mainFrame.grid()
        self.topFrame = Frame(self.mainFrame, bd=5, width=1340, height=70, relief=RIDGE)#title frame
        self.topFrame.grid(row=0,column=0)
        self.topFrameA = Frame(self.mainFrame, bd=5, width=1340, height=180, relief=RIDGE)
        self.topFrameA.grid(row=1,column=0)
        self.topFrameB = Frame(self.mainFrame, bd=5, width=1340, height=360, relief=RIDGE)
        self.topFrameB.grid(row=2,column=0)
        self.topFrameC = Frame(self.mainFrame, bd=5, width=1340, height=40, relief=RIDGE)
        self.topFrameC.grid(row=3,column=0)

#admin mode window
class AdminMode:
    def __init__(self,root):
        self.root = root
        self.root.title("AADT United States --> AdminMode")
        self.root.geometry("1360x770+0+0")#+0+0 = coordinates
        self.root.resizable(width=False,height=False)


        #frames
        self.mainFrame = Frame(self.root, bd=10, width=1350, height=750, relief=RIDGE, bg="cadet blue")
        self.mainFrame.grid()
        self.topFrame = Frame(self.mainFrame, bd=5, width=1340, height=70, relief=RIDGE)#title frame
        self.topFrame.grid(row=0,column=0)
        self.topFrameA = Frame(self.mainFrame, bd=5, width=1340, height=180, relief=RIDGE)
        self.topFrameA.grid(row=1,column=0)
        self.topFrameB = Frame(self.mainFrame, bd=5, width=1340, height=360, relief=RIDGE)
        self.topFrameB.grid(row=2,column=0)
        self.topFrameC = Frame(self.mainFrame, bd=5, width=1340, height=40, relief=RIDGE)
        self.topFrameC.grid(row=3,column=0)


#search window
class Search:
    def __init__(self,root):
        self.root = root
        self.root.title("AADT United States --> Search")
        self.root.geometry("1680x770+0+0")#+0+0 = coordinates
        self.root.resizable()

        #VARIABLES
        self.stationId = StringVar()
        self.county = StringVar()
        self.routeNumber = StringVar()
        self.year = StringVar()
        self.street = StringVar()
        self.stnNumber = IntVar()
        self.stationLocation = StringVar()

        #frames
        self.mainFrame = Frame(self.root, bd=10, width=1608, height=750, relief=RIDGE, bg="cadet blue")
        self.mainFrame.grid()
        self.topFrame = Frame(self.mainFrame, bd=5, width=1340, height=70, relief=RIDGE)#title frame
        self.topFrame.grid(row=0,column=0)
        self.topFrameA = Frame(self.mainFrame, bd=5, width=1340, height=180, relief=RIDGE)
        self.topFrameA.grid(row=1,column=0)
        self.topFrameB = Frame(self.mainFrame, bd=5, width=1605, height=360, relief=RIDGE)
        self.topFrameB.grid(row=2,columnspan=2)
        self.topFrameC = Frame(self.mainFrame, bd=5, width=1340, height=40, relief=RIDGE)
        self.topFrameC.grid(row=3,column=0)

        self.labelTitle = Label(self.topFrame, bd=7, font=('new times roman',56,UNDERLINE,'bold'),text="AADT State of Tennessee")
        self.labelTitle.grid(row=0,column=0, padx=132)

        self.midFrameLeft = Frame(self.topFrameA, bd=5, width=650, height=200, relief=RIDGE)
        self.midFrameLeft.pack(side=LEFT, padx=4,pady=4)
        self.midFrameRight = Frame(self.topFrameA, bd=5, width=650, height=200, relief=RIDGE)
        self.midFrameRight.pack(side=LEFT, padx=4,pady=4)

        self.titleLbl = Label(self.midFrameLeft, bd=7, font=('new times roman',12,'bold'),text="Query Fields",anchor=W)
        self.titleLbl.grid(row=0,columnspan=2)
        
        self.stationIdLbl = Label(self.midFrameLeft, bd=7, font=('new times roman',12,'bold'),text="Station ID",anchor=W)
        self.stationIdLbl.grid(row=1,column=0)
        self.stationIdEntry = Entry(self.midFrameLeft, bd=2, font=('new times roman',12),width=40,justify='left')
        self.stationIdEntry.grid(row=1,column=1)
        
        self.countyNameLbl = Label(self.midFrameLeft, bd=7, font=('new times roman',12,'bold'),text="County",anchor=W)
        self.countyNameLbl.grid(row=2,column=0)
        self.countyNameEntry = Entry(self.midFrameLeft, bd=2, font=('new times roman',12),width=40,justify='left',textvariable=self.county)
        self.countyNameEntry.grid(row=2,column=1)
        
        self.rteNumberLbl = Label(self.midFrameLeft, bd=7, font=('new times roman',12,'bold'),text="Route Number",anchor=W)
        self.rteNumberLbl.grid(row=3,column=0)
        self.rteNumberEntry = Entry(self.midFrameLeft, bd=2, font=('new times roman',12),width=40,justify='left')
        self.rteNumberEntry.grid(row=3,column=1)

        self.yearEntryLbl = Label(self.midFrameLeft, bd=7, font=('new times roman',12,'bold'),text="Year",anchor=W)
        self.yearEntryLbl.grid(row=4,column=0)
        self.yearEntry = ttk.Combobox(self.midFrameLeft, font=('arial',12),width=38,state='readonly')
        self.yearEntry['values'] = ('','AADT_2018','AADT_2017','AADT_2016','AADT_2015','AADT_2014',
                'AADT_2013','AADT_2012','AADT_2011','AADT_2010','AADT_2009',
                'AADT_2008','AADT_2007','AADT_2006','AADT_2005','AADT_2004',
                'AADT_2003','AADT_2002','AADT_2001','AADT_2000','AADT_1999',
                'AADT_1998','AADT_1997','AADT_1996','AADT_1995','AADT_1994',
                'AADT_1993','AADT_1992','AADT_1991','AADT_1990','AADT_1989',
                'AADT_1988','AADT_1987','AADT_1986','AADT_1985','AADT_1984')
        self.yearEntry.current(0)
        self.yearEntry.grid(row=4,column=1)

        self.streetLabel = Label(self.midFrameLeft, bd=7, font=('new times roman',12,'bold'),text="Location",anchor=W)
        self.streetLabel.grid(row=5,column=0)
        self.streetEntry = Entry(self.midFrameLeft, bd=2, font=('new times roman',12),width=40,justify='left')
        self.streetEntry.grid(row=5,column=1)
        
        #functions
        #query database, get records from specified county
        def displayRecords():
            if self.county.get() == "":
                tkinter.messagebox.showinfo("AADT TnState","Enter County Name")
                return
                
            try:
                connection = mysql.connector.connect(host = "localhost",user="root",passwd="root",database="dtraffic")
                cursor = connection.cursor()
                print("debug->Connected")
                cursor.execute("SELECT * FROM dtraffic.trafficdata where COUNTY='%s'"%self.county.get())
                results = cursor.fetchall()

                if len(results) !=0:
                    self.trafficRecords.delete(*self.trafficRecords.get_children())
                    for row in results:
                        self.trafficRecords.insert('',END,values=row)
                        #print(row[2])
                
                else:
                    tkinter.messagebox.showinfo("AADT TnState","NO such record found")
                connection.commit()
                connection.close()
            except Error as e:
                print(f"Error '{e}' occured")
                

        #exit
        def closeApp():
            close = tkinter.messagebox.askyesno("AADT TnState","Confirm exit?")
            if close > 0:
                root.destroy()
                return

        #resetting the app
        def resetEnries():
            self.stationId.set("")
            self.county.set("")
            self.routeNumber.set("")
            self.year.set("AADT_2018")
            self.street.set("")
            self.stationLocation.set("")

        #click on row in table and it populates the entries
        def tableData(env):
            viewInfo = self.trafficRecords.focus()
            record = self.trafficRecords.item(viewInfo)
            row= record['values'] 
            self.stationId.set(row[0])
            self.stnNumber.set(row[1])
            self.county.set(row[2])
            self.stationLocation.set(row[3])
            self.routeNumber.set(row[4])

        
        #buttons
        self.trfcStatisticsBtn = Button(self.midFrameRight,bd=4,font=('arial',12,'bold'),width=15,text="Run",command=displayRecords)
        self.trfcStatisticsBtn.grid(row=0,column=0,padx=4,pady=4)
        self.trfcStatisticsBtn = Button(self.midFrameRight,bd=4,font=('arial',12,'bold'),width=15,text="Coming")
        self.trfcStatisticsBtn.grid(row=0,column=1,padx=4,pady=4)

        self.trfcStatisticsBtn = Button(self.midFrameRight,bd=4,font=('arial',12,'bold'),width=15,text="Coming")
        self.trfcStatisticsBtn.grid(row=1,column=0,padx=4,pady=4)
        self.trfcStatisticsBtn = Button(self.midFrameRight,bd=4,font=('arial',12,'bold'),width=15,text="Coming")
        self.trfcStatisticsBtn.grid(row=1,column=1,padx=4,pady=4)

        self.trfcStatisticsBtn = Button(self.midFrameRight,bd=4,font=('arial',12,'bold'),width=15,text="Reset",command=resetEnries)
        self.trfcStatisticsBtn.grid(row=2,column=0,padx=4,pady=4)
        self.trfcStatisticsBtn = Button(self.midFrameRight,bd=4,font=('arial',12,'bold'),width=15,text="Exit",command=closeApp)
        self.trfcStatisticsBtn.grid(row=2,column=1,padx=4,pady=4)

        #scroll bars
        self.scrollY = Scrollbar(self.topFrameB, orient=VERTICAL)
        self.scrollX = Scrollbar(self.topFrameB,orient=HORIZONTAL)
        self.trafficRecords = ttk.Treeview(self.topFrameB,height=10,columns=("STATION_ID","STN_NUMBER","COUNTY","LOCATION","RTE_NUMBER",
            "AADT_2018","AADT_2017","AADT_2016","AADT_2015","AADT_2014",
            "AADT_2013","AADT_2012","AADT_2011","AADT_2010","AADT_2009",
            "AADT_2008","AADT_2007","AADT_2006","AADT_2005","AADT_2004",
            "AADT_2003","AADT_2002","AADT_2001","AADT_2000","AADT_1999",
            "AADT_1998","AADT_1997","AADT_1996","AADT_1995","AADT_1994",
            "AADT_1993","AADT_1992","AADT_1991","AADT_1990","AADT_1989",
            "AADT_1988","AADT_1987","AADT_1986","AADT_1985","AADT_1984",
            "AADT_1983"), yscrollcommand=self.scrollY.set,xscrollcommand=self.scrollX.set)

        self.scrollY.pack(side=RIGHT,fill=X)
        self.scrollX.pack(side=BOTTOM,fill=X)

        self.trafficRecords.heading("STATION_ID", text="Station Id")
        self.trafficRecords.heading("STN_NUMBER", text="Station No")
        self.trafficRecords.heading("COUNTY", text="County")
        self.trafficRecords.heading("LOCATION", text="Location")
        self.trafficRecords.heading("RTE_NUMBER", text="Route Number")
        self.trafficRecords.heading("AADT_2018", text="2018")
        self.trafficRecords.heading("AADT_2017", text="2017")
        self.trafficRecords.heading("AADT_2016", text="2016")
        self.trafficRecords.heading("AADT_2015", text="2015")
        self.trafficRecords.heading("AADT_2014", text="2014")
        self.trafficRecords.heading("AADT_2013", text="2013")
        self.trafficRecords.heading("AADT_2012", text="2012")
        self.trafficRecords.heading("AADT_2011", text="2011")
        self.trafficRecords.heading("AADT_2010", text="2010")
        self.trafficRecords.heading("AADT_2009", text="2009")
        self.trafficRecords.heading("AADT_2008", text="2008")
        self.trafficRecords.heading("AADT_2007", text="2007")
        self.trafficRecords.heading("AADT_2006", text="2006")
        self.trafficRecords.heading("AADT_2005", text="2005")
        self.trafficRecords.heading("AADT_2004", text="2004")
        self.trafficRecords.heading("AADT_2003", text="2003")
        self.trafficRecords.heading("AADT_2002", text="2002")
        self.trafficRecords.heading("AADT_2001", text="2001")
        self.trafficRecords.heading("AADT_2000", text="2000")
        self.trafficRecords.heading("AADT_1999", text="1999")
        self.trafficRecords.heading("AADT_1998", text="1998")
        self.trafficRecords.heading("AADT_1997", text="1997")
        self.trafficRecords.heading("AADT_1996", text="1996")
        self.trafficRecords.heading("AADT_1995", text="1995")
        self.trafficRecords.heading("AADT_1994", text="1994")
        self.trafficRecords.heading("AADT_1993", text="1993")
        self.trafficRecords.heading("AADT_1992", text="1992")
        self.trafficRecords.heading("AADT_1991", text="1991")
        self.trafficRecords.heading("AADT_1990", text="1990")
        self.trafficRecords.heading("AADT_1989", text="1989")
        self.trafficRecords.heading("AADT_1988", text="1988")
        self.trafficRecords.heading("AADT_1987", text="1987")
        self.trafficRecords.heading("AADT_1986", text="1986")
        self.trafficRecords.heading("AADT_1985", text="1985")
        self.trafficRecords.heading("AADT_1984", text="1984")
        self.trafficRecords.heading("AADT_1983", text="1983")

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
        self.trafficRecords.column("AADT_2014", width=40)
        self.trafficRecords.column("AADT_2013", width=40)
        self.trafficRecords.column("AADT_2012", width=40)
        self.trafficRecords.column("AADT_2011", width=40)
        self.trafficRecords.column("AADT_2010", width=40)
        self.trafficRecords.column("AADT_2009", width=40)
        self.trafficRecords.column("AADT_2008", width=40)
        self.trafficRecords.column("AADT_2007", width=40)
        self.trafficRecords.column("AADT_2006", width=40)
        self.trafficRecords.column("AADT_2005", width=40)
        self.trafficRecords.column("AADT_2004", width=40)
        self.trafficRecords.column("AADT_2003", width=40)
        self.trafficRecords.column("AADT_2002", width=40)
        self.trafficRecords.column("AADT_2001", width=40)
        self.trafficRecords.column("AADT_2000", width=40)
        self.trafficRecords.column("AADT_1999", width=40)
        self.trafficRecords.column("AADT_1998", width=40)
        self.trafficRecords.column("AADT_1997", width=40)
        self.trafficRecords.column("AADT_1996", width=40)
        self.trafficRecords.column("AADT_1995", width=40)
        self.trafficRecords.column("AADT_1994", width=40)
        self.trafficRecords.column("AADT_1993", width=40)
        self.trafficRecords.column("AADT_1992", width=40)
        self.trafficRecords.column("AADT_1991", width=40)
        self.trafficRecords.column("AADT_1990", width=40)
        self.trafficRecords.column("AADT_1989", width=40)
        self.trafficRecords.column("AADT_1988", width=40)
        self.trafficRecords.column("AADT_1987", width=40)
        self.trafficRecords.column("AADT_1986", width=40)
        self.trafficRecords.column("AADT_1985", width=40)
        self.trafficRecords.column("AADT_1984", width=40)
        self.trafficRecords.column("AADT_1983", width=40)

        self.trafficRecords.pack(fill=BOTH, expand=1)
        self.trafficRecords.bind("<ButtonRelease-1>", tableData)
        #displayRecords()


#add users window
class NewCredential:
    def __init__(self,root):
        self.root = root
        self.root.title("AADT United States --> Sign Up")
        self.root.geometry("1360x770+0+0")#+0+0 = coordinates
        self.root.resizable(width=False,height=False)


        #frames
        self.mainFrame = Frame(self.root, bd=10, width=1350, height=750, relief=RIDGE, bg="cadet blue")
        self.mainFrame.grid()
        self.topFrame = Frame(self.mainFrame, bd=5, width=1340, height=70, relief=RIDGE)#title frame
        self.topFrame.grid(row=0,column=0)
        self.topFrameA = Frame(self.mainFrame, bd=5, width=1340, height=180, relief=RIDGE)
        self.topFrameA.grid(row=1,column=0)
        self.topFrameB = Frame(self.mainFrame, bd=5, width=1340, height=360, relief=RIDGE)
        self.topFrameB.grid(row=2,column=0)
        self.topFrameC = Frame(self.mainFrame, bd=5, width=1340, height=40, relief=RIDGE)
        self.topFrameC.grid(row=3,column=0)