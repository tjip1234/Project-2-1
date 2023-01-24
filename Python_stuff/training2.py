import pandas as pd
import numpy as np
from scipy.interpolate import interp1d
import matplotlib.pyplot as plt
rand = '0'
runs = '10000000'
data = pd.read_csv('randombound'+rand+'_MDPtest_Current'+runs)
data1 = pd.read_csv('randombound'+rand+'_MDPtest_ALL'+runs)

x = data['iterations']
y = data['winrate']
z = data['Size']
z1 = data1['Size']
x1 = data1['iterations']
y1 = data1['winrate']
k = data['Variance']
k1 = data1['Variance']
l = data['Mean']
l1 = data1['Mean']


plt.title("Winrate On actual data")
plt.plot(x, y, label='Current')
plt.plot(x1, y1, label='All')

plt.xlabel('Iterations')
plt.ylabel('Winrate (%)')
plt.legend()
plt.savefig(rand+"-wintest.png" )
plt.show()





#%%

plt.plot(x, z, label='Current')
plt.plot(x1, z1, label='All')

plt.xlabel('Iterations')
plt.ylabel('Size')
plt.legend()
plt.tight_layout()
plt.title("Size of Q-Table")
plt.savefig(rand+"-Sizetest.png")
plt.show()

#%%
std = np.sqrt(k)
std1 = np.sqrt(k1)
plt.errorbar(x, l, yerr=std, fmt='o', label='Mean')
plt.errorbar(x1, l1, yerr=std1, fmt='o', label='Mean')
plt.set_xlabel('Iterations')
plt.set_ylabel('Value')
plt.set_title('Iterations vs Mean with Variance error')
plt.show()
#%%
