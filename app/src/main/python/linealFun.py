import numpy as np
import  io
from matplotlib import pyplot as plt


def main(num1, num2,signo):

    x = range(-99,99)
    plt.plot(x, [funcionLineal(r,num1,num2,signo) for r in x])
    plt.axhline(0, color="black")
    plt.axvline(0, color="black")
    plt.xlim(-99,99)
    plt.ylim(-99,99)
    plt.grid()
    f = io.BytesIO()
    plt.savefig(f,format="png")
    return f.getvalue()

    
def funcionLineal(x,num1,num2,n):

    return num1*x+num2 if(n=="+")else num1*x-num2


def __init__():
    main("lineal",-5,3,"+")