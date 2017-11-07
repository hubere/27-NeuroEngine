from __future__ import print_function


import tensorflow as tf
import os



# logging
tf.logging.set_verbosity(tf.logging.ERROR)
logDir = "/usr/huber/Projekte/2-development/27-NeuroEngine/tmp/logs/"


# print working directory
print("Path at terminal when executing this file")
print(os.getcwd() + "\n")
print("logDir= " + logDir + "\n")


