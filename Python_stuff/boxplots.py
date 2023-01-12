import pandas as pd
import matplotlib.pyplot as plt
import random as rand

if __name__ == '__main__':
    import matplotlib.pyplot as plt
    import csv

    # Set up empty lists to store the data for each bot
    RandomBot_data = []
    GreedyBot_data = []

    # Open the CSV file and read the data
    with open('GreedyvsRule') as csvfile:
        reader = csv.reader(csvfile)
        for row in reader:
            # Split the row into the bot name and score
            bot, score = row[0].split(':')
            score = int(score)

            # Add the score to the appropriate list
            if bot == 'RandomBot':
                RandomBot_data.append(score)
            elif bot == 'GreedyBot':
                GreedyBot_data.append(score)

    # Create the boxplot
    fig, ax = plt.subplots()
    ax.boxplot([RandomBot_data, GreedyBot_data], labels=['RandomBot', 'GreedyBot'])

    # Add a title and label the axes
    ax.set_title("Comparison of RandomBot and GreedyBot")
    ax.set_xlabel("Bot")
    ax.set_ylabel("Score")

    # Show the plot
    plt.show()
