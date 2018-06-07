from PIL import Image
import colorsys
import math

class GetColorInformation:
    def __init__(self):
        self.PREVIEW_WIDTH = 150
        self.PREVIEW_HEIGHT = 150
        
    def get_most_colors(self, img_link, count=20, reduce_brightness=True, reduce_gradients=True, delta=16):
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
                
                if not hex_array.get(hex_val):
                    hex_array[hex_val] = w_v
                else:
                    hex_array[hex_val] += w_v
                    
        if reduce_gradients:
            sorted_hex_array = sorted(hex_array.items(), key=lambda x:x[1], reverse=True)
            
            gradients = dict()
            for hex_val, num in sorted_hex_array:
                if not gradients.get(hex_val):
                    new_hex = GetColorInformation._find_adjacent(hex_val, gradients, delta)
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
                    new_hex = GetColorInformation._normalize(hex_val, brightness, delta)
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

    def get_weight_of_colors(self, colors, count=1):
	    color_result = dict()
	    for i in range(11):
	        color_result[i] = 0
	    for color in colors:
	        hsv = GetColorInformation._convert_rgb_to_hsv(color[0])
	        prop = color[1]

	        if hsv[1] < 15:
	            # white
	            if hsv[2] > 70: c = 8
	            # gray
	            elif hsv[2] > 40: c = 9
	            # black
	            else: c = 10
	        else:
	            if hsv[2] < 40: c = 10
	            else:
	                # red
	                if hsv[0] < 15 or hsv[0] >= 350: 
	                    if hsv[1] < 60 and hsv[2] < 45: c = 7
	                    else: c = 0
	                # orange
	                if hsv[0] < 40:
	                    if hsv[1] + hsv[2] < 170: c = 7
	                    else: c = 4
	                # yellow
	                elif hsv[0] < 60: 
	                    if hsv[2] < 70: c= 7
	                    else: c = 1
	                # green
	                elif hsv[0] < 160: c = 2
	                # blue
	                elif hsv[0] < 255: c = 3
	                # purple
	                elif hsv[0] < 290: c = 5
	                # pink
	                elif hsv[0] < 350: c = 6
	                else: continue
	        color_result[c] += prop
	    sorted_color_result = sorted(color_result.items(), key=lambda x:x[1], reverse=True)
	    return sorted_color_result[:count]
        
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
