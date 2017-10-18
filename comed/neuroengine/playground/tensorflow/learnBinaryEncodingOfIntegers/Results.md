
1) binaryEncoding with LinearRegressor

estimator = tf.contrib.learn.LinearRegressor(feature_columns=feature_columns)


Loading dataset binaryEncoding_training.csv
Loading dataset binaryEncoding_test.csv
{'loss': 0.72447777, 'global_step': 200}
{'loss': 0.53716826, 'global_step': 300}
{'loss': 0.36982581, 'global_step': 400}
{'loss': 0.24560353, 'global_step': 500}
{'loss': 0.16000007, 'global_step': 600}
{'loss': 0.10309862, 'global_step': 700}
{'loss': 0.065999314, 'global_step': 800}
{'loss': 0.042080197, 'global_step': 900}
{'loss': 0.026761353, 'global_step': 1000}
{'loss': 0.01699149, 'global_step': 1100}
{'loss': 0.01077694, 'global_step': 1200}
{'loss': 0.0068306914, 'global_step': 1300}
{'loss': 0.0043275706, 'global_step': 1400}
{'loss': 0.0027407473, 'global_step': 1500}
{'loss': 0.0017355566, 'global_step': 1600}
{'loss': 0.0010988606, 'global_step': 1700}
{'loss': 0.00069568818, 'global_step': 1800}
{'loss': 0.00044041444, 'global_step': 1900}
{'loss': 0.00027879584, 'global_step': 2000}
{'loss': 0.00017649485, 'global_step': 2100}
Dataset(data=array([[False, False, False],
       [False, False,  True],
       [False,  True,  True],
       [ True, False, False],
       [ True,  True, False],
       [ True,  True,  True],
       [False, False, False]], dtype=bool), target=array([0, 1, 3, 4, 6, 7, 0]))
Dataset(data=array([[False,  True, False],
       [ True, False,  True]], dtype=bool), target=array([2, 5]))
{'loss': 0.00017649485, 'global_step': 2100}
[ 2.013026    4.98646069]


