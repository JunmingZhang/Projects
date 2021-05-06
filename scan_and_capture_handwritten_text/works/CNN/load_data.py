import numpy as np
import pandas as pd
# import matplotlib.pyplot as plt

from sklearn.model_selection import train_test_split
from keras.utils import np_utils

# constants
HEIGHT = 28
WIDTH = 28

def load_data():
    """
    load data from EMNIST byclass dataset
    adapt from https://www.kaggle.com/ashwani07/emnist-using-keras-cnn

    The dataset byclass has character type digits, uppercase and lowercase
    letters from https://www.kaggle.com/crawford/emnist

    output format:
    x_train:  (628137, 28, 28, 1)
    y_train:  (628137, 62) 62 one-hot bits: 10 digits + 26 uppercase + 26 lowercase
    x_test:  (116322, 28, 28, 1)
    y_test:  (116322, 62)
    x_val:  (69794, 28, 28, 1)
    y_val:  (69794, 62)
    """
    # read from the csv dataset
    train = pd.read_csv("../../emnist/emnist-byclass-train.csv", delimiter = ',')
    test = pd.read_csv("../../emnist/emnist-byclass-test.csv", delimiter = ',')
    mapp = pd.read_csv("../../emnist/emnist-byclass-mapping.txt", delimiter = ' ', \
                    index_col=0, header=None, squeeze=True)
    # split x and y
    x_train = train.iloc[:,1:]
    y_train = train.iloc[:,0]
    x_test = test.iloc[:,1:]
    y_test = test.iloc[:,0]

    # reform data memory to array
    x_train = np.asarray(x_train)
    x_test = np.asarray(x_test)
    
    # number of classes: 10 digits + 26 uppercase + 26 lowercase = 62
    num_classes = y_train.nunique()

    # one hot encoding (categorical data), use the index to map the code of the character
    y_train = np_utils.to_categorical(y_train, num_classes)
    y_test = np_utils.to_categorical(y_test, num_classes)

    # reshape image for CNN
    x_train = x_train.reshape(-1, HEIGHT, WIDTH, 1)
    x_test = x_test.reshape(-1, HEIGHT, WIDTH, 1)

    # partition to train and val
    x_train, x_val, y_train, y_val = train_test_split(x_train, y_train, test_size= 0.10, random_state=7)

    return x_train, y_train, x_test, y_test, x_val, y_val, num_classes

if __name__ == "__main__":
    x_train, y_train, x_test, y_test, x_val, y_val, _ = load_data()

    print(x_train.shape)
    print(y_train.shape)
    print(x_test.shape)
    print(y_test.shape)
    print(x_val.shape)
    print(y_val.shape)
    # pd.read_csv("../emnist/try.txt")
