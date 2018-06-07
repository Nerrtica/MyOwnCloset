from PIL import Image
import colorsys
import math
 
def convert_rgb_to_hsv(rgb):
    r = int(rgb[:2], 16)
    g = int(rgb[2:4], 16)
    b = int(rgb[4:], 16)
    
    hsv = colorsys.rgb_to_hsv(r/255, g/255, b/255)
    return (round(hsv[0]*360), round(hsv[1]*100), round(hsv[2]*100))
 
class GetMostCommonColors:
    def __init__(self):
        self.PREVIEW_WIDTH = 150
        self.PREVIEW_HEIGHT = 150
        self.error = None
        
    def get_color(self, img_link, count=20, reduce_brightness=True, reduce_gradients=True, delta=16):
        if delta > 2:
            half_delta = delta / 2 - 1
        else:
            half_delta = 0
        
        # get image size (width, height)
        img = Image.open(img_link)
        img_size = img.size
        scale = 1
        
        # image resizing
        if img_size[0] > 0 and img_size[1] > 0:
            scale = min(self.PREVIEW_WIDTH/img_size[0], self.PREVIEW_HEIGHT/img_size[1])
        if scale < 1:
            width = math.floor(scale*img_size[0])
            height = math.floor(scale*img_size[1])
        else:
            width = img_size[0]
            height = img_size[1]
            
        img_resized = img.resize((width, height))
        
        total_pixel_count = 0
        pixel = img_resized.load()
        hex_array = dict()
        for y in range(height):
            for x in range(width):
                total_pixel_count += 1
                colors = pixel[x, y]
                if delta > 1:
                    red = int((colors[0]+half_delta)/delta)*delta
                    green = int((colors[1]+half_delta)/delta)*delta
                    blue = int((colors[2]+half_delta)/delta)*delta
                    if red >= 256: red = 255
                    if green >= 256: green = 255
                    if blue >= 256: blue = 255
                
                hex_val = hex(red).replace('x', '')[-2:] + hex(green).replace('x', '')[-2:] + hex(blue).replace('x', '')[-2:]
                
                w_x = 1 - abs(((width / 2) - x) / (width / 2))
                w_y = 1 - abs(((height / 2) - y) / (height / 2))
                w_v = w_x * w_y
                
                # 이 곳에 가중치를 줄 수 있음.
                if not hex_array.get(hex_val):
                    hex_array[hex_val] = w_v
                else:
                    hex_array[hex_val] += w_v
                    
        if reduce_gradients:
            sorted_hex_array = sorted(hex_array.items(), key=lambda x:x[1], reverse=True)
            
            gradients = dict()
            for hex_val, num in sorted_hex_array:
                if not gradients.get(hex_val):
                    new_hex = GetMostCommonColors._find_adjacent(hex_val, gradients, delta)
                    gradients[hex_val] = new_hex
                else:
                    new_hex = gradients[hex_val]
                    
                if hex_val != new_hex:
                    hex_array[hex_val] = 0
                    hex_array[new_hex] += num
                    
        if reduce_brightness:
            sorted_hex_array = sorted(hex_array.items(), key=lambda x:x[1], reverse=True)
            
            brightness = dict()
            for hex_val, num in sorted_hex_array:
                if not brightness.get(hex_val):
                    new_hex = GetMostCommonColors._normalize(hex_val, brightness, delta)
                    brightness[hex_val] = new_hex
                else:
                    new_hex = brightness[hex_val]
                
                if hex_val != new_hex:
                    hex_array[hex_val] = 0
                    hex_array[new_hex] += num
                    
        for key, value in hex_array.items():
            hex_array[key] = float(value) / total_pixel_count
 
        sorted_hex_array = sorted(hex_array.items(), key=lambda x:x[1], reverse=True)
        
        if count > 0: return sorted_hex_array[:count]
        else: return sorted_hex_array
        
    def _convert_rgb_to_hsv(rgb):
        r = int(rgb[:2], 16)
        g = int(rgb[2:4], 16)
        b = int(rgb[4:], 16)
 
        hsv = colorsys.rgb_to_hsv(r/255, g/255, b/255)
        return (round(hsv[0]*360), round(hsv[1]*100), round(hsv[2]*100))
        
    def _find_adjacent(hex_val, gradients, delta):
        red = int(hex_val[:2], 16)
        green = int(hex_val[2:4], 16)
        blue = int(hex_val[4:], 16)
        
        if red > delta:
            new_hex = hex(red - delta).replace('x', '')[-2:] + hex(green).replace('x', '')[-2:] + hex(blue).replace('x', '')[-2:]
            if gradients.get(new_hex):
                return gradients[new_hex]
        if green > delta:
            new_hex = hex(red).replace('x', '')[-2:] + hex(green - delta).replace('x', '')[-2:] + hex(blue).replace('x', '')[-2:]
            if gradients.get(new_hex):
                return gradients[new_hex]
        if blue > delta:
            new_hex = hex(red).replace('x', '')[-2:] + hex(green).replace('x', '')[-2:] + hex(blue - delta).replace('x', '')[-2:]
            if gradients.get(new_hex):
                return gradients[new_hex]
        
        if red < 255 - delta:
            new_hex = hex(red + delta).replace('x', '')[-2:] + hex(green).replace('x', '')[-2:] + hex(blue).replace('x', '')[-2:]
            if gradients.get(new_hex):
                return gradients[new_hex]
        if green < 255 - delta:
            new_hex = hex(red).replace('x', '')[-2:] + hex(green + delta).replace('x', '')[-2:] + hex(blue).replace('x', '')[-2:]
            if gradients.get(new_hex):
                return gradients[new_hex]
        if blue < 255 -  delta:
            new_hex = hex(red).replace('x', '')[-2:] + hex(green).replace('x', '')[-2:] + hex(blue + delta).replace('x', '')[-2:]
            if gradients.get(new_hex):
                return gradients[new_hex]
            
        return hex_val
    
    def _normalize(hex_val, hex_array, delta):
        lowest = 255
        highest = 0
        red = int(hex_val[:2], 16)
        green = int(hex_val[2:4], 16)
        blue = int(hex_val[4:], 16)
        
        if red < lowest: lowest = red
        if green < lowest: lowest = green
        if blue < lowest: lowest = blue
        
        if red > highest: highest = red
        if green > highest: highest = green
        if blue > highest: highest = blue
            
        if lowest == highest:
            if delta <= 32:
                if lowest == 0 or highest >= 255 - delta:
                    return hex_val
            else:
                return hex_val
        
        while highest < 256:
            new_hex = hex(red - lowest).replace('x', '')[-2:] + hex(green - lowest).replace('x', '')[-2:] + hex(blue - lowest).replace('x', '')[-2:]
            if hex_array.get(new_hex):
                return new_hex
            lowest += delta
            highest += delta
            
        return hex_val
 
