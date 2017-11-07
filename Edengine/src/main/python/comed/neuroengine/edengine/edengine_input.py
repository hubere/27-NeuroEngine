from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

from tensorflow.python.platform import gfile

import tensorflow as tf
import numpy as np
import csv
import os
import collections




filenameTrainingsSet = "D:\\usr\\huber\\Projekte\\2-development\\27-NeuroEngine\\Edengine\\src\\main\\python\\comed\\neuroengine\\edengine\\data\\stockfishEvaluations\\kaggle_chesspositions_training.extFEN"
filenameTestSet =      "D:\\usr\\huber\\Projekte\\2-development\\27-NeuroEngine\\Edengine\\src\\main\\python\\comed\\neuroengine\\edengine\\data\\stockfishEvaluations\\kaggle_chesspositions_test.extFEN"


#
# defining columns
#

CSV_COLUMNS = ["a1", "a2", "a3" , "a4", "a5", "a6", "a7", "a8",
               "b1", "b2", "b3" , "b4", "b5", "b6", "b7", "b8",
               "c1", "c2", "c3" , "c4", "c5", "c6", "c7", "c8",
               "d1", "d2", "d3" , "d4", "d5", "d6", "d7", "d8",
               "e1", "e2", "e3" , "e4", "e5", "e6", "e7", "e8",
               "f1", "f2", "f3" , "f4", "f5", "f6", "f7", "f8",
               "g1", "g2", "g3" , "g4", "g5", "g6", "g7", "g8",
               "h1", "h2", "h3" , "h4", "h5", "h6", "h7", "h8",
               "ActiveColour", "Castling", "EnPassant", "eval"]
  

#
# TODO FIXME check difference of tf.feature_column.indicator_column and tf.feature_column.embedding_column (see https://www.kaggle.com/mmmarcy/tensorflow-dnn-regressor-with-feature-engineering)
#
feature_columns = []
pieces = ["r", "n" , "b", "q", "k", "p", "R", "N", "B", "Q", "K", "P"]
#for line in range(1,8):
#  for row in range(1,8):
for line in range(1,3):
  for row in range(1,3):
    key = chr(96+line) + str(row)  # build key "a1", "a2", a3 ....
    feature_columns.append(tf.feature_column.indicator_column(tf.feature_column.categorical_column_with_vocabulary_list(key, pieces)))

ActiveColour = tf.feature_column.indicator_column(tf.feature_column.categorical_column_with_vocabulary_list( "ActiveColour", ["w", "b"]))
Castling     = tf.feature_column.indicator_column(tf.feature_column.categorical_column_with_vocabulary_list( "Castling",     ["k" , "q", "K", "Q", "-"]))
# EnPassant    = tf.feature_column.categorical_column_with_vocabulary_list( "EnPassant",    ["-", "*" ]) # DO NOT USE for there could be any field instead of '-'


feature_columns.append(ActiveColour)
feature_columns.append(Castling)






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


