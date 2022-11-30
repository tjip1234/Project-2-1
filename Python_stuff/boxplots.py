import pandas as pd
import matplotlib.pyplot as plt
import random as rand

if __name__ == '__main__':
    nameCSV = "Python_stuff/GreedyvsRule"
    df = pd.read_csv(nameCSV)
    print(df)
    plt.figure()
    bp = df.boxplot()
    plt.title(nameCSV[13:])
    plt.xlabel("AI's")
    plt.ylabel("Scores")
    plt.savefig(nameCSV+".jpg")
    plt.show()
