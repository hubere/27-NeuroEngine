#
# Create your first MLP in Keras
#
# from https://machinelearningmastery.com/tutorial-first-neural-network-python-keras/
# and https://fizzylogic.nl/2017/05/08/monitor-progress-of-your-keras-based-neural-network-using-tensorboard/
#

from time import time
from keras.models import Sequential
from keras.layers import Dense
from keras.callbacks import TensorBoard

from comed.neuroengine.edengine import edengine_init

import numpy

# fix random seed for reproducibility
numpy.random.seed(7)

# load pima indians dataset
dataset = numpy.loadtxt("pima-indians-diabetes.csv", delimiter=",")

# split into input (X) and output (Y) variables
X = dataset[:,0:8]
Y = dataset[:,8]

# create model
model = Sequential()
model.add(Dense(12, input_dim=8, activation='relu'))
model.add(Dense(8, activation='relu'))
model.add(Dense(1, activation='sigmoid'))

# Compile model
model.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])

# create tensorboard data
tensorboard = TensorBoard(log_dir=edengine_init.logDir + "/{}".format(time()))

# Fit the model
model.fit(X, Y, epochs=150, batch_size=10, callbacks=[tensorboard])

# evaluate the model
scores = model.evaluate(X, Y)
print("\n%s: %.2f%%" % (model.metrics_names[1], scores[1]*100))
