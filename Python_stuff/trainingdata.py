import pandas as pd
import numpy as np
from scipy.interpolate import interp1d
import matplotlib.pyplot as plt
rand = '10'
runs = '1000'
data = pd.read_csv('randombound'+rand+'_MDPtest_Current'+runs)
data1 = pd.read_csv('randombound'+rand+'_MDPtest_ALL'+runs)
data2 = pd.read_csv('randombound'+rand+'_MDPtest_Minimal'+runs)
data3 = pd.read_csv('randombound'+rand+'_MDPtest_veryminimal'+runs)
x = data['iterations']
y = data['winrate']
z = data['Size']
z1 = data1['Size']
z2 = data2['Size']
z3 = data3['Size']
x1 = data1['iterations']
y1 = data1['winrate']
x2 = data2['iterations']
y2 = data2['winrate']
x3 = data3['iterations']
y3 = data3['winrate']

plt.title("Winrate with "+rand+" seeded games")
plt.plot(x, y, label='Current')
plt.plot(x1, y1, label='All')
plt.plot(x2, y2, label='Minimal')
plt.plot(x3, y3, label='Bare')
plt.xlabel('Iterations')
plt.ylabel('Winrate (%)')
plt.legend()
plt.show()
plt.savefig(rand+"-wintest.png", dpi = 100)




#%%

plt.plot(x, z, label='Current')
plt.plot(x1, z1, label='All')
plt.plot(x2, z2, label='Minimal')
plt.plot(x3, z3, label='Bare')
plt.xlabel('Iterations')
plt.ylabel('Size')
plt.legend()
plt.tight_layout()
plt.title("Size of Q-Table")
plt.show()
plt.savefig(rand+"-Sizetest.png", dpi = 100)
#%%
