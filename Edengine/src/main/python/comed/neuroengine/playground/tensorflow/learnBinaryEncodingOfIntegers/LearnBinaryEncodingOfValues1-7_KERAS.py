# Create your first MLP in Keras
from keras.models import Sequential
from keras.layers import Dense
from keras.callbacks import TensorBoard
from keras.wrappers.scikit_learn import KerasRegressor
from sklearn.model_selection import cross_val_score
from sklearn.model_selection import KFold

from comed.neuroengine.edengine import edengine_init
from comed.neuroengine.edengine import edengine_input

import numpy
from time import time

# fix random seed for reproducibility
seed = 7
numpy.random.seed(seed)

#
# Load datasets.
#
training_set = edengine_input.get_trainings_set("binaryEncoding_training.csv")
test_set = edengine_input.get_test_set("binaryEncoding_test.csv")
DIMENSION = 4
#dataset = numpy.loadtxt("pima-indians-diabetes.csv", delimiter=",")

# split into input (X) and output (Y) variables
#X = training_set[:,0:8]
#Y = training_set[:,8]
X = training_set.data
Y = training_set.target


# create tensorboard data
tensorboard = TensorBoard(log_dir=edengine_init.logDir + "/{}".format(time()))


#
# create model
# Compile model
#
def baseline_model():
  model = Sequential()
  model.add(Dense(4, input_dim=3, activation='relu'))
  #model.add(Dense(8, activation='relu'))
  model.add(Dense(1, activation='sigmoid'))  
  model.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])
  return model


#
# Fit the model
#
#model.fit(X, Y, epochs=150, batch_size=10)

# evaluate model with standardized dataset
estimator = KerasRegressor(build_fn=baseline_model, nb_epoch=100, batch_size=5, verbose=0)

#
# evaluate the model
#
kfold = KFold(n_splits=7, random_state=seed)
results = cross_val_score(estimator, X, Y, cv=kfold, fit_params={'callbacks' : [tensorboard]})
print("Results: %.2f (%.2f) MSE" % (results.mean(), results.std()))


