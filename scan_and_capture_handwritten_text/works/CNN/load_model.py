from keras.models import model_from_json

def load_model(model_filename="model2"):
    """
    load the saved model
    adapt from: learn from https://machinelearningmastery.com/save-load-keras-deep-learning-models/
    """
    # load json and create model
    json_file = open('{}.json'.format(model_filename), 'r')
    model_json = json_file.read()
    json_file.close()
    model = model_from_json(model_json)
    # load weights into new model
    model.load_weights("{}.h5".format(model_filename))
    
    # evaluate loaded model on test data
    model.compile(loss='categorical_crossentropy', optimizer='adam', metrics=['accuracy'])
    print("done")
    return model

if __name__ == "__main__":
    from load_data import load_data

    model = load_model()
    print(type(model))
    x_train, y_train, x_test, y_test, x_val, y_val, num_classes = load_data()
    # loss and accuracy
    score = model.evaluate(x_test, y_test)
    print(score[1])
