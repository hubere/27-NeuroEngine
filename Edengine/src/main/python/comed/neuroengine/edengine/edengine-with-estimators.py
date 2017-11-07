from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

#from sacred import Experiment

import tensorflow as tf
import numpy as np
import os
import collections
import argparse
import sys
import pandas
import tempfile

import edengine_init
import edengine_input
import edengine_monitor

#
# global settings
#
FLAGS = None
# tf.logging.set_verbosity(tf.logging.INFO)


filenameTrainingsSet = "D:\\usr\\huber\\Projekte\\2-development\\27-NeuroEngine\\Edengine\\src\\main\\python\\comed\\neuroengine\\edengine\\data\\stockfishEvaluations\\70_kaggle_chesspositions_training.extFEN"
filenameTestSet =      "D:\\usr\\huber\\Projekte\\2-development\\27-NeuroEngine\\Edengine\\src\\main\\python\\comed\\neuroengine\\edengine\\data\\stockfishEvaluations\\8_of_70_kaggle_chesspositions_test.extFEN"


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


 
def input_fn(data_file, num_epochs, shuffle):
  """Input builder function."""
  
  df_data = pandas.read_csv(data_file, names=CSV_COLUMNS, engine="python", skipinitialspace=True, skiprows=1, na_values='.' )
#  labels = df_data["eval"].astype(pandas.np.float64) # normalize evaluations to -1.0 to 1.0
  labels = df_data["eval"].apply(lambda x: x /100.0).astype(pandas.np.float64) # normalize evaluations to -1.0 to 1.0
      
  # labels = df_data["eval"].apply(lambda x: x + 10000).astype(int)  
  del df_data['eval']  # remove column "eval" from df_data
  
  return tf.estimator.inputs.pandas_input_fn(
      x=df_data,
      y=labels,
      batch_size=100,
      num_epochs=num_epochs,
      shuffle=shuffle,
      num_threads=5)

  
def build_estimator(model_dir, model_type):
  """Build an estimator."""
  print ("build_estimator('"+model_dir+"', '"+model_type+"')")

  #
  # This code from https://github.com/tensorflow/tensorflow/blob/r1.3/tensorflow/examples/learn/wide_n_deep_tutorial.py 
  # feature_columns were replaces with my own in oder to compile. This will not work properly for other model_type then "wide"
  #  
  if model_type == "wide":
    m = tf.contrib.learn.LinearRegressor(
        model_dir=model_dir, 
        feature_columns=feature_columns)
  elif model_type == "deep":
    m = tf.estimator.DNNRegressor(
        model_dir=model_dir,
        feature_columns=feature_columns,
        activation_fn=tf.nn.tanh,
        hidden_units=[68, 34])
  else:
    m = tf.estimator.DNNLinearCombinedRegressor(
        model_dir=model_dir,
        linear_feature_columns=feature_columns,
        dnn_feature_columns=feature_columns,
        dnn_hidden_units=[100, 50])
  return m
  

def train_and_eval(model_dir, model_type, train_steps, train_data, test_data):
  """Train and evaluate the model."""
  print ("train_and_eval('"+model_dir+"','"+ model_type+"','"+ str(train_steps)+"','"+ train_data+"','"+ test_data+"')")
  
  #filenameTrainingsSet, filenameTestSet = maybe_download(train_data, test_data)
  model_dir = tempfile.mkdtemp() if not model_dir else model_dir

  estimator = build_estimator(model_dir, model_type)
  
  
  for x in range(200):
    
    estimator.train(
        input_fn=input_fn(train_data, num_epochs=None, shuffle=True),
        steps=train_steps)

    # set steps to None to run evaluation until all data consumed.
    results = estimator.evaluate(
        input_fn=input_fn(test_data, num_epochs=1, shuffle=False),
        steps=None)
    for key in sorted(results):
      print("%s: %s \t" % (key, results[key]) , sep=' ', end='', flush=True)
    print ("")

  
  print ("--- The End ---")
  
  
def main(_):
#  if tf.gfile.Exists(FLAGS.log_dir):
#    tf.gfile.DeleteRecursively(FLAGS.log_dir)
#  tf.gfile.MakeDirs(FLAGS.log_dir)
  
  train_and_eval(FLAGS.model_dir, FLAGS.model_type, FLAGS.train_steps,
                 FLAGS.train_data, FLAGS.test_data)


 
  
if __name__ == "__main__":
  parser = argparse.ArgumentParser()
  parser.register("type", "bool", lambda v: v.lower() == "true")
  parser.add_argument(
      "--model_dir",
      type=str,
      default="/usr/huber/Projekte/2-development/27-NeuroEngine/model/deep/68_34/tanh",
      help="Base directory for output models."
  )
  parser.add_argument(
      "--model_type",
      type=str,
      default="deep",
      help="Valid model types: {'wide', 'deep', 'wide_n_deep'}."
  )
  parser.add_argument(
      "--train_steps",
      type=int,
      default=200,
      help="Number of training steps."
  )
  parser.add_argument(
      "--train_data",
      type=str,
      default=filenameTrainingsSet,
      help="Path to the training data."
  )
  parser.add_argument(
      "--test_data",
      type=str,
      default=filenameTestSet,
      help="Path to the test data."
  )
  parser.add_argument(
      '--log_dir', 
      type=str, 
      default=edengine_init.logDir,
      help='Summaries log directory'
  )
  
  FLAGS, unparsed = parser.parse_known_args()
tf.app.run(main=main, argv=[sys.argv[0]] + unparsed)