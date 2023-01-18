import plotly
import plotly.express as px
import pandas as pd

df = pd.read_csv("dataMCTS4.csv")

# Create the 3D scatter plot
fig = px.scatter_3d(df, x='IterationCount', y=' UTC constant', z=' Wins',
                    color=' Wins', size=' Wins',log_x=True)
fig.show()
plotly.offline.plot(fig, auto_open=True)


#%%
from plotly.graph_objs import Scatter3d

trace = Scatter3d(x=df['IterationCount'], y=df[' UTC constant'], z=df[' Wins'],
                  mode='lines+markers', marker={'size': 5, 'color': 'red'})

layout = {'scene': {'xaxis': {'title': 'IterationCount'},
                    'yaxis': {'title': ' UTC constant'},
                    'zaxis': {'title': ' Wins'}}}

fig = {'data': [trace], 'layout': layout}

# Show the plot
plotly.offline.plot(fig, auto_open=True)
