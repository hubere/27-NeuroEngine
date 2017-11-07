from __future__ import absolute_import
from __future__ import division
from __future__ import print_function


import tensorflow as tf
import numpy as np
import os
import collections
import argparse
import sys


from comed.neuroengine.edengine import edengine_init
from comed.neuroengine.edengine import edengine_input  
from comed.neuroengine.edengine import edengine_monitor 

#
# global settings
#
FLAGS = None



def train():

  print('')
  print('')
  print('--- learning to map binary representation of numbers 1 to 7 into their decimal representation ---')
  print('')
  print('')
  
  #
  # Load datasets.
  #
  training_set = edengine_input.get_trainings_set("binaryEncoding_training.csv")
  test_set = edengine_input.get_test_set("binaryEncoding_test.csv")
  DIMENSION = 4
  
  #
  # Specify that all features have real-value data
  #
  feature_columns = [tf.contrib.layers.real_valued_column("", dimension=DIMENSION)]
  
    
  # get session
  sess = tf.InteractiveSession()
  
  #
  # Merge all the summaries and write them out to /tmp/mnist_logs (by default)
  #
  merged = tf.summary.merge_all()
  #train_writer = tf.train.SummaryWriter(FLAGS.log_dir + '/train')
  #test_writer = tf.train.SummaryWriter(FLAGS.log_dir + '/test')
  train_writer = tf.summary.FileWriter(FLAGS.log_dir + '/train', sess.graph)  
  test_writer = tf.summary.FileWriter(FLAGS.log_dir + '/test')
  tf.global_variables_initializer().run()
  
  #
  # Build 3 layer DNN with 10, 20, 10 units respectively.
  estimator = tf.contrib.learn.DNNRegressor(feature_columns=feature_columns, hidden_units=[4, 4])
  print('--- DNNRegressor(feature_columns=feature_columns, hidden_units=[100, 200, 10] ---')
 # estimator = tf.contrib.learn.LinearRegressor(feature_columns=feature_columns)
 # print('--- LinearRegressor(feature_columns=feature_columns) ---')
  
  
  # Fit model.
  estimator.fit(x=training_set.data, y=training_set.target, steps=100)
    
  
  for x in range(20):
    estimator.fit(x=training_set.data, y=training_set.target, steps=100)
    estimation = estimator.evaluate(x=test_set.data, y=test_set.target)
    prediction = estimator.predict(x=test_set.data, as_iterable=False)
    #train_writer.add_run_metadata(run_metadata, 'step%03d' % i)
    #train_writer.add_summary(summary, i)

    print(estimation)  
  
  
  estimation = estimator.evaluate(x=test_set.data, y=test_set.target)
  prediction = estimator.predict(x=test_set.data, as_iterable=False)
  
  print(training_set)
  print(test_set)
  print(estimation)  
  print(prediction)
  print('--- The End ---')



def main(_):
  if tf.gfile.Exists(FLAGS.log_dir):
    tf.gfile.DeleteRecursively(FLAGS.log_dir)
  tf.gfile.MakeDirs(FLAGS.log_dir)
  train()
  
  
if __name__ == '__main__':
  parser = argparse.ArgumentParser()
  parser.add_argument('--fake_data', nargs='?', const=True, type=bool,
                      default=False,
                      help='If true, uses fake data for unit testing.')
  parser.add_argument('--max_steps', type=int, default=1000,
                      help='Number of steps to run trainer.')
  parser.add_argument('--learning_rate', type=float, default=0.001,
                      help='Initial learning rate')
  parser.add_argument('--dropout', type=float, default=0.9,
                      help='Keep probability for training dropout.')
  parser.add_argument('--data_dir', type=str, default='/tmp/tensorflow/mnist/input_data',
                      help='Directory for storing input data')
#  parser.add_argument('--log_dir', type=str, default='/tmp/tensorflow/mnist/logs/mnist_with_summaries',
#                      help='Summaries log directory')
  parser.add_argument('--log_dir', type=str, default='/tmp/tensorflow/logs/edengine_summaries',
                      help='Summaries log directory')
  FLAGS, unparsed = parser.parse_known_args()  

  print ("FLAGS.log_dir: " + FLAGS.log_dir)
  
  tf.app.run(main=main, argv=[sys.argv[0]] + unparsed)


