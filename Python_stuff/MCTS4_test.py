import matplotlib.pyplot as plt

# data for each bot
random_bot = [84.16243654822335,598784.0,3900,13.44,
              84.94949494949495,597265.0,3900,14.661,
              83.09859154929578,597912.0,3900,14.333,
              86.06060606060606,597158.0,3900,15.421,
              82.9465186680121,585595.0,3900,13.916,
              87.19758064516128,585431.0,3900,15.439,
              83.55329949238579,564078.0,3900,13.931,
              84.57661290322581,560215.0,3900,14.553,
              84.6619576185671,558984.0,3900,14.772,
              88.83248730964468,558703.0,3900,15.743]
greedy_bot = [54.03472931562819,551153.0,3900,-2.641,
              52.755102040816325,549330.0,3900,-2.653,
              56.42857142857143,550232.0,3900,-1.278,
              57.59109311740891,550396.0,3900,-1.67,
              57.5356415478615,549790.0,3900,-1.128,
              53.265306122448976,549884.0,3900,-2.139,
              52.760736196319016,549730.0,3900,-2.936,
              54.23901940755873,549770.0,3900,-2.403,
              52.947154471544714,550176.0,3900,-2.053,
              53.278688524590166,549854.0,3900,-2.647]
mcts3_bot = [50.50813008130082,1104606.0,3900,-0.34,
             49.84771573604061,1106020.0,3900,-0.293,
             48.67886178861789,1105199.0,3900,-1.074,
             49.693251533742334,1105567.0,3900,-0.139,
             50.40567951318459,1105931.0,3900,0.253,
             51.016260162601625,1105860.0,3900,-0.303,
             52.7013251783894,1105703.0,3900,1.137,
             50.356052899287896,1105144.0,3900,0.465,
             50.308008213552355,1105819.0,3900,0.245,
             49.441624365482234,1105269.0,3900,0.052]
# create subplots
# compute average performance of each bot
# calculate average performance for each bot
random_bot_avg = [sum(random_bot[::4]) / len(random_bot[::4]),
                  sum(random_bot[1::4]) / len(random_bot[1::4]),
                  sum(random_bot[2::4]) / len(random_bot[2::4]),
                  sum(random_bot[3::4]) / len(random_bot[3::4])]
greedy_bot_avg = [sum(greedy_bot[::4]) / len(greedy_bot[::4]),
                  sum(greedy_bot[1::4]) / len(greedy_bot[1::4]),
                  sum(greedy_bot[2::4]) / len(greedy_bot[2::4]),
                  sum(greedy_bot[3::4]) / len(greedy_bot[3::4])]
mcts3_bot_avg = [sum(mcts3_bot[::4]) / len(mcts3_bot[::4]),
                 sum(mcts3_bot[1::4]) / len(mcts3_bot[1::4]),
                 sum(mcts3_bot[2::4]) / len(mcts3_bot[2::4]),
                 sum(mcts3_bot[3::4]) / len(mcts3_bot[3::4])]

# calculate average performance for each bot
random_bot_avg = [sum(random_bot[::4]) / len(random_bot[::4]),
                  sum(random_bot[1::4]) / len(random_bot[1::4]),
                  sum(random_bot[2::4]) / len(random_bot[2::4]),
                  sum(random_bot[3::4]) / len(random_bot[3::4])]
greedy_bot_avg = [sum(greedy_bot[::4]) / len(greedy_bot[::4]),
                  sum(greedy_bot[1::4]) / len(greedy_bot[1::4]),
                  sum(greedy_bot[2::4]) / len(greedy_bot[2::4]),
                  sum(greedy_bot[3::4]) / len(greedy_bot[3::4])]
mcts3_bot_avg = [sum(mcts3_bot[::4]) / len(mcts3_bot[::4]),
                 sum(mcts3_bot[1::4]) / len(mcts3_bot[1::4]),
                 sum(mcts3_bot[2::4]) / len(mcts3_bot[2::4]),
                 sum(mcts3_bot[3::4]) / len(mcts3_bot[3::4])]

# create subplots
fig, axs = plt.subplots(2, 2)

# plot win rates
axs[0, 0].bar(["RandomBot", "GreedyBot", "MCTS3_bot"],
              [random_bot_avg[0], greedy_bot_avg[0], mcts3_bot_avg[0]])
axs[0, 0].set_title("Win Rate")

# plot time taken
axs[0, 1].bar(["RandomBot", "GreedyBot", "MCTS3_bot"],
              [random_bot_avg[1], greedy_bot_avg[1], mcts3_bot_avg[1]])
axs[0, 1].set_title("Time Taken")

# plot space used
axs[1, 0].bar(["RandomBot", "GreedyBot", "MCTS3_bot"],
              [random_bot_avg[2], greedy_bot_avg[2], mcts3_bot_avg[2]])
axs[1, 0].set_title("Space Used")

# plot score
axs[1, 1].bar(["RandomBot", "GreedyBot", "MCTS3_bot"],
              [random_bot_avg[3], greedy_bot_avg[3], mcts3_bot_avg[3]])
axs[1, 1].set_title("Score")

# show plot
plt.show()

