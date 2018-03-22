import sys
import json
import numpy as np
import matplotlib.pyplot as plt
import keras
from keras.models import Sequential
from keras.layers import Dense, Conv2D, MaxPooling2D, Dropout, Flatten
from keras.utils import to_categorical
from PIL import Image

def read_data(l, c, size):
	links = []
	labels = []
	with open(l) as f:
	    count = 0
	    prev = -1
	    for line in f:
	        category = int(line.split('/')[0])
	        if category == prev:
	            count += 1
	        else:
	            count = 0
	            prev = category
	        if c != 0 and count >= c:
	            continue
	        links.append('ACS/images/'+line.strip()+'.jpg')
	        labels.append(category)
	labels = np.array(labels)

	data = []
	for i, link in enumerate(links):
	    if (c != 0 and i % c == 0) or (c == 0 and i % 10000 == 0):
	        sys.stdout.write('\rProcessing ... %d' % i)
	    img = Image.open(link)
		arr = np.array(img.resize(size))
	    data.append(arr)
	data = np.array(data)

	return (links, data, labels)

train_links, train_data, train_labels = read_data('ACS/train.txt', 100, (256, 256))
test_links, test_data, test_labels = read_data('ACS/test.txt', 100, (256, 256))