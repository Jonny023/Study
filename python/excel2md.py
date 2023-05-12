import pandas as pd
engine = 'python'

# df = pd.read_csv('无纸化会议系统.csv',engine=engine)  # 如果是 csv 文件
df = pd.read_excel('市场费用.xlsx')

md = df.to_markdown()
with open('output-报价.md', 'w') as f:
    f.write(md)