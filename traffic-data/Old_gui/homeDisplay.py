"""
Author: Gabriel Kiprono
Filename: homeDisplay.ppy
Purpose: Home window for the AADT Software GUI

"""


from tkinter import *#use pip install tkinter or py -m pip install tkinter (windows powershell)
from tkinter import ttk
import tkinter.messagebox

class HomeDisplay:
    def __init__(self, root):
        self.root = root
        self.root.title("AADT United States")#Display at the top of the window
        self.root.geometry("1060x700+0+0")
        self.root.resizable(width=False,height=False)
        #self.homeFrame = Frame(self.root)
        #self.homeFrame.grid()

        """
        Frames, Canvas
        
        """
        self.homeFrame = Frame(self.root,bd=5,width=770,height=700,relief=FLAT,bg='khaki4')
        self.homeFrame.grid()

        self.titleFrame = Frame(self.homeFrame, bd=5,width=700, height=50,relief=RIDGE)
        self.titleFrame.grid(row=0,column=0)
        self.headTitle = Frame(self.homeFrame,bd=20,width=700, height=100,relief=RIDGE)
        self.headTitle.grid(row=1,column=0)

        self.leftSide = Frame(self.headTitle, bd=5, width=100,height=400,relief=SUNKEN)
        self.leftSide.pack(side=LEFT)
        self.leftSideFrame = Frame(self.leftSide, bd=5, width=90, height=300,padx=2,bg='khaki4')
        self.leftSideFrame.pack(side=TOP)


        self.rightSide = Frame(self.headTitle, bd=5, width=770,height=400,relief=SUNKEN)
        self.rightSide.pack(side=LEFT)
        self.rightSideFrame = Frame(self.rightSide, bd=5, width=600, height=180,padx=2,pady=2,bg='khaki4')
        self.rightSideFrame.pack(side=TOP)

                
    



def main():
    root = Tk()
    HomeDisplay(root)
    root.mainloop()

if __name__ == "__main__":
    main()