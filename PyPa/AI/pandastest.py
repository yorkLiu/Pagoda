

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt



print pd.Series([1, 3, 5, 7,9, np.nan, 10])

dates = pd.date_range('2017-07-01', periods=10, freq='D')

df = pd.DataFrame(np.random.randn(10, 4), index=dates, columns=list('ABCD'))
print df.head(6)
print df.tail(4)

print df.index
print df.columns
print df.values
print df.describe()
print df.T

print df.sort_index(axis=1, ascending=False)
print df.sort_values(by='B')

print df['A']
print df[0:2]
print df['2017-07-02':'2017-07-05']

print df.loc[dates[2]]

print df.loc[:,['A', 'B']]
print df.loc['2017-07-01':'2017-07-03',['A', 'B']]
print df.loc[dates[0],'A']
print df.at[dates[0],'A']

print df.iloc[3]

ts = pd.Series(np.random.randn(1000), index=pd.date_range('1/1/2000', periods=1000))

ts = ts.cumsum()
print ts

# ts.plot()
# plt.show()

pdf = pd.DataFrame(np.random.randn(1000, 4), index=ts.index,
                                   columns=['A', 'B', 'C', 'D'])
pdf.cumsum()
pdf.plot()
# plt.figure(); pdf.plot(); plt.legend(loc='best')
plt.show()


# print np.array([3, 4, 5]*4)