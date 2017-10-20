from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

from tensorflow.python.platform import gfile

import tensorflow as tf
import numpy as np
import os
import csv
import collections


# Data sets
#IRIS_TRAINING = "1000-chesspositions_training.csv"
#IRIS_TRAINING = "2-chesspositions.csv"
IRIS_TRAINING = "3-pawnsPositions_training.csv"
IRIS_TEST = "3-pawnsPositions_test.csv"



#
# My helper
#

Dataset = collections.namedtuple('Dataset', ['data', 'target'])

#
# taken from tensorflow/.../base.py
# adjusted to interpret '1' and '0' as boolean values
#
def load_csv_with_header_to_bool(filename,
                         target_dtype,
                         features_dtype = np.bool,
                         target_column=-1):
  """Load dataset from CSV file with a header row."""
  print("Loading dataset " + filename)  
  with gfile.Open(filename) as csv_file:
    data_file = csv.reader(csv_file)
    header = next(data_file)
    n_samples = int(header[0])
    n_features = int(header[1])
    data = np.zeros((n_samples, n_features), dtype=features_dtype)
    target = np.zeros((n_samples,), dtype=target_dtype)
    for i, row in enumerate(data_file):
      target[i] = np.asarray(row.pop(target_column), dtype=target_dtype)
      row = [ int(x) for x in row ]
      row = [ bool(x) for x in row ]
      data[i] = np.asarray(row, dtype=features_dtype)

  return Dataset(data=data, target=target)

def str_to_bool2(s):
    return (bool(int(s)))

def str_to_bool(s):
    if s == '1':
         return True
    elif s == '0':
         return False
    else:
         raise ValueError # evil ValueError that doesn't tell you what the wrong value was
       


# Load datasets.
training_set = load_csv_with_header_to_bool( filename=IRIS_TRAINING, target_dtype=np.int)
test_set = load_csv_with_header_to_bool( filename=IRIS_TEST, target_dtype=np.int)



# Specify that all features have real-value data
feature_columns = [tf.contrib.layers.real_valued_column("", dimension=19)]

#sparse_bit_features = tf.train.Feature(int64_list = tf.train.Int64List(value= a_sparse))
#features = {'sparse_bit_features' : sparse_bit_features }
#feature_columns = [tf.contrib.layers.sparse_column_with_integerized_feature("", dimension=768)]

# feature_columns = [tf.contrib.layers.real_valued_column("", dimension=768)]

# Build 3 layer DNN with 10, 20, 10 units respectively.
#classifier = tf.contrib.learn.DNNClassifier(feature_columns=feature_columns, <-- we do not need a classifier!!
#                                            hidden_units=[10, 20, 10],
#                                            n_classes=3,
#                                            model_dir="/tmp/chesspositions_model")

classifier = tf.contrib.learn.LinearClassifier(feature_columns, model_dir = os.getcwd() + "/model")

# Fit model.
classifier.fit(x=training_set.data,
               y=training_set.target,
               steps=100)


# Evaluate accuracy.
accuracy_score = classifier.evaluate(x=test_set.data,
                                     y=test_set.target)["accuracy"]
print('Accuracy: {0:f}'.format(accuracy_score))

# Classify two new flower samples.
#new_samples = np.array(
#    [[6.4, 3.2, 4.5, 1.5], [5.8, 3.1, 5.0, 1.7]], dtype=float)
#y = list(classifier.predict(new_samples, as_iterable=True))
#print('Predictions: {}'.format(str(y)))



