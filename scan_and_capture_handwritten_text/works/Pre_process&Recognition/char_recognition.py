import dewarp
import os
import sys
import cv2
import numpy as np
import matplotlib.pyplot as plt
import tensorflow as tf
from mpl_toolkits import mplot3d
from scipy.io import loadmat
import pandas as pd
from scipy import io as spio
from keras.models import model_from_json
import pytesseract
#%matplotlib inline

pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files (x86)\Tesseract-OCR\tesseract.exe'
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'

def map_to_char(n):
    """
    map the predicted label to ascii mapped character
    """
    if (n < 10):
        return chr(n + 48)
    elif (n < 36):
        return chr(n + 55)
    else:
        return chr(n + 61)


def load_model(model_filename = "model.pkl"):
    """
    load the saved model
    adapt from: learn from https://machinelearningmastery.com/save-load-keras-deep-learning-models/
    """
    # load json and create model
    json_file = open('model.json', 'r')
    print(json_file)
    model_json = json_file.read()
    json_file.close()
    model = model_from_json(model_json)
    # load weights into new model
    model.load_weights("model.h5")
    
    # evaluate loaded model on test data
    model.compile(loss='categorical_crossentropy', optimizer='adam', metrics=['accuracy'])
    print("done")
    return model


def word_contours(im_invert, step=2, thres_t=10):
    """
    Find the contour of a word, since we assuming the
    characters of word may not connected. We need to make
    the character bolder and bolder step by step,
    until they connected as words by finding highest decent value.
    """
    temp = im_invert
    #temp = cv2.GaussianBlur(im_invert,(3,3),0)
    n_cnt_p = np.infty
    diff = np.infty
    res = 0
    while(diff > thres_t):
        temp = cv2.morphologyEx(temp, cv2.MORPH_DILATE, np.ones((step,step)))
        thresh = cv2.threshold(temp, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)[1]
        cnt, _ = cv2.findContours(thresh, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
        diff = n_cnt_p - len(cnt)
        n_cnt_p = len(cnt)
        res += step
    return (res, temp, cnt)


def group_in_lines(rect_arr, thres):
    """
    Group the (word)contour rectangles into lines.
    thres indicates how many tolenrance can we take to
    consider as a line when vertical position is slightly
    off.
    """
    lines = []
    cur = 0
    nx = cur
    #thresh = 20
    while(nx < len(rect_arr)):
        if (cur == nx):
            lines.append(cur)
        if (np.abs(rect_arr[nx, 1] - rect_arr[cur, 1]) < thres):
            nx += 1
        else:
            cur = nx
    lines.append(len(rect_arr))
    return lines


def get_ordered_lines(rect_arr, thres):
    """
    Reorder the lines from top to bottom.
    """
    split_lines = group_in_lines(rect_arr, thres)
    w_lines = []
    for i in range(len(split_lines) - 1):
        start = split_lines[i]
        end = split_lines[i+1]
        arr_to_sort = rect_arr[start:end,:]
        #print(np.argsort(arr_to_sort[:,0]))
        w_lines.append(arr_to_sort[np.argsort(arr_to_sort[:, 0])])
    w_lines.reverse()
    return w_lines


def thresh_rectangle(invert_im, contours, content_ratio = 8, edge_thres = 20):
    """
    Threshold the word contour rectangle to avoid noise
    """
    rect = []
    for c in contours:
        x,y,w,h = cv2.boundingRect(c)
        if ((w * h)/content_ratio * 255 < invert_im[y:y+h,x:x+w].sum() and 1/edge_thres < (w/h) < edge_thres):
            rect.append(np.array([x,y,w,h]))
    return rect


def invert_image(im):
    """
    invert white and black color of the binarized image
    """
    return np.ones_like(im) * 255 - im


def build_word_contour_mask(word_rect, invert_g):
    """
    Get the contour mask of a word patch.
    The main purpose is to avoid finding the
    intersected contours.
    """
    x,y,w,h = (tuple)(word_rect)
    word_patch = invert_g[y:y+h,x:x+w]
    thresh = cv2.threshold(word_patch, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)[1]
    cnt_c, _ = cv2.findContours(thresh, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    rect_0 = thresh_rectangle(word_patch, cnt_c, 6)
    temp = np.zeros_like(word_patch)
    for i in rect_0:
        x,y,w,h = (tuple)(i)
        temp[y:y+h,x:x+w] = 255
    return temp


def fix_ratio_resize(im_t_r, target_shape):
    """
    resize the image patch to a desired shape but
    the ratio is fixed.
    """
    o_im_shape = im_t_r.shape
    #print(ratio, "??", o_im_shape)
    if (o_im_shape[0] > o_im_shape[1]):
        ratio = o_im_shape[0] / o_im_shape[1]
        #print((target_shape, (int)(target_shape/ratio)), "1")
        return cv2.resize(im_t_r, ((int)(target_shape/ratio), target_shape))
    else:
        ratio = o_im_shape[1] / o_im_shape[0]
        #print((target_shape, (int)(target_shape/ratio)), "2")
        return cv2.resize(im_t_r, (target_shape, (int)(target_shape/ratio)))


def resize_to_square(im_t_r, target_shape):
    """
    Resize the image to a suqare shape with fixed ratio
    """
    temp = np.zeros((target_shape, target_shape))
    info_im = fix_ratio_resize(im_t_r, 24)
    thres = 40
    ct = place_at_center(info_im, temp)
    ct[ct < thres] = 0
    return ct


def place_at_center(im_f, im_b):
    """
    place im_f to the center of im_b.
    """
    f_center = np.array(im_f.shape) / 2
    b_center = np.array(im_b.shape) / 2
    disp = (b_center - f_center).astype(np.int32)
    #print(im_f.shape, "??", im_b.shape)
    #im_b[disp[1]+im_f.shape[1], disp[0]+im_f.shape[0]] = im_f
    im_b[disp[0]:disp[0]+im_f.shape[0], disp[1]:disp[1]+im_f.shape[1]] = im_f
    return im_b


# def remove_punctuation(char_patch_l, thres, shape):
#     """
#     Remove some punctuation such as .,": etc
#     according to the area of infomation.
#     """
#     n_c_p_l = []
#     area = shape[0] * shape[1]
#     for c_p in char_patch_l:
#         if (c_p.sum() < thres * area * 255):
#             n_c_p_l.append(c_p)
#     return n_c_p_l


def split_characters_from_word(word, invert_g):
    """
    Cut out the characters from word patches
    """
    w_c_mask = build_word_contour_mask(word, invert_g)
    x,y,w,h = (tuple)(word)
    word_patch = invert_g[y:y+h,x:x+w]
    thresh = cv2.threshold(w_c_mask, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)[1]
    cnt_c, _ = cv2.findContours(thresh, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    rect_1 = thresh_rectangle(word_patch, cnt_c, 6)
    # sorted in left to right word order
    r_1_arr = np.array(rect_1)
    r_1_arr = r_1_arr[np.argsort(r_1_arr[:,0])]
    #...
    char_patch_l = []
    for r in r_1_arr:
        char_patch_l.append(build_standard_character_patch(word_patch, r, 28))
    return char_patch_l
    #return remove_punctuation(char_patch_l, 1/4, (28, 28))


def build_standard_character_patch(word_patch, rect, square_shape):
    """
    Resize to 28 x 28 square patch in order to match
    the model input dimension.
    """
    x,y,w,h = (tuple)(rect)
    c_p = word_patch[y:y+h,x:x+w]
    return resize_to_square(c_p, square_shape)


def final_build_lines(word_lines, invert_g):
    """
    Build the final structure of information we store:
    it is a list with 3 dimentsion:
    L[a][b][c] - a: line #, b: word #, c: character #
    """
    res = []
    for w_l in word_lines:
        line_l = []
        for c_rect in w_l:
            #print(c_rect)
            line_l.append(split_characters_from_word(np.array(c_rect), invert_g))
        res.append(line_l)
    return res


def build_word_im(c_l):
    """
    Build a image represent a word of consists
    of only characters we extract.
    """
    temp = np.zeros((28, 28 * len(c_l)))
    #plt.imshow(c_l[1])
    for i in range(len(c_l)):
        temp[0:28, i*28:(i+1)*28] = c_l[i]
    return temp


def build_line_im(l_l):
    """
    Build a image represent a line of words
    we extract.
    """
    w_p_list = []
    w = 0
    for i in l_l:
        w_im = build_word_im(i)
        w += (w_im.shape[1] + 28)
        w_p_list.append(w_im)
    temp = np.zeros((28, w))
    count = 0
    for i in w_p_list:
        temp[0:28, count:count+i.shape[1]] = i
        count += (i.shape[1] + 28)
    return temp


def build_doc_im(doc_patch):
    """
    Build a entrie image of character maps of the character
    we extract.
    """
    doc_l = []
    l_max = 0
    for i in doc_patch:
        l_im = build_line_im(i)
        l_max = max(l_max, l_im.shape[1])
        doc_l.append(l_im)
    temp = np.zeros((28*len(doc_patch), l_max))
    count = 0
    for i in doc_l:
        temp[count: count+28, 0:i.shape[1]] = i
        count += 28
    return temp


def text_predict_using_model(model, doc_lines):
    """
    Predit a group of text using trained model
    character by character.
    """
    doc = ""
    for l in doc_lines:
        for w in l:
            for c in w:
                mod_in = c.reshape((1, 28, 28, 1))
                doc += str(map_to_char(model.predict(mod_in).argmax()))
            doc += " "
        doc += "\n"
    return doc


def write_txt_file(lines, f_name):
    """
    Write recognized text to a textfile
    """
    f = open(f_name, "w")
    f.write(lines)
    f.close()


def main():
    if len(sys.argv) < 2:
        print('usage:', sys.argv[0], '[image]')
        sys.exit(0)
    im_path = sys.argv[1]
    os.system('python dewarp.py '+ im_path)
    f_name = im_path.split('.')[0]
    image = cv2.imread(f_name+'_thresh.png')
    invert_g = cv2.cvtColor(invert_image(image), cv2.COLOR_BGR2GRAY)
    total_step, mask, cnt = word_contours(invert_g, 2, 10)
    rect = thresh_rectangle(invert_g, cnt, 8)
    rect_arr = np.array(rect)
    word_lines = get_ordered_lines(rect_arr, 20)
    res = final_build_lines(word_lines, invert_g)
    model = load_model()
    doc_predict = text_predict_using_model(model, res)
    write_txt_file(doc_predict, "predict_using_model.txt")
    doc_im = build_doc_im(res)
    cv2.imwrite("assembled_doc.png", doc_im)
    doc_single_chr = pytesseract.image_to_string(doc_im)
    write_txt_file(doc_single_chr, "tesseract_single_char.txt")
    doc_full_page = pytesseract.image_to_string(image)
    write_txt_file(doc_full_page, "tesseract_full_page.txt")


if __name__ == '__main__':
    main()
