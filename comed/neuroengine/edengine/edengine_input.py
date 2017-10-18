from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

from tensorflow.python.platform import gfile

import tensorflow as tf
import numpy as np
import csv
import os
import collections



# Global constants describing the data set.
#TRAINING_FILE = "1000-chesspositions_training.csv"
#TEST_FILE = "1000-chesspositions_test.csv"
#DIMENSION = 768

#TRAINING_FILE = "3-pawnsPositions_training.csv"
#TEST_FILE = "3-pawnsPositions_test.csv"
#DIMENSION = 19

#TRAINING_FILE = "binaryEncoding_training.csv"
#TEST_FILE = "binaryEncoding_test.csv"
#DIMENSION = 4



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


  # Display the training images in the visualizer.
  tf.summary.image('positions', data)
  
  return Dataset(data=data, target=target)
#  return collections.namedtuple('Dataset', ['data', 'target'])

def str_to_bool2(s):
    return (bool(int(s)))

def str_to_bool(s):
    if s == '1':
         return True
    elif s == '0':
         return False
    else:
         raise ValueError # evil ValueError that doesn't tell you what the wrong value was
       
       
def get_trainings_set(trainingFileName):
  return load_csv_with_header_to_bool(filename=trainingFileName, target_dtype=np.int)

def get_test_set(testFileName):
  return load_csv_with_header_to_bool(filename=testFileName, target_dtype=np.int)