def get_most_color(colors, count=1):
    color_result = dict()
    for i in range(11):
        color_result[i] = 0
    for color in colors:
        hsv = convert_rgb_to_hsv(color[0])
        prop = color[1]
 
        if hsv[1] < 15:
            # 화이트
            if hsv[2] > 70: c = 8
            # 그레이
            elif hsv[2] > 40: c = 9
            # 블랙
            else: c = 10
        else:
            if hsv[2] < 40: c = 10
            else:
                # 레드
                if hsv[0] < 15 or hsv[0] >= 350: 
                    if hsv[1] < 60 and hsv[2] < 45: c = 7
                    else: c = 0
                # 주황
                if hsv[0] < 40:
                    if hsv[1] + hsv[2] < 170: c = 7
                    else: c = 4
                # 옐로
                elif hsv[0] < 60: 
                    if hsv[2] < 70: c= 7
                    else: c = 1
                # 그린
                elif hsv[0] < 160: c = 2
                # 블루
                elif hsv[0] < 255: c = 3
                # 퍼플
                elif hsv[0] < 290: c = 5
                # 핑크
                elif hsv[0] < 350: c = 6
                else: continue
        color_result[c] += prop
    
    sorted_color_result = sorted(color_result.items(), key=lambda x:x[1], reverse=True)
    return sorted_color_result[:count]
 
