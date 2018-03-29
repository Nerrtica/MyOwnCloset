from keras.models import Sequential
from keras.layers import Conv2D, MaxPooling2D, Flatten, Dense, Dropout
from keras.preprocessing.image import ImageDataGenerator

def get_dataset(train_folder, test_folder):
	train_datagen = ImageDataGenerator(rescale = 1./255, shear_range = 0.2, zoom_range = 0.2, horizontal_flip = True)
	test_datagen = ImageDataGenerator(rescale = 1./255)
	train_set = train_datagen.flow_from_directory(train_folder, target_size = (64, 64), batch_size = 32, class_mode = 'categorical')
	test_set = test_datagen.flow_from_directory(test_folder, target_size = (64, 64), batch_size = 32, class_mode = 'categorical')
	return train_set, test_set
