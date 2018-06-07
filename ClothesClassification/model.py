import keras
from keras.models import Sequential
from keras.layers import Activation, Dense, Dropout, Flatten
from keras.layers import Conv2D, MaxPooling2D
from keras import backend as K
from keras.preprocessing.image import ImageDataGenerator

K.set_image_dim_ordering('th')

from PIL import ImageFile
ImageFile.LOAD_TRUNCATED_IMAGES = True

import numpy as np

class Model:
    def __init__(self, category_num, weights_path=None):
        self.model = None
        self.is_trained = False
        if weights_path:
            self.set_model(category_num, weights_path=weights_path)
            self.is_trained = True
        else:
            self.set_model(category_num)

    def set_model(self, category_num, weights_path=None):
        model = Sequential()
        model.add(Conv2D(32, (3, 3), input_shape=(3, 150, 150)))
        model.add(Activation('relu'))
        model.add(MaxPooling2D(pool_size=(2, 2)))

        model.add(Conv2D(32, (3, 3)))
        model.add(Activation('relu'))
        model.add(MaxPooling2D(pool_size=(2, 2)))

        model.add(Conv2D(64, (3, 3)))
        model.add(Activation('relu'))
        model.add(MaxPooling2D(pool_size=(2, 2)))

        model.add(Flatten())  
        model.add(Dense(64))
        model.add(Activation('relu'))
        model.add(Dropout(0.5))
        model.add(Dense(category_num))
        model.add(Activation('softmax'))

        if weights_path:
            model.load_weights(weights_path)

        model.compile(loss=keras.losses.categorical_crossentropy,
                      optimizer=keras.optimizers.Adadelta(),
                      metrics=['accuracy'])

        self.model = model

    def get_dataset(self, data_path, batch_size=10):
        return ImageDataGenerator(rescale=1./255).flow_from_directory(data_path, 
                                                                      target_size=(150, 150), 
                                                                      batch_size=batch_size, 
                                                                      class_mode='categorical')

    def train_model(self, train_dataset, test_dataset=None, epochs=20, validation_steps=10):
        if test_dataset:
            self.model.fit_generator(train_dataset,
                                     epochs = epochs,
                                     validation_data = test_dataset,
                                     validation_steps = validation_steps)
        else:
            self.model.fit_generator(train_dataset, epochs=epochs)
        self.is_trained = True

    def save_model(self, weights_path):
        if not self.is_trained:
            print('This model is not trained. Do train model first.')
            raise AttributeError
        self.model.save_weights(weights_path)

    def predict(self, image):
        if not self.is_trained:
            print('This model is not trained. Do train model first.')
            raise AttributeError
        result = self.model.predict(image)
        return int(np.argmax(result)), float(np.max(result))
