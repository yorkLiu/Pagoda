

import pandas as pd

start = pd.datetime.today()
# end = datetime.date(start.year, 12, 31)
end = pd.datetime.today()

bussiness_days_rng =pd.date_range(start, periods=1, freq='BM')

print bussiness_days_rng.strftime('%Y-%m-%d')[0]

a = ['CA-3480', 'INC-1877', 'INC-1899']
print ' or summary ~ '.join(a)

