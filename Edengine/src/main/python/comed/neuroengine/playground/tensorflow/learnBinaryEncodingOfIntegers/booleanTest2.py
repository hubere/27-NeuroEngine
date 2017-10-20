from __future__ import absolute_import
from __future__ import division
from __future__ import print_function


import tensorflow as tf
import numpy as np
import collections


tf.logging.set_verbosity(tf.logging.ERROR)

# Data sets
dataComplete = np.array([[0,0,0],[0,0,1],[0,1,0],[0,1,1],[1,0,0],[1,0,1],[1,1,0],[1,1,1]], dtype=bool)
targetComplete = np.array([0,     1,      2,      3,      4,      5,      6,      7     ], dtype=np.int )

Dataset = collections.namedtuple('Dataset', ['data', 'target'])

# for trainig data, remove some data from the complete set
data = np.delete(dataComplete, [2,4,6], 0)
target = np.delete(targetComplete, [2,4,6])
training_set = Dataset(data=data, target=target)

# for test set pick some of the complete set.
data = np.array([[0,0,1], [0,1,0]], dtype=bool)
target = np.array([1,3], dtype=np.int )
test_set = Dataset(data=data, target=target)


# Specify that all features have real-value data 
# <-- This is seems to be wrong. I do not have a real valued featured, but boolean features. However
# I could not something like tf.contrib.layers.boolean_valued_column
feature_columns = [tf.contrib.layers.real_valued_column("", dimension=3)]


# Build 3 layer DNN with 10, 20, 10 units respectively.
#classifier = tf.contrib.learn.DNNClassifier(feature_columns=feature_columns, hidden_units=[10, 20, 10], n_classes=8, model_dir="/tmp/chesspositions_model")
#classifier = tf.contrib.learn.LinearClassifier(feature_columns)
#estimator = tf.contrib.learn.LinearRegressor(feature_columns)
estimator = tf.contrib.learn.DNNRegressor(feature_columns=feature_columns, hidden_units=[10, 20, 10])


# monitors 
validation_monitor = tf.contrib.learn.monitors.ValidationMonitor(
    test_set.data,
    test_set.target,
    every_n_steps=50)



# Fit model.
#classifier.fit(x=training_set.data, y=training_set.target, steps=1)
estimator.fit(x=training_set.data, y=training_set.target, steps=10, monitors=[validation_monitor])

# Evaluate accuracy.
#accuracy_score = classifier.evaluate(x=test_set.data, y=test_set.target)["accuracy"]
#print('Accuracy: {0:f}'.format(accuracy_score))


estimation = estimator.evaluate(x=test_set.data, y=test_set.target)
print('estimator.evaluate-loss: {0:f}'.format(estimation["loss"]))

for x in range(20):
  estimator.fit(x=training_set.data, y=training_set.target, steps=100)
  estimation = estimator.evaluate(x=test_set.data, y=test_set.target)
  prediction = estimator.predict(x=dataComplete, as_iterable=False)
  print(estimation)  
  

print(estimation)  
print(prediction)
print('--- The End ---')






