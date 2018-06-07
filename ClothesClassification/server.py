from socket import *
import threading
from _thread import *
import struct
import json
from keras.preprocessing.image import img_to_array, load_img
from keras import backend as K
import numpy as np

K.set_image_dim_ordering('th')

from model import Model
from getColorInformation import GetColorInformation

category = ['코트', '재킷', '정장', '후드티', '스웨터', '셔츠', '티셔츠', '청바지', '면바지', '운동화', '구두', '드레스', '블라우스', '치마', '하이힐']
pattern = ['단색', '꽃무늬', '패턴무늬', '줄무늬', '프린팅']

upper = [0, 1, 2, 3, 4, 5, 6, 11, 12]
bottom = [7, 8, 13]

HOST = ''
PORT = 11559
ADDR = (HOST, PORT)
BUFSIZE = 2048

ServerSocket = socket(AF_INET,SOCK_STREAM)
ServerSocket.bind(('',11559))
ServerSocket.listen(10)

def _writeData(file, data):
    file.write(data[:-3])
    if data[-3:] == b'end':
        print('Image Recieved!')
        raise StopIteration
    file.write(data[-3:])

def _get_img_from_path(img_path):
    img = load_img(img_path)
    img = img_to_array(img.resize((150, 150)))
    img = np.expand_dims(img, axis=0)
    return img

def clientSocket(connectionSocket, num):
    try:
        sex = None
        with open('capstone_dump_img.jpg', 'wb') as img:
            data = connectionSocket.recv(1024)
            sex = 'woman' if int(data.split(b';')[0]) else 'man'
            data = b';'.join(data.split(b';')[1:])
            _writeData(img, data)
            while True:
                data = connectionSocket.recv(1024)
                _writeData(img, data)
    except StopIteration:
        pass
    except Exception as e:
        print(repr(str(e)))
        connectionSocket.send('-1'.encode())

    try:
        getColorInformation = GetColorInformation()
        result = dict()

        img = _get_img_from_path('capstone_dump_img.jpg')

        if sex == 'woman': model = Model(15, weights_path='woman_category_model.h5')
        else: model = Model(11, weights_path='man_category_model.h5')
        category_result = model.predict(img)
        result['category'] = category_result[0]
        result['category_prob'] = category_result[1]
        del model
        K.clear_session()
        
        if result['category'] in upper or result['category'] in bottom:
            model = Model(5, weights_path='pattern_model.h5')
            pattern_result = model.predict(img)
            result['pattern'] = pattern_result[0]
            result['pattern_prob'] = pattern_result[1]
            del model
            K.clear_session()
        else:
            result['pattern'] = -1
            result['pattern_prob'] = 0

        if result['category'] in upper:
            model = Model(2, weights_path='upper_is_long_model.h5')
            is_long_result = model.predict(img)
            result['is_long'] = is_long_result[0]
            result['is_long_prob'] = is_long_result[1]
            del model
            K.clear_session()
        elif result['category'] in bottom:
            model = Model(2, weights_path='bottom_is_long_model.h5')
            is_long_result = model.predict(img)
            result['is_long'] = is_long_result[0]
            result['is_long_prob'] = is_long_result[1]
            del model
            K.clear_session()
        else:
            result['is_long'] = -1
            result['is_long_prob'] = 0

        most_colors = getColorInformation.get_most_colors('capstone_dump_img.jpg')
        color_weights = getColorInformation.get_weight_of_colors(most_colors)
        result['color'] = color_weights[0][0]

        connectionSocket.send(json.dumps(result).encode())
        if result['pattern'] != -1:
            print('category : %s (%.3f) / pattern : %s (%.3f) / is_long : %d (%.3f)' % (category[result['category']], result['category_prob'],
                                                                                        pattern[result['pattern']], result['pattern_prob'],
                                                                                        result['is_long'], result['is_long_prob']))
        else:
            print('category : %s (%.3f) / is_long : %d (%.3f)' % (category[result['category']], result['category_prob'],
                                                                  result['is_long'], result['is_long_prob']))

    except Exception as e:
        print(repr(str(e)))
        connectionSocket.send('-2'.encode())

while True :
    (connectionSocket, clientAddress) = ServerSocket.accept()
    num = 0
    start_new_thread(clientSocket,(connectionSocket,num))