import keras
from keras.datasets import mnist
from keras.models import Sequential
from keras.layers import Activation, Dense, Dropout, Flatten
from keras.layers import Conv2D, MaxPooling2D
from keras import backend as K
from keras.preprocessing.image import ImageDataGenerator
 
K.set_image_dim_ordering('th')
 
from PIL import ImageFile
ImageFile.LOAD_TRUNCATED_IMAGES = True
 
def get_model(category_num, weights_path=None):
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
    
    return model
 
category = ['코트', '재킷', '정장', '후드티', '스웨터', '셔츠', '티셔츠', '청바지', '면바지', '운동화', '구두', '드레스', '블라우스', '치마', '하이힐']
pattern = ['단색', '꽃무늬', '패턴무늬', '줄무늬', '프린팅']
upper = [0, 1, 2, 3, 4, 5, 6, 11, 12]
bottom = [7, 8, 13]
 
from keras.preprocessing.image import img_to_array, load_img
import numpy as np
 
from socket import *
import threading
from _thread import *
import struct
import json
 
HOST = ''
PORT = 11559
ADDR = (HOST, PORT)
BUFSIZE = 2048
 
ServerSocket = socket(AF_INET,SOCK_STREAM)
ServerSocket.bind(('',11559))
ServerSocket.listen(10)
 
def _writeData(file, data):
    file.write(data[:-3])
#     if not data or data[-3:] == b'end':
    if data[-3:] == b'end':
        print('Image Recieved!')
        raise StopIteration
    file.write(data[-3:])
 
def clientSocket(connectionSocket, num):
    try:
        sex = None
        with open('test.jpg', 'wb') as img:
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
        result = dict()
 
        img = load_img('test.jpg')
        img = img_to_array(img.resize((150,150)))
        img /= 255.
        img = np.expand_dims(img, axis=0)
 
        if sex == 'woman': model = get_model(15, weights_path='woman_category_model.h5')
        else: model = get_model(11, weights_path='man_category_model.h5')
 
        category_result = model.predict(img)
        result['category'] = int(np.argmax(category_result))
        result['category_prob'] = float(np.max(category_result))
        del model
        K.clear_session()
        
        if result['category'] in upper or result['category'] in bottom:            
            model = get_model(5, weights_path='pattern_model.h5')
            pattern_result = model.predict(img)
            result['pattern'] = int(np.argmax(pattern_result))
            result['pattern_prob'] = float(np.max(pattern_result))
            del model
            K.clear_session()
        else:
            result['pattern'] = 0
            result['pattern_prob'] = 0
 
        if result['category'] in upper:
            model = get_model(2, weights_path='upper_is_long_model.h5')
            is_long_result = model.predict(img)
            result['is_long'] = int(np.argmax(is_long_result))
            result['is_long_prob'] = float(np.max(is_long_result))
            del model
            K.clear_session()
        elif result['category'] in bottom:
            model = get_model(2, weights_path='bottom_is_long_model.h5')
            is_long_result = model.predict(img)
            result['is_long'] = int(np.argmax(is_long_result))
            result['is_long_prob'] = float(np.max(is_long_result))
            del model
            K.clear_session()
        else:
            result['is_long'] = 0
            result['is_long_prob'] = 0
 
        # color 추출
        colors = get_most_color(GetMostCommonColors().get_color('test.jpg', count=20), 1)
        result['color'] = colors[0][0]
 
        connectionSocket.send(json.dumps(result).encode())
        print('category : %s (%.3f) / pattern : %s (%.3f) / is_long : %d (%.3f)' % (category[result['category']], result['category_prob'],
                                                                                    pattern[result['pattern']], result['pattern_prob'],
                                                                                    result['is_long'], result['is_long_prob']))

    except Exception as e:
        print(repr(str(e)))
        connectionSocket.send('-2'.encode())
 
while True :
    (connectionSocket, clientAddress) = ServerSocket.accept()
    num = 0
    start_new_thread(clientSocket,(connectionSocket,num))
