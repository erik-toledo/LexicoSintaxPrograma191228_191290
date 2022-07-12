from cmath import sqrt
import numpy as np
import  io
from matplotlib import pyplot as plt


def main(num1,num2,num3,signo,signo2):


    x = range(-99, 99)
    plt.plot(x, [funcionCuadratica(r,num1,num2,num3,signo,signo2) for r in x])
    plt.axhline(0, color="black")
    plt.axvline(0, color="black")
    plt.xlim(-99, 99)
    plt.ylim(-99, 99)
    plt.grid()
    f = io.BytesIO()
    plt.savefig(f,format="png")
    plt.show()
    return f.getvalue()


def funcionCuadratica(x,num1,num2,num3,signo,signo2):
    if(signo == "+" and signo2 == "+"):
        funcion = num1*(x**2)+num2*x+num3
    if(signo == "+" and signo2 == "-"):
        funcion = num1*(x**2)+num2*x-num3
    if(signo == "-" and signo2 == "-"):
        funcion = num1*(x**2)-num2*x-num3
    if(signo == "-" and signo2 == "+"):
        funcion = num1*(x**2)-num2*x+num3
    return funcion