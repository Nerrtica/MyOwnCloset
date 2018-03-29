from keras.models import Sequential
from keras.layers import Conv2D, MaxPooling2D, Flatten, Dense, Dropout
from keras.preprocessing.image import ImageDataGenerator

def get_dataset(train_folder, test_folder, target_size):
	train_datagen = ImageDataGenerator(rescale = 1./255, shear_range = 0.2, zoom_range = 0.2, horizontal_flip = True)
	test_datagen = ImageDataGenerator(rescale = 1./255)
	train_set = train_datagen.flow_from_directory(train_folder, target_size = target_size, batch_size = 32, class_mode = 'categorical')
	test_set = test_datagen.flow_from_directory(test_folder, target_size = target_size, batch_size = 32, class_mode = 'categorical')
	return train_set, test_set

def generate_model(input_shape, category_num):
	model = Sequential()
	model.add(Conv2D(32, (3, 3), padding='same', activation='relu', input_shape=input_shape))
	model.add(Conv2D(32, (3, 3), activation='relu'))
	model.add(MaxPooling2D(pool_size=(2, 2)))
	model.add(Dropout(0.25))

	model.add(Conv2D(64, (3, 3), padding='same', activation='relu'))
	model.add(Conv2D(64, (3, 3), activation='relu'))
	model.add(MaxPooling2D(pool_size=(2, 2)))
	model.add(Dropout(0.25))

	model.add(Conv2D(64, (3, 3), padding='same', activation='relu'))
	model.add(Conv2D(64, (3, 3), activation='relu'))
	model.add(MaxPooling2D(pool_size=(2, 2)))
	model.add(Dropout(0.25))

	model.add(Flatten())
	model.add(Dense(512, activation='relu'))
	model.add(Dropout(0.5))
	model.add(Dense(category_num, activation='softmax'))

	model.compile(optimizer='rmsprop', loss='categorical_crossentropy', metrics=['accuracy'])
	return model
